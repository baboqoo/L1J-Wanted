package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_CharAmount extends ServerBasePacket {
	private static final String S_CHAR_AMOUNT = "[S] S_CharAmount";
	private byte[] _byte = null;

	public S_CharAmount(int value, int slot) {
		writeC(Opcodes.S_NUM_CHARACTER);
		writeC(value);
		writeC(slot);
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
		return S_CHAR_AMOUNT;
	}
}

