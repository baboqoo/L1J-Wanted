package l1j.server.server.serverpackets.spell;

import l1j.server.server.serverpackets.ServerBasePacket;

public class S_SpellLateHandlingNoti extends ServerBasePacket {
	private static final String S_SPELL_LATE_HANDLING_NOTI = "[S] S_SpellLateHandlingNoti";
	private byte[] _byte = null;
	public static final int CORRECTION = 0x0410;

	public static final S_SpellLateHandlingNoti NOT_CORRECTION = new S_SpellLateHandlingNoti(true, S_SpellLateHandlingNoti.eLevel.NOT_CORRECTION);
	
	private S_SpellLateHandlingNoti(boolean on_off, S_SpellLateHandlingNoti.eLevel level) {
		write_init();
		write_on_off(on_off);
		write_correction_level(level);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(l1j.server.server.Opcodes.S_EXTENDED_PROTOBUF);
		writeH(CORRECTION);
	}
	
	void write_on_off(boolean on_off) {
		writeRaw(0x08);// on_off
		writeB(on_off);
	}
	
	void write_correction_level(eLevel correction_level) {
		writeRaw(0x10);// correction_level
		writeRaw(correction_level.value);
	}
	
	public enum eLevel{
		NOT_CORRECTION(0),
		LEVEL_1(1),
		LEVEL_2(2),
		LEVEL_3(3),
		LEVEL_4(4),
		LEVEL_5(5),
		;
		private int value;
		eLevel(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eLevel v){
			return value == v.value;
		}
		public static eLevel fromInt(int i){
			switch(i){
			case 0:
				return NOT_CORRECTION;
			case 1:
				return LEVEL_1;
			case 2:
				return LEVEL_2;
			case 3:
				return LEVEL_3;
			case 4:
				return LEVEL_4;
			case 5:
				return LEVEL_5;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eLevel, %d", i));
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
		return S_SPELL_LATE_HANDLING_NOTI;
	}

}

