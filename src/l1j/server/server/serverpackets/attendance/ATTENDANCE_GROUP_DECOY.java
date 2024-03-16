package l1j.server.server.serverpackets.attendance;

import l1j.server.server.utils.BinaryOutputStream;

public class ATTENDANCE_GROUP_DECOY extends BinaryOutputStream {
	public ATTENDANCE_GROUP_DECOY(int group_id, int attendance_id, int season_num) {
		super();
		write_group_id(group_id);
		write_attendance_id(attendance_id);
		write_season_num(season_num);
	}
	
	void write_group_id(int group_id) {
		writeC(0x08);// group_id
		writeC(group_id);
	}
	
	void write_attendance_id(int attendance_id) {
		writeC(0x10);// attendance_id
		writeC(attendance_id);
	}
	
	void write_season_num(int season_num) {
		writeC(0x18);// season_num
		writeC(season_num);
	}
}

