package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_DeadRestart extends ServerBasePacket {
	private static final String S_DEAD_RESTART = "[S] S_DeadRestart";
	private byte[] _byte = null;
	public static final int DEAD_RESTART = 0x08cf;
	
	public static final S_DeadRestart SUCCESS = new S_DeadRestart(true);
	
	public S_DeadRestart(boolean success) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(DEAD_RESTART);
		writeRaw(0x08);// success
		writeB(success);
        writeH(0x00);
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
		return S_DEAD_RESTART;
	}

}

