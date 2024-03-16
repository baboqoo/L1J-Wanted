package l1j.server.server.serverpackets.eventpush;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_EventPushDeleteNoti extends ServerBasePacket {
	private static final String S_EVENT_PUSH_DELETE_NOTI = "[S] S_EventPushDeleteNoti";
	private byte[] _byte = null;
	public static final int NOTI = 0x09a0;
	
	public S_EventPushDeleteNoti(int event_push_id, int reset_num) {
		write_init();
		write_event_push_id(event_push_id);
		write_reset_num(reset_num);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_event_push_id(int event_push_id) {
		writeRaw(0x08);
		writeBit(event_push_id);
	}
	
	void write_reset_num(int reset_num) {
		writeRaw(0x10);
		writeBit(reset_num);
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
		return S_EVENT_PUSH_DELETE_NOTI;
	}
}

