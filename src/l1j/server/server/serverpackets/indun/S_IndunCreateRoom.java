package l1j.server.server.serverpackets.indun;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_IndunCreateRoom extends ServerBasePacket {
	private byte[] _byte = null;
	private static final String S_INDUN_CREATE_ROOM = "[S] S_IndunCreateRoom";
	public static final int CREATE = 0x08a8;

	public static final S_IndunCreateRoom INDUN_CREATE_FAIL	= new S_IndunCreateRoom(ArenaRoomCreateResult.ERROR_INTERNAL, 0);// 인던방 생성 불가
	public static final S_IndunCreateRoom INDUN_ROOM_MAX	= new S_IndunCreateRoom(ArenaRoomCreateResult.ERROR_EXCEED_MAX_ROOM_COUNT, 0);// 인던방 초과
	public static final S_IndunCreateRoom INDUN_NAME_FAIL	= new S_IndunCreateRoom(ArenaRoomCreateResult.ERROR_INVALID_TITLE, 0);// 인던방 이름 길이 초과
	public static final S_IndunCreateRoom INDUN_KEY_EMPTY	= new S_IndunCreateRoom(ArenaRoomCreateResult.ERROR_NOT_ENOUGH_KEY, 0);// 인던 열쇠 없음
	public static final S_IndunCreateRoom INDUN_ADENA_FAIL	= new S_IndunCreateRoom(ArenaRoomCreateResult.ERROR_INVALID_FEE, 0);// 인던 아데나 설정체크
	public static final S_IndunCreateRoom INDUN_PLAY_MAX	= new S_IndunCreateRoom(ArenaRoomCreateResult.ERROR_ENTER_LIMIT, 0);// 인던 이용횟수 초과
	
	public S_IndunCreateRoom(ArenaRoomCreateResult result, int room_id) {
		write_init();
		write_result(result);
		write_room_id(room_id);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(CREATE);
	}
	
	void write_result(ArenaRoomCreateResult result) {
		writeC(0x08);// result
		writeC(result.value);
	}
	
	void write_room_id(int room_id) {
		writeC(0x10);// room_id
		writeBit(room_id);
	}
 	
 	public enum ArenaRoomCreateResult{
		SUCCESS(1),						// 입장
		ERROR_INTERNAL(2),				// 지금은인던방을생서할수없습니다
		ERROR_NOT_EXIST_USER(3),
		ERROR_EXCEED_MAX_ROOM_COUNT(4),	// 현재 인던방생성초과
		ERROR_EXCEED_MAX_ARENA_COUNT(5),
		ERROR_CANNOT_CREATE_ARENA(6),
		ERROR_INVALID_TITLE(7),			// 방제목이머누김니다
		ERROR_PASSWORD(8),
		ERROR_TEAM_COUNT(9),
		ERROR_TEAM_MEMBER_COUNT(10),
		ERROR_ALLOCATE_ARENA(11),
		ERROR_JOIN_ROOM(12),
		ERROR_OTHER_ROOM_ENTERED(13),
		ERROR_LEVEL(14),
		ERROR_NOT_ENOUGH_KEY(15),		// 열쇠없음
		ERROR_INVALID_FEE(16),			// 참가비입령내용이잘못
		ERROR_ENTER_LIMIT(17),			// 플레이가능횟수초과
		ERROR_INDUN_BLOCK(18),
		ERROR_NOT_OPEN_TIME(19),
		ERROR_RESTRICT_ITEM(20),
		;
		private int value;
		ArenaRoomCreateResult(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(ArenaRoomCreateResult v){
			return value == v.value;
		}
		public static ArenaRoomCreateResult fromInt(int i){
			switch(i){
			case 1:
				return SUCCESS;
			case 2:
				return ERROR_INTERNAL;
			case 3:
				return ERROR_NOT_EXIST_USER;
			case 4:
				return ERROR_EXCEED_MAX_ROOM_COUNT;
			case 5:
				return ERROR_EXCEED_MAX_ARENA_COUNT;
			case 6:
				return ERROR_CANNOT_CREATE_ARENA;
			case 7:
				return ERROR_INVALID_TITLE;
			case 8:
				return ERROR_PASSWORD;
			case 9:
				return ERROR_TEAM_COUNT;
			case 10:
				return ERROR_TEAM_MEMBER_COUNT;
			case 11:
				return ERROR_ALLOCATE_ARENA;
			case 12:
				return ERROR_JOIN_ROOM;
			case 13:
				return ERROR_OTHER_ROOM_ENTERED;
			case 14:
				return ERROR_LEVEL;
			case 15:
				return ERROR_NOT_ENOUGH_KEY;
			case 16:
				return ERROR_INVALID_FEE;
			case 17:
				return ERROR_ENTER_LIMIT;
			case 18:
				return ERROR_INDUN_BLOCK;
			case 19:
				return ERROR_NOT_OPEN_TIME;
			case 20:
				return ERROR_RESTRICT_ITEM;
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
		return S_INDUN_CREATE_ROOM;
	}
}
