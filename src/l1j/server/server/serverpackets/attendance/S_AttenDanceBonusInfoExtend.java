package l1j.server.server.serverpackets.attendance;

import l1j.server.Config;
import l1j.server.GameSystem.attendance.AttendanceGroupType;
import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_AttenDanceBonusInfoExtend extends ServerBasePacket {
	private static final String S_ATTENDANCE_BONUS_INFO_EXTEND = "[S] S_AttenDanceBonusInfoExtend";
	private byte[] _byte = null;
	public static final int EXTEND	= 0x0220;
	
	public static final S_AttenDanceBonusInfoExtend SEASON_FIRST	= new S_AttenDanceBonusInfoExtend(0);
	public static final S_AttenDanceBonusInfoExtend SEASON_SECOND	= new S_AttenDanceBonusInfoExtend(1);
	
	public S_AttenDanceBonusInfoExtend(int season_num) {
		write_init();
		write_checkInterval(Config.ATTEND.ATTENDANCE_DAILY_MINUTE);// 60분
		write_resetPeriod(Config.ATTEND.ATTENDANCE_REST_PERIOD_HOUR);// 24시간
		write_dailyMaxCount(Config.ATTEND.ATTENDANCE_DAILY_MAX_COUNT);
		write_weekendMaxCount(Config.ATTEND.ATTENDANCE_WEEKEND_MAX_COUNT);
		write_totalBonusGroupCount(AttendanceGroupType.getAllSize());
		write_rest_gauge_icon_display(true);
		write_rest_gauge_bonus_display(100);
		write_season_num(season_num);
		writeH(0);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(EXTEND);
	}
	
	void write_checkInterval(int checkInterval) {
		writeRaw(0x08);// checkInterval
		writeRaw(checkInterval);
	}
	
	void write_resetPeriod(int resetPeriod) {
		writeRaw(0x10);// resetPeriod
		writeRaw(resetPeriod);
	}
	
	void write_dailyMaxCount(int dailyMaxCount) {
		writeRaw(0x18);// dailyMaxCount
		writeRaw(dailyMaxCount);
	}
	
	void write_weekendMaxCount(int weekendMaxCount) {
		writeRaw(0x20);// weekendMaxCount
		writeRaw(weekendMaxCount);
	}
	
	void write_totalBonusGroupCount(int totalBonusGroupCount) {
		writeRaw(0x28);// totalBonusGroupCount
		writeRaw(totalBonusGroupCount);
	}
	
	void write_rest_gauge_icon_display(boolean rest_gauge_icon_display) {
		writeRaw(0x30);// rest_gauge_icon_display
		writeB(rest_gauge_icon_display);
	}
	
	void write_rest_gauge_bonus_display(int rest_gauge_bonus_display) {
		writeRaw(0x38);// rest_gauge_bonus_display
		writeBit(rest_gauge_bonus_display);
	}
	
	void write_season_num(int season_num) {
		writeRaw(0x40);// season_num
		writeRaw(season_num);
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
		return S_ATTENDANCE_BONUS_INFO_EXTEND;
	}
}
