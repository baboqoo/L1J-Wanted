package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_SiegeObjectPutNoti extends ServerBasePacket {
	private static final String S_SIEGE_OBJECT_PUT_NOTI = "[S] S_SiegeObjectPutNoti";
	private byte[] _byte = null;
	public static final int NOTI = 0x0041;
	
	public S_SiegeObjectPutNoti(int IdOfObject, SIEGE_BLOOD_KIND bloodKind) {
		write_init();
		write_IdOfObject(IdOfObject);
		write_bloodKind(bloodKind);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_IdOfObject(int IdOfObject) {
		writeRaw(0x08);
		writeBit(IdOfObject);
	}
	
	void write_bloodKind(SIEGE_BLOOD_KIND bloodKind) {
		writeRaw(0x10);
		writeRaw(bloodKind.value);
	}
	
	public enum SIEGE_BLOOD_KIND{
		SIEGE_RED_BLOOD(1),
		SIEGE_BLACK_BLOOD(2),
		SIEGE_RED_INIT(3),
		;
		private int value;
		SIEGE_BLOOD_KIND(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(SIEGE_BLOOD_KIND v){
			return value == v.value;
		}
		public static SIEGE_BLOOD_KIND fromInt(int i){
			switch(i){
			case 1:
				return SIEGE_RED_BLOOD;
			case 2:
				return SIEGE_BLACK_BLOOD;
			case 3:
				return SIEGE_RED_INIT;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments SIEGE_BLOOD_KIND, %d", i));
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
		return S_SIEGE_OBJECT_PUT_NOTI;
	}
}

