package l1j.server.server.serverpackets.trade;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_Trade extends ServerBasePacket {
	private static final String S_TRADE = "[S] S_Trade";
	private byte[] _byte = null;
	
	public S_Trade(String name) {
		writeC(Opcodes.S_XCHG_START);
		writeS(name);
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
		return S_TRADE;
	}
}

