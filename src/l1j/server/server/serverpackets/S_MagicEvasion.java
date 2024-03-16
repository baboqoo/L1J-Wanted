package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_MagicEvasion extends ServerBasePacket {
	private static final String S_MAGIC_EVASION = "[S] S_MagicEvasion";
	private byte[] _byte = null;
	public static final int MAGIC_EVASION	= 0x01e8;// 확률 마법 회피
	
	public S_MagicEvasion(int value) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(MAGIC_EVASION);
		writeRaw(0x08);
		writeBit(value);
		writeH(0x00);
	}
	
	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = _bao.toByteArray();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_MAGIC_EVASION;
	}
}

