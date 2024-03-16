package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_MonsterKillCounter extends ServerBasePacket {
	private static final String S_MONSTER_KILL_COUNTER = "[S] S_MonsterKillCounter";
	private byte[] _byte = null;
	public static final int KILL_COUNT = 0x08cd;

	public S_MonsterKillCounter(int value){
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(KILL_COUNT);
		writeC(0x08);
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
		return S_MONSTER_KILL_COUNTER;
	}
}

