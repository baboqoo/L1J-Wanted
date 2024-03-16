package l1j.server.server.serverpackets.eventpush;

import java.util.Collection;

import l1j.server.GameSystem.eventpush.bean.EventPushObject;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_EventPushInfoList extends ServerBasePacket {
	private static final String S_EVENT_PUSH_INFO_LIST = "[S] S_EventPushInfoList";
	private byte[] _byte = null;
	public static final int LIST = 0x09a2;
	
	public S_EventPushInfoList(L1PcInstance pc, Collection<EventPushObject> tempList) {
		write_init();
		write_result(EventPushResultCode.EVENT_PUSH_SUCCESS);
		
		L1ItemInstance dummy = new L1ItemInstance();
		int expire_date = (int) (System.currentTimeMillis() / 1000);
		for (EventPushObject temp : tempList) {
			int enable_date = (int) ((temp.getEnableDate().getTime()) / 1000);
			if (enable_date <= expire_date) {// 제한시간 지난 물품 제외
				continue;
			}
			write_event_push_info_list(pc, temp, enable_date, expire_date, dummy);
		}
		write_current_page(1);
		write_total_page(1);
		dummy = null;

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
	
	void write_event_push_info_list(L1PcInstance pc, EventPushObject temp, int enable_date, int expire_date, L1ItemInstance dummy) {
		EventPushInfoStream os = null;
		try {
			os = new EventPushInfoStream(pc, temp, enable_date, expire_date, dummy);
			writeRaw(0x12);// event_push_info_list
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
	
	void write_current_page(int current_page) {
		writeRaw(0x18);// current_page
		writeRaw(current_page);
	}
	
	void write_total_page(int total_page) {
		writeRaw(0x20);// total_page
		writeRaw(total_page);
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
		return S_EVENT_PUSH_INFO_LIST;
	}
}

