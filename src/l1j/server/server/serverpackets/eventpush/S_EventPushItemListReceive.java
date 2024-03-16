package l1j.server.server.serverpackets.eventpush;

import java.util.Iterator;

import javolution.util.FastMap;
import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_EventPushItemListReceive extends ServerBasePacket {
	private static final String S_EVENT_PUSH_ITEM_LIST_RECEIVE = "[S] S_EventPushItemListReceive";
	private byte[] _byte = null;
	public static final int RECEIVE	= 0x09a8;
	
	public S_EventPushItemListReceive(FastMap<Integer, Integer> infoMap) {
		write_init();
		Iterator<Integer> it = infoMap.keySet().iterator();
		while(it.hasNext()){
			int event_push_id	= it.next();
			int reset_num		= infoMap.get(event_push_id);
			write_event_push_result_list(event_push_id, reset_num);
		}
		write_result(EventPushResultCode.EVENT_PUSH_SUCCESS);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(RECEIVE);
	}
	
	void write_event_push_result_list(int event_push_id, int reset_num) {
		EventPushResultStream os = null;
		try {
			os = new EventPushResultStream(event_push_id, reset_num, EventPushResultCode.EVENT_PUSH_SUCCESS);
			writeRaw(0x0a);// event_push_result_list
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
		writeRaw(0x10);// result
		writeRaw(result.toInt());
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
		return S_EVENT_PUSH_ITEM_LIST_RECEIVE;
	}
}

