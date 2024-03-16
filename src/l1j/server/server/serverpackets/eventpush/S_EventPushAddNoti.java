package l1j.server.server.serverpackets.eventpush;

import l1j.server.GameSystem.eventpush.bean.EventPushObject;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_EventPushAddNoti extends ServerBasePacket {
	private static final String S_EVENT_PUSH_ADD_NOTI = "[S] S_EventPushAddNoti";
	private byte[] _byte = null;
	public static final int NOTI = 0x099f;
	
	public S_EventPushAddNoti(L1PcInstance pc, EventPushObject temp) {
		write_init();
		L1ItemInstance dummy = new L1ItemInstance();
		int expire_date = (int) (System.currentTimeMillis() / 1000);
		int enable_date = (int) (temp.getEnableDate().getTime() / 1000);
		write_event_push_info(pc, temp, enable_date, expire_date, dummy);
		dummy = null;
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_event_push_info(L1PcInstance pc, EventPushObject temp, int enable_date, int expire_date, L1ItemInstance dummy) {
		EventPushInfoStream os = null;
		try {
			os = new EventPushInfoStream(pc, temp, enable_date, expire_date, dummy);
			writeRaw(0x0a);// event_push_info
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
		return S_EVENT_PUSH_ADD_NOTI;
	}
}

