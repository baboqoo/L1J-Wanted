package l1j.server.server.serverpackets.eventpush;

import l1j.server.server.utils.BinaryOutputStream;

public class EventPushUpdateInfoStream extends BinaryOutputStream {
	public EventPushUpdateInfoStream(int event_push_id, int reset_num, int remain_time, int event_push_status) {
		super();
		write_event_push_id(event_push_id);
		write_reset_num(reset_num);
		write_remain_time(remain_time);
		write_event_push_status(event_push_status);
	}
	
	void write_event_push_id(int event_push_id) {
		writeC(0x08);// event_push_id
		writeBit(event_push_id);
	}
	
	void write_reset_num(int reset_num) {
		writeC(0x10);// reset_num
		writeC(reset_num);
	}
	
	void write_remain_time(int remain_time) {
		writeC(0x18);// remain_time
		writeBit(remain_time);
	}
	
	void write_event_push_status(int event_push_status) {
		writeC(0x20);// event_push_status
		writeC(event_push_status);
	}
}

