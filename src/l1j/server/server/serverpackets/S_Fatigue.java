package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_Fatigue extends ServerBasePacket {
	private static final String S_FATIGUE= "[S] S_Fatigue";
	private byte[] _byte = null;
	public static final int FATIGUE	= 0x014e;
	
	public static final S_Fatigue[] LEVELS	= {
		new S_Fatigue(0, 0),
		new S_Fatigue(1, 0),
		new S_Fatigue(2, 0),
		new S_Fatigue(3, 0),
		new S_Fatigue(4, 0),
		new S_Fatigue(5, 0),
		new S_Fatigue(6, 0)
	};

	public S_Fatigue(int penaltyLevel, int combatFatigue){
		write_init();
		write_status(FATIGUE_STATUS.FATIGUE_STATUS_OK);
		write_penaltyLevel(penaltyLevel);
		write_combatFatigue(combatFatigue);
        writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(FATIGUE);
	}
	
	void write_status(FATIGUE_STATUS status) {
		writeRaw(0x08);
		writeRaw(status.value);
	}
	
	void write_penaltyLevel(int penaltyLevel) {
		writeRaw(0x10);
		writeRaw(penaltyLevel);
	}
	
	void write_combatFatigue(int combatFatigue) {
		writeRaw(0x18);
		writeBit(combatFatigue);
	}
	
	public enum FATIGUE_STATUS{
		FATIGUE_STATUS_OK(1),
		FATIGUE_STATUS_FAIL(2),
		FATIGUE_STATUS_NOT_USE(3);
		private int value;
		FATIGUE_STATUS(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(FATIGUE_STATUS v){
			return value == v.value;
		}
		public static FATIGUE_STATUS fromInt(int i){
			switch(i){
			case 1:
				return FATIGUE_STATUS_OK;
			case 2:
				return FATIGUE_STATUS_FAIL;
			case 3:
				return FATIGUE_STATUS_NOT_USE;
			default:
				return null;
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
		return S_FATIGUE;
	}
}

