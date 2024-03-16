package l1j.server.server.serverpackets.indun;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_IndunChangeReadyEnter extends ServerBasePacket {
	private byte[] _byte = null;
	private static final String S_INDUN_CHNAGE_READY_ENTER = "[S] S_IndunChangeReadyEnter";
	public static final int CHANGE_READY_ENTER 	= 0x08b0;

	public S_IndunChangeReadyEnter(ArenaReadyResult result, int room_id, boolean ready) {
		write_init();
		write_result(result); 
		write_room_id(room_id);
		write_ready(ready);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(CHANGE_READY_ENTER);
	}
	
	void write_result(ArenaReadyResult result) {
		writeRaw(0x08);// result
		writeRaw(result.value); 
	}
	
	void write_room_id(int room_id) {
		writeRaw(0x10);// room_id
		writeBit(room_id);
	}
	
	void write_ready(boolean ready) {
		writeRaw(0x18);// ready
		writeB(ready);
	}
 	
 	public enum ArenaReadyResult{
		SUCCESS(1),
		FAIL(2),
		FAIL_NOT_ENOUGH_MONEY(3),
		FAIL_LEVEL(4),
		;
		private int value;
		ArenaReadyResult(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(ArenaReadyResult v){
			return value == v.value;
		}
		public static ArenaReadyResult fromInt(int i){
			switch(i){
			case 1:
				return SUCCESS;
			case 2:
				return FAIL;
			case 3:
				return FAIL_NOT_ENOUGH_MONEY;
			case 4:
				return FAIL_LEVEL;
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
		return S_INDUN_CHNAGE_READY_ENTER;
	}
}
