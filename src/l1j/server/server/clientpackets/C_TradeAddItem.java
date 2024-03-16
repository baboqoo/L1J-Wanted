package l1j.server.server.clientpackets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import l1j.server.L1DatabaseFactory;
import l1j.server.GameSystem.charactertrade.CharacterTradeManager;
import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Trade;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

public class C_TradeAddItem extends ClientBasePacket {
	private static final String C_TRADE_ADD_ITEM = "[C] C_TradeAddItem";
	
	public C_TradeAddItem(byte abyte0[], GameClient client)throws Exception {
		super(abyte0);
		L1PcInstance pc = client.getActiveChar();
		if (pc == null) {
			return;
		}
		
		int itemobjid = readD();
		int itemcount = readD();
		L1ItemInstance item = pc.getInventory().getItem(itemobjid);
		if (item == null) {
			return; 
		}
		
		/** 버그 방지 **/
		if (itemobjid != item.getId()) {
			return;
		}
		if (!item.isMerge() && itemcount != 1) {
			return;
		}
		if (itemcount <= 0 || item.getCount() <= 0) {
			return;
		}
		if (itemcount > item.getCount()) {
			itemcount = item.getCount();
		}
		if (itemcount > L1Inventory.MAX_AMOUNT) {
			return;
		}
		
		/** 시간제아이템 교환불가 */
		if (item.getEndTime() != null 
				&& !(item.getItemId() == L1ItemId.INN_ROOM_KEY || item.getItemId() == L1ItemId.INN_HALL_KEY 
				|| item.getItemId() == 80500 || item.getItemId() == 31236)) {
			pc.sendPackets(L1ServerMessage.sm125);
			return;
		}
		if (item.isEquipped()) {
			pc.sendPackets(L1ServerMessage.sm125);
			return;
		}
		// 케릭터 교환
		if (item.getItemId() == L1ItemId.HIGH_CHARACTER_TRADE || item.getItemId() == L1ItemId.LOW_CHARACTER_TRADE || item.getItemId() == CharacterTradeManager.MARBLE_STORE_ID) {
			if (isValidQuiz(pc, StringUtil.EmptyString) || !pc.isQuizValidated()) {
				pc.sendPackets(L1SystemMessage.SECURITY_NOT_SETTING);
				return;
			}
			if (pc.getLevel() >= 70 && item.getItemId() == L1ItemId.LOW_CHARACTER_TRADE) {
				pc.sendPackets(L1SystemMessage.CHAR_TRADE_ITEM_70LEVEL);
				return;
			} else if (pc.getLevel() < 70 && item.getItemId() == L1ItemId.HIGH_CHARACTER_TRADE) {
				pc.sendPackets(L1SystemMessage.CHAR_TRADE_ITEM_70LEVEL_LOW);
				return;
			}
		}

		if (!item.getItem().isTradable() || item.getBless() >= 128 || item.isSlot()) {
			pc.sendPackets(new S_ServerMessage(210, item.getItem().getDesc()), true); // \f1%0은 버리거나 또는 타인에게 양일을 할 수 없습니다.
			return;
		}
		
		if (pc.getPet() != null && pc.getPet().getAmuletId() == item.getId()) {
			pc.sendPackets(new S_ServerMessage(210, item.getItem().getDesc()), true);// \f1%0은 버리거나 또는 타인에게 양일을 할 수 없습니다.
			return;
		}
		
		if (pc.getDoll() != null && item.getId() == pc.getDoll().getItemObjId()) {
			pc.sendPackets(new S_ServerMessage(210, item.getItem().getDesc()), true);
			return;
		}
		
		if (pc.getTradeOk()) {
			return;
		}
		L1PcInstance tradingPartner = (L1PcInstance) L1World.getInstance().findObject(pc.getTradeID());
		if (tradingPartner == null) {
			return;
		}
		if (pc.getTradeOk() || tradingPartner.getTradeOk()) {
			pc.sendPackets(L1SystemMessage.TRADE_COMPLETE_CANNOT_ADD);
			tradingPartner.sendPackets(L1SystemMessage.TRADE_COMPLETE_CANNOT_ADD);
			return;
		}
		if (tradingPartner.getInventory().checkAddItem(item, itemcount) != L1Inventory.OK) {
			tradingPartner.sendPackets(L1ServerMessage.sm270);
			pc.sendPackets(L1ServerMessage.sm271);
			return;
		}
		L1Trade trade = new L1Trade();
		trade.TradeAddItem(pc, itemobjid, itemcount);
	}
	
	private boolean isValidQuiz(L1PcInstance pc, String quiz) {
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		boolean result			= false;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement(String.format("SELECT quiz FROM accounts WHERE login='%s'", pc.getAccountName()));
			rs		= pstm.executeQuery();
			String oldQuiz = StringUtil.EmptyString;
			if (rs.next()) {
				oldQuiz = rs.getString(1);
			}
			if (oldQuiz == null || oldQuiz.equalsIgnoreCase(quiz)) {
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return result;
	}

	@Override
	public String getType() {
		return C_TRADE_ADD_ITEM;
	}
}

