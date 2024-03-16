package l1j.server.server.serverpackets.indun;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_IndunExitRoom extends ServerBasePacket {
	private byte[] _byte = null;
	private static final String S_INDUN_EXIT_ROOM = "[S] S_IndunExitRoom";
	public static final int EXIT = 0x08b8;
	
	public S_IndunExitRoom(ArenaRoomExitResult result, int room_id) {
		write_init();
		write_result(result);
		write_room_id(room_id);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(EXIT);
	}
	
	void write_result(ArenaRoomExitResult result) {
		writeC(0x08);// result
		writeC(result.value);
	}
	
	void write_room_id(int room_id) {
		writeC(0x10);// room_id
		writeBit(room_id);
	}
 	
 	public enum ArenaRoomExitResult{
		SUCCESS(1),
		FAIL(2),
		;
		private int value;
		ArenaRoomExitResult(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(ArenaRoomExitResult v){
			return value == v.value;
		}
		public static ArenaRoomExitResult fromInt(int i){
			switch(i){
			case 1:
				return SUCCESS;
			case 2:
				return FAIL;
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
		return S_INDUN_EXIT_ROOM;
	}
}
