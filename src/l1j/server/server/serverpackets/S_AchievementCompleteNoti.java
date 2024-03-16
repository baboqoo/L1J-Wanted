package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_AchievementCompleteNoti extends ServerBasePacket {
	private static final String S_ACHIEVEMENT_COMPLETE_NOTI = "[S] S_AchievementCompleteNoti";
	private byte[] _byte = null;
	public static final int NOTI = 0x0238;
	
	public S_AchievementCompleteNoti(int achievement_id, int section) {
		achievement_id = achievement_id * 3;
		if (section == 5) {
			achievement_id -= 2;
		} else if (section == 6) {
			achievement_id -= 1;
		} else if (section != 7) {
			achievement_id = 0;
		}
		write_init();
		write_achievement_id(achievement_id);
		write_completed_time(System.currentTimeMillis() / 1000L);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_achievement_id(int achievement_id) {
		writeRaw(0x08);// achievement_id
		writeBit(achievement_id);
	}
	
	void write_completed_time(long completed_time) {
		writeRaw(0x10);// completed_time
		writeBit(completed_time);
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
		return S_ACHIEVEMENT_COMPLETE_NOTI;
	}
}

