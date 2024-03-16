package l1j.server.server.serverpackets.indun;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_IndunEnterRoom extends ServerBasePacket {
	private byte[] _byte = null;
	private static final String S_INDUN_ENTER_ROOM = "[S] S_IndunEnterRoom";
	public static final int ENTER_INDUN_ROOM = 0x08ad;

	public S_IndunEnterRoom(ArenaEnterResult result, int room_id) {
		write_init();
		write_result(result);
		write_room_id(room_id);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(ENTER_INDUN_ROOM);
	}
	
	void write_result(ArenaEnterResult result) {
		writeC(0x08);// result
		writeC(result.value);
	}
	
	void write_room_id(int room_id) {
		writeC(0x10);// room_id
		writeBit(room_id);
	}
 	
 	public enum ArenaEnterResult{
		SUCCESS(1),						// 입장
		FAIL(2),						// 방입장에실패하였습니다
		FAIL_TO_ALREADY_PLAY(3),		// 이미게임이시작되어실패
		FAIL_NOT_ENOUGH_MONEY(4),		// 아데나부족
		FAIL_INVALID_PASSWORD(5),		// 비번입력
		FAIL_INVALID_ROOM_STATUS(6),	// 입장조건에만족하지못하였거나없는상태
		FAIL_NOT_FOUND_ROOM(7),			// 선택한방을찾을수없습니다
		FAIL_FULL(8),					// 입장인원이가득찼습니다
		FAIL_ALREADY_ENTERED_PLAYER(9),	// 이미플레이어로입장한상태
		FAIL_OTHER_ROOM_ENTERED(10),	// 다른방에입장한상태라불가
		FAIL_LEVEL(11),					// 레벨이맞지않아불가
		FAIL_INVALID_SERVER(12),		// 지금은입장불가
		FAIL_ENTER_LIMIT(13),			// 플레이가능한횟수초과
		FAIL_INDUN_BLOCK(14),
		FAIL_NOT_ENOUGH_KEY(15),
		FAIL_NOT_OPEN_TIME(16),
		FAIL_RESTRICT_ITEM(17),
		;
		private int value;
		ArenaEnterResult(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(ArenaEnterResult v){
			return value == v.value;
		}
		public static ArenaEnterResult fromInt(int i){
			switch(i){
			case 1:
				return SUCCESS;
			case 2:
				return FAIL;
			case 3:
				return FAIL_TO_ALREADY_PLAY;
			case 4:
				return FAIL_NOT_ENOUGH_MONEY;
			case 5:
				return FAIL_INVALID_PASSWORD;
			case 6:
				return FAIL_INVALID_ROOM_STATUS;
			case 7:
				return FAIL_NOT_FOUND_ROOM;
			case 8:
				return FAIL_FULL;
			case 9:
				return FAIL_ALREADY_ENTERED_PLAYER;
			case 10:
				return FAIL_OTHER_ROOM_ENTERED;
			case 11:
				return FAIL_LEVEL;
			case 12:
				return FAIL_INVALID_SERVER;
			case 13:
				return FAIL_ENTER_LIMIT;
			case 14:
				return FAIL_INDUN_BLOCK;
			case 15:
				return FAIL_NOT_ENOUGH_KEY;
			case 16:
				return FAIL_NOT_OPEN_TIME;
			case 17:
				return FAIL_RESTRICT_ITEM;
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
		return S_INDUN_ENTER_ROOM;
	}
}
