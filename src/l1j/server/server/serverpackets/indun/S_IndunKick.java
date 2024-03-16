package l1j.server.server.serverpackets.indun;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_IndunKick extends ServerBasePacket {
	private byte[] _byte = null;
	private static final String S_INDUN_KICK = "[S] S_IndunKick";
	public static final int KICK = 0x08bb;

	public S_IndunKick(ArenaKickResult result, int room_id, int kick_arena_char_id) {
		write_init();
		write_result(result);
		write_room_id(room_id);
		write_kick_arena_char_id(kick_arena_char_id);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(KICK);
	}
	
	void write_result(ArenaKickResult result) {
		writeRaw(0x08);// result
		writeRaw(result.value);
	}
	
	void write_room_id(int room_id) {
		writeRaw(0x10);// room_id
		writeBit(room_id);
	}
	
	void write_kick_arena_char_id(int kick_arena_char_id) {
		writeRaw(0x18);// kick_arena_char_id
		writeBit(kick_arena_char_id);
	}
 	
 	public enum ArenaKickResult{
		SUCCESS(1),
		FAIL(2),
		FAIL_NOT_EXIST_KICKER(3),
		FAIL_INVALID_KICKER(4),
		FAIL_NOT_EXIST_TARGET(5),
		FAIL_ROOM_STATE(6),
		FAIL_ROOM_EXIT(7),
		FAIL_INVALID_ROOM_TYPE(8),
		;
		private int value;
		ArenaKickResult(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(ArenaKickResult v){
			return value == v.value;
		}
		public static ArenaKickResult fromInt(int i){
			switch(i){
			case 1:
				return SUCCESS;
			case 2:
				return FAIL;
			case 3:
				return FAIL_NOT_EXIST_KICKER;
			case 4:
				return FAIL_INVALID_KICKER;
			case 5:
				return FAIL_NOT_EXIST_TARGET;
			case 6:
				return FAIL_ROOM_STATE;
			case 7:
				return FAIL_ROOM_EXIT;
			case 8:
				return FAIL_INVALID_ROOM_TYPE;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments ArenaKickResult, %d", i));
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
		return S_INDUN_KICK;
	}
}
