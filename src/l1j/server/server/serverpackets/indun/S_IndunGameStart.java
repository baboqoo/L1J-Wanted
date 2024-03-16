package l1j.server.server.serverpackets.indun;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_IndunGameStart extends ServerBasePacket {
	private byte[] _byte = null;
	private static final String S_INDUN_GAME_START = "[S] S_IndunGameStart";
	public static final int GAME_START 	= 0x08b2;
	
	public S_IndunGameStart(ArenaGameStartResult result, int room_id) {
		write_init();
		write_result(result);
		write_room_id(room_id);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(GAME_START);
	}
	
	void write_result(ArenaGameStartResult result) {
		writeC(0x08);// result
		writeC(result.value);
	}
	
	void write_room_id(int room_id) {
		writeC(0x10);// room_id
		writeBit(room_id);
	}
 	
 	public enum ArenaGameStartResult{
		SUCCESS(1),				// 시작
		FAIL(2),				// 게임시작이 되지 않앗습니다
		FAIL_INVALID_USER(3),	// 시작할캐릭터가없습니다
		FAIL_INVALID_ROOM(4),	// 잘못된방번호라게임을시작하수없습니다
		FAIL_INVALID_OWNER(5),	// 방장이아니라서시작할수없습니다
		FAIL_INVALID_STATE(6),	// 참가자가부족하여시작할수없습니다
		FAIL_ISSUE_PLAY(7),		// unknown error startgame
		FAIL_NOT_ENOUGH_KEY(8),	// 지금은이용할수없습니다
		FAIL_LEVEL(9),
		FAIL_ENTER_LIMIT(10),
		FAIL_INDUN_BLOCK(11),
		FAIL_NOT_OPEN_TIME(12),
		FAIL_RESTRICT_ITEM(13),
		;
		private int value;
		ArenaGameStartResult(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(ArenaGameStartResult v){
			return value == v.value;
		}
		public static ArenaGameStartResult fromInt(int i){
			switch(i){
			case 1:
				return SUCCESS;
			case 2:
				return FAIL;
			case 3:
				return FAIL_INVALID_USER;
			case 4:
				return FAIL_INVALID_ROOM;
			case 5:
				return FAIL_INVALID_OWNER;
			case 6:
				return FAIL_INVALID_STATE;
			case 7:
				return FAIL_ISSUE_PLAY;
			case 8:
				return FAIL_NOT_ENOUGH_KEY;
			case 9:
				return FAIL_LEVEL;
			case 10:
				return FAIL_ENTER_LIMIT;
			case 11:
				return FAIL_INDUN_BLOCK;
			case 12:
				return FAIL_NOT_OPEN_TIME;
			case 13:
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
		return S_INDUN_GAME_START;
	}
}
