package l1j.server.server.serverpackets.eventpush;

import l1j.server.server.utils.BinaryOutputStream;

public class EventPushResultStream extends BinaryOutputStream {
	
	public EventPushResultStream(int event_push_id, int reset_num, EventPushResultCode result) {
		super();
		write_event_push_key(event_push_id, reset_num);
		write_result(result);
	}
	
	void write_event_push_key(int event_push_id, int reset_num) {
		EventPushKeyStream os = null;
		try {
			os = new EventPushKeyStream(event_push_id, reset_num);
			writeC(0x0a);// event_push_key
			writeBytesWithLength(os.getBytes());
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				os = null;
			}
		}
	}
	
	void write_result(EventPushResultCode result) {
		writeC(0x10);// result
		writeC(result.toInt());
	}
	
}

