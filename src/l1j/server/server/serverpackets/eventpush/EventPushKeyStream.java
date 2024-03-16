package l1j.server.server.serverpackets.eventpush;

import l1j.server.server.utils.BinaryOutputStream;

public class EventPushKeyStream extends BinaryOutputStream {
	
	public EventPushKeyStream(int event_push_id, int reset_num) {
		super();
		write_event_push_id(event_push_id);
		write_reset_num(reset_num);
	}
	
	void write_event_push_id(int event_push_id) {
		writeC(0x08);// event_push_id
		writeBit(event_push_id);
	}
	
	void write_reset_num(int reset_num) {
		writeC(0x10);// reset_num
		writeC(reset_num);
	}
}

