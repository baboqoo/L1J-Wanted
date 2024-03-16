package l1j.server.server.serverpackets.trade;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_TradeStatus extends ServerBasePacket {
	private static final String S_TRADE_STATUS = "[S] S_TradeStatus";
	private byte[] _byte = null;
	
	public static final S_TradeStatus OK		= new S_TradeStatus(0);
	public static final S_TradeStatus CANCEL	= new S_TradeStatus(1);
	
	public S_TradeStatus(int type) {
		writeC(Opcodes.S_XCHG_RESULT);
		writeC(type);// 0:거래 완료 1:거래 캔슬
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}
	
	@Override
	public String getType() {
		return S_TRADE_STATUS;
	}
}

