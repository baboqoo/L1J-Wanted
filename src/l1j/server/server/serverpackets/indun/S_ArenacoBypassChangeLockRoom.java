package l1j.server.server.serverpackets.indun;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_ArenacoBypassChangeLockRoom extends ServerBasePacket {
	private byte[] _byte = null;
	private static final String S_ARENACO_BYPASS_CHANGE_LOCK_ROOM = "[S] S_ArenacoBypassChangeLockRoom";
	public static final int LOCK = 0x02f0;
	
	public S_ArenacoBypassChangeLockRoom(S_ArenacoBypassChangeLockRoom.eResult result, int room_id, boolean is_lock) {
		write_init();
		write_result(result);
		write_room_id(room_id);
		write_is_lock(is_lock);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(LOCK);
	}
	
	void write_result(S_ArenacoBypassChangeLockRoom.eResult result) {
		writeRaw(0x08);// result
		writeRaw(result.value);
	}
	
	void write_room_id(int room_id) {
		writeRaw(0x10);// room_id
		writeBit(room_id);
	}
	
	void write_is_lock(boolean is_lock) {
		writeRaw(0x18);// is_lock
		writeB(is_lock);
	}
	
	public enum eResult{
		SUCCESS(1),
		FAIL(2),
		FAIL_NOT_OWNER(3),
		FAIL_INVALID_ROOM_STATUS(4),
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
				return FAIL_NOT_OWNER;
			case 4:
				return FAIL_INVALID_ROOM_STATUS;
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
		return S_ARENACO_BYPASS_CHANGE_LOCK_ROOM;
	}
}
