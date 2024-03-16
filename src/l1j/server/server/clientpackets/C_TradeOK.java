package l1j.server.server.clientpackets;

import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1Trade;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

public class C_TradeOK extends ClientBasePacket {
	private static final String C_TRADE_CANCEL = "[C] C_TradeOK";

	public C_TradeOK(byte abyte0[], GameClient clientthread) throws Exception {
		super(abyte0);

		L1PcInstance player = clientthread.getActiveChar();
		if (player == null || !player.getTradeReady()) {
			return;
		}
		L1PcInstance trading_partner = (L1PcInstance) L1World.getInstance().findObject(player.getTradeID());
		if (trading_partner != null) {
			player.setTradeOk(true);
			if (player.getTradeOk() && trading_partner.getTradeOk()) {// 모두 OK를 눌렀다
				// (180 - 16)개 미만이라면 트레이드 성립.
				// 본래는 겹치는 아이템(아데나등 )을 이미 가지고 있는 경우를 고려하지 않는 차면 안 된다.
				if (player.getInventory().getSize() < (L1PcInventory.MAX_SIZE - 16) && trading_partner.getInventory().getSize() < (L1PcInventory.MAX_SIZE - 16)) {// 서로의 아이템을 상대에게 건네준다
					L1Trade trade = new L1Trade();
					trade.TradeOK(player);
					player.saveInventory();
					trading_partner.saveInventory();
				} else {// 서로의 아이템을 수중에 되돌린다
					player.sendPackets(L1ServerMessage.sm263); // \f1한사람의 캐릭터가 가지고 걸을 수 있는 아이템은 최대 180개까지입니다.
					trading_partner.sendPackets(L1ServerMessage.sm263); // \f1한사람의 캐릭터가 가지고 걸을 수 있는 아이템은 최대 180개까지입니다.
					L1Trade trade = new L1Trade();
					trade.TradeCancel(player);
				}
			}
		} else {
			L1Trade trade = new L1Trade();
			trade.TradeOK(player);
		}
	}

	@Override
	public String getType() {
		return C_TRADE_CANCEL;
	}

}

