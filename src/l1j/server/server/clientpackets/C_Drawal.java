package l1j.server.server.clientpackets;

import l1j.server.common.data.eBloodPledgeRankType;
import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.controller.action.War;
import l1j.server.server.datatables.CastleTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.templates.L1Castle;

public class C_Drawal extends ClientBasePacket {

	private static final String C_DRAWAL = "[C] C_Drawal";

	public C_Drawal(byte abyte0[], GameClient clientthread) throws Exception {
		super(abyte0);
		try {
			readD();
			int j = readD();

			L1PcInstance pc = clientthread.getActiveChar();
			if (pc == null) {
				return;
			}
			L1Clan clan = pc.getClan();
			if (clan == null) {
				return;
			}
			int castle_id = clan.getCastleId();
			if (castle_id == 0) {
				return;
			}
			if (War.getInstance().isNowWar(clan.getCastleId())) {
				pc.sendPackets(L1SystemMessage.WAR_TIME_ACTION_FAIL);
				return;
			}
			if (pc.getBloodPledgeRank() != eBloodPledgeRankType.RANK_NORMAL_KING || !pc.isCrown() || pc.getId() != pc.getClan().getLeaderId()) {
				return;
			}
			L1Castle l1castle = CastleTable.getInstance().getCastleTable(castle_id);
			int money = l1castle.getPublicMoney();
			long _money = money;

			if (_money <= 0 || money < j) {// 버그방지
				return;
			}
			money -= j;
			L1ItemInstance item = ItemTable.getInstance().createItem(L1ItemId.ADENA);
			
			if (item != null) {
				l1castle.setPublicMoney(money);
				CastleTable.getInstance().updateCastle(l1castle);
				if (pc.getInventory().checkAddItem(item, j) == L1Inventory.OK && pc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(L1ItemId.ADENA), j) == L1Inventory.OK) {
					pc.getInventory().storeItem(L1ItemId.ADENA, j);
				} else { 
					L1World.getInstance().getInventory(pc.getX(), pc.getY(),pc.getMapId()).storeItem(L1ItemId.ADENA, j);
				}
//				pc.sendPackets(new S_SystemMessage("공금 " + j + " 아데나를 인출하였습니다."), true);
				S_ServerMessage sm = new S_ServerMessage(143, "$457", String.format("$4 (%d)", j));
				pc.sendPackets(sm, true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			clear();
		}
	}

	@Override
	public String getType() {
		return C_DRAWAL;
	}
}

