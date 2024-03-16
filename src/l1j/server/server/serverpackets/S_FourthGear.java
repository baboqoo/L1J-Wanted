package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_FourthGear extends ServerBasePacket {
	private byte[] _byte = null;
	private static final String S_FOURTH_GEAR = "[S] S_FourthGear";
	public static final int GEAR = 0x094e;

	public S_FourthGear(int targetId, boolean enable) {
		write_init();
		write_targetId(targetId);
		write_enable(enable);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(GEAR);
	}
	
	void write_targetId(int targetId) {
		writeRaw(0x08);
		writeBit(targetId);
	}
	
	void write_enable(boolean enable) {
		writeRaw(0x10);
		writeB(enable);
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
		return S_FOURTH_GEAR;
	}
}
