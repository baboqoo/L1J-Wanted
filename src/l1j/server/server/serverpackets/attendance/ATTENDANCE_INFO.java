package l1j.server.server.serverpackets.attendance;

import l1j.server.server.utils.BinaryOutputStream;

public class ATTENDANCE_INFO extends BinaryOutputStream {

	public ATTENDANCE_INFO() {
		super();
	}
	
	void write_attendance_id(int attendance_id) {
		writeC(0x08);// attendance_id
		writeBit(attendance_id);
	}
	
	void write_group_id(int group_id) {
		writeC(0x10);// group_id
		writeC(group_id);
	}
	
	void write_status(int status) {
		writeC(0x18);// status
		writeC(status);
	}
	
	void write_playtimeminute(int playtimeminute) {
		writeC(0x20);// playtimeminute
		writeBit(playtimeminute);
	}
	
	void write_reserved_random(byte[] reserved_random) {
		writeC(0x2a);// reserved_random
		writeBytesWithLength(reserved_random);
	}
	
	void write_season_num(int season_num) {
		writeC(0x30);// season_num
		writeC(season_num);
	}
	
}

