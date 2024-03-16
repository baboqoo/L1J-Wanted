package l1j.server.server.clientpackets;

import l1j.server.server.GameClient;
import l1j.server.server.model.L1Trade;
import l1j.server.server.model.Instance.L1PcInstance;

public class C_TradeCancel extends ClientBasePacket {
	private static final String C_TRADE_CANCEL = "[C] C_TradeCancel";

	public C_TradeCancel(byte abyte0[], GameClient clientthread) throws Exception {
		super(abyte0);
		L1PcInstance player = clientthread.getActiveChar();
		if (player == null || !player.getTradeReady()) {
			return;
		}
		L1Trade trade = new L1Trade();
		trade.TradeCancel(player);
	}

	@Override
	public String getType() {
		return C_TRADE_CANCEL;
	}

}

