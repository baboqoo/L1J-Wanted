package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_Tutorial extends ServerBasePacket {
	private static final String S_TUTORIAL = "[S] S_Tutorial";
	private byte[] _byte = null;
	
	public S_Tutorial(int code, NOTI_TYPE type){
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(code);
		
		writeC(0x08);// type
		writeC(type.value);
		
        writeH(0x00);
	}
	
	public S_Tutorial(int code, TUTORIAL_TYPE type, TUTORIAL_RESULT result){
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(code);
		
		writeC(0x08);// type
		writeC(type.value);
		
		writeC(0x10);// result
		writeC(result.value);

        writeH(0x00);
	}
	
	public enum NOTI_TYPE {
		NOTI_TYPE_CHECK_BEGINNER_TUTORIAL(0),
		;
		private int value;
		NOTI_TYPE(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(NOTI_TYPE v){
			return value == v.value;
		}
		public static NOTI_TYPE fromInt(int i){
			switch(i){
			case 0:
				return NOTI_TYPE_CHECK_BEGINNER_TUTORIAL;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments NOTI_TYPE, %d", i));
			}
		}
	}
	
	public enum TUTORIAL_TYPE{
		TUTORIAL_TYPE_NONE(0),
		TUTORIAL_TYPE_BEGINNER(1),
		;
		private int value;
		TUTORIAL_TYPE(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(TUTORIAL_TYPE v){
			return value == v.value;
		}
		public static TUTORIAL_TYPE fromInt(int i){
			switch(i){
			case 0:
				return TUTORIAL_TYPE_NONE;
			case 1:
				return TUTORIAL_TYPE_BEGINNER;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments TUTORIAL_TYPE, %d", i));
			}
		}
	}
	
	public enum TUTORIAL_RESULT{
		TUTORIAL_RESULT_SUCCESS(0),
		TUTORIAL_RESULT_FAIL(1),
		TUTORIAL_RESULT_INVALID_TYPE(2),
		TUTORIAL_RESULT_ALREADY_PLAYING(3),
		TUTORIAL_RESULT_CANT_FIND_TUTORIAL(4),
		;
		private int value;
		TUTORIAL_RESULT(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(TUTORIAL_RESULT v){
			return value == v.value;
		}
		public static TUTORIAL_RESULT fromInt(int i){
			switch(i){
			case 0:
				return TUTORIAL_RESULT_SUCCESS;
			case 1:
				return TUTORIAL_RESULT_FAIL;
			case 2:
				return TUTORIAL_RESULT_INVALID_TYPE;
			case 3:
				return TUTORIAL_RESULT_ALREADY_PLAYING;
			case 4:
				return TUTORIAL_RESULT_CANT_FIND_TUTORIAL;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments TUTORIAL_RESULT, %d", i));
			}
		}
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = _bao.toByteArray();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_TUTORIAL;
	}
}

