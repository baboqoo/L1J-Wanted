package l1j.server.server.serverpackets.eventpush;

import java.util.Collection;

import l1j.server.GameSystem.eventpush.bean.EventPushObject;
import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_EventPushUpdateInfoList extends ServerBasePacket {
	private static final String S_EVENT_PUSH_UPDATE_INFO_LIST = "[S] S_EventPushUpdateList";
	private byte[] _byte = null;
	public static final int LIST = 0x09aa;
	
	public S_EventPushUpdateInfoList(Collection<EventPushObject> tempList) {
		write_init();
		write_result(EventPushResultCode.EVENT_PUSH_SUCCESS);
		int expire_date = (int) (System.currentTimeMillis() / 1000);
		for (EventPushObject obj : tempList) {
			int enable_date = (int) (obj.getEnableDate().getTime() / 1000);
			if (enable_date <= expire_date) {// 제한시간 지난 물품 제외
				continue;
			}
			write_event_push_update_info_list(obj, enable_date, expire_date);
		}
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(LIST);
	}
	
	void write_result(EventPushResultCode result) {
		writeRaw(0x08);// result
		writeRaw(result.toInt());
	}
	
	void write_event_push_update_info_list(EventPushObject obj, int enable_date, int expire_date) {
		EventPushUpdateInfoStream os = null;
		try {
			os = new EventPushUpdateInfoStream(obj.getEventPushId(), 60, enable_date - expire_date, 0);
			writeRaw(0x12);
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
	
	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_EVENT_PUSH_UPDATE_INFO_LIST;
	}
}

