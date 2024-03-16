package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_PlayTimeLimitNoti extends ServerBasePacket {
	private static final String S_PLAY_TIME_LIMIT_NOTI = "[S] S_PlayTimeLimitNoti";
	private byte[] _byte = null;
	public static final int NOTI = 0x0141;
	
	public S_PlayTimeLimitNoti(int playTimeLimitSec, int remainPlayTimeSec) {
		write_init();
		write_playTimeLimitSec(playTimeLimitSec);
		write_remainPlayTimeSec(remainPlayTimeSec);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_playTimeLimitSec(int playTimeLimitSec) {
		writeRaw(0x08);// playTimeLimitSec
		writeBit(playTimeLimitSec);
	}
	
	void write_remainPlayTimeSec(int remainPlayTimeSec) {
		writeRaw(0x10);// remainPlayTimeSec
		writeBit(remainPlayTimeSec);
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
		return S_PLAY_TIME_LIMIT_NOTI;
	}
}

