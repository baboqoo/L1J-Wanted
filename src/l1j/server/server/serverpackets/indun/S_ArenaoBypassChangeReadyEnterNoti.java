package l1j.server.server.serverpackets.indun;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_ArenaoBypassChangeReadyEnterNoti extends ServerBasePacket {
	private byte[] _byte = null;
	private static final String S_ARENACO_BYPASS_CHANGE_READY_ENTER_NOTI = "[S] S_ArenaoBypassChangeReadyEnterNoti";
	public static final int NOTI 	= 0x02d5;
	
	public S_ArenaoBypassChangeReadyEnterNoti(int room_id, int arena_char_id, boolean ready) {
		write_init();
		write_room_id(room_id);
		write_arena_char_id(arena_char_id);
		write_ready(ready);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_room_id(int room_id) {
		writeRaw(0x08);// room_id
		writeBit(room_id);
	}
	
	void write_arena_char_id(int arena_char_id) {
		writeC(0x10);// arena_char_id
		writeBit(arena_char_id);
	}
	
	void write_ready(boolean ready) {
		writeRaw(0x18);// ready
		writeB(ready);
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
		return S_ARENACO_BYPASS_CHANGE_READY_ENTER_NOTI;
	}
}
