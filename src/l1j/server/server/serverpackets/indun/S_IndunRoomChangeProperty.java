package l1j.server.server.serverpackets.indun;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_IndunRoomChangeProperty extends ServerBasePacket {
	private byte[] _byte = null;
	private static final String S_INDUN_ROOM_CHANGE_PROPERTY = "[S] S_IndunRoomChangeProperty";
	public static final int PROPERTY 	= 0x08aa;
	
	public S_IndunRoomChangeProperty(S_IndunRoomChangeProperty.eResult result, int room_id) {
		write_init();
		write_result(result);
		write_room_id(room_id);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(PROPERTY);
	}
	
	void write_result(S_IndunRoomChangeProperty.eResult result) {
		writeRaw(0x08);// result
		writeRaw(result.value);
	}
	
	void write_room_id(int room_id) {
		writeRaw(0x10);// room_id
		writeBit(room_id);
	}
	
	public enum eResult{
		SUCCESS(1),
		FAIL(2),
		FAIL_NOT_EXIST_USER(3),
		FAIL_TITLE(4),
		FAIL_MIN_LEVEL(5),
		FAIL_CLOSED(6),
		FAIL_PASSWORD(7),
		FAIL_DISTRIBUTION_TYPE(8),
		FAIL_MAX_PLAYER(9),
		FAIL_INVALID_ROOM_STATUS(10),
		FAIL_NOT_FOUND_ROOM(11),
		FAIL_INVALID_ROOMID(12),
		FAIL_NOT_OWNER(13),
		;
		private int value;
		eResult(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eResult v){
			return value == v.value;
		}
		public static eResult fromInt(int i){
			switch(i){
			case 1:
				return SUCCESS;
			case 2:
				return FAIL;
			case 3:
				return FAIL_NOT_EXIST_USER;
			case 4:
				return FAIL_TITLE;
			case 5:
				return FAIL_MIN_LEVEL;
			case 6:
				return FAIL_CLOSED;
			case 7:
				return FAIL_PASSWORD;
			case 8:
				return FAIL_DISTRIBUTION_TYPE;
			case 9:
				return FAIL_MAX_PLAYER;
			case 10:
				return FAIL_INVALID_ROOM_STATUS;
			case 11:
				return FAIL_NOT_FOUND_ROOM;
			case 12:
				return FAIL_INVALID_ROOMID;
			case 13:
				return FAIL_NOT_OWNER;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eResult, %d", i));
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
		return S_INDUN_ROOM_CHANGE_PROPERTY;
	}
}
