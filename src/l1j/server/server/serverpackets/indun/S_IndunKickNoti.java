package l1j.server.server.serverpackets.indun;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_IndunKickNoti extends ServerBasePacket {
	private byte[] _byte = null;
	private static final String S_INDUN_KICK_NOTI = "[S] S_IndunKickNoti";
	public static final int KICK_NOTI 	= 0x08bc;
	
	public S_IndunKickNoti(int room_id, L1PcInstance kick_char) {
		write_init();
		write_room_id(room_id);
		write_kick_arena_char_id(kick_char.getId());
		write_kick_char_name(kick_char.getName().getBytes());
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(KICK_NOTI);
	}
	
	void write_room_id(int room_id) {
		writeC(0x08);// room_id
		writeBit(room_id);
	}
	
	void write_kick_arena_char_id(int kick_arena_char_id) {
		writeC(0x10);// kick_arena_char_id
		writeBit(kick_arena_char_id);
	}
	
	void write_kick_char_name(byte[] kick_char_name) {
		writeC(0x12);// kick_char_name
		writeBytesWithLength(kick_char_name);
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
		return S_INDUN_KICK_NOTI;
	}
}
