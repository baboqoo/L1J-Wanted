package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Resistance;

public class S_SpecialResistance extends ServerBasePacket {
	private static final String S_SPECIAL_RESISTANCE = "[S] S_SpecialResistance";
	private byte[] _byte = null;
	public static final int ABLITY		= 0x03f7;
	
	public static final int RESISTANCE	= 0x0a;// 내성
	public static final int PIERCE		= 0x12;// 적중
	
	public S_SpecialResistance(L1Resistance resistance) {
		write_init();
		for (int i=1; i<=2; i++) {
			int type = i == 1 ? RESISTANCE : PIERCE;
			for (ResistanceKind kind : ResistanceKind.ARRAY) {
				int value = getValue(resistance, type, kind);
				write_type(type, 3 + getBitSize(value));
				write_kind(kind);
				write_value(value);
			}
		}
		writeH(0x00);
	}
	
	private int getValue(L1Resistance resistance, int type, ResistanceKind kind){
		switch(kind){
		case NONE:			return 0;
		case ABILITY:		return type == PIERCE ? resistance.getHitupSkill() : resistance.getToleranceSkill();
		case SPIRIT:		return type == PIERCE ? resistance.getHitupSpirit() : resistance.getToleranceSpirit();
		case DRAGON_SPELL:	return type == PIERCE ? resistance.getHitupDragon() : resistance.getToleranceDragon();
		case FEAR:			return type == PIERCE ? resistance.getHitupFear() : resistance.getToleranceFear();
		case ALL:			return 0;
		default:			return 0;
		}
	}
	
	public S_SpecialResistance(int type, ResistanceKind kind, int value) {
		write_init();
		write_type(type, 3 + getBitSize(value));
		write_kind(kind);
		write_value(value);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(ABLITY);
	}
	
	void write_type(int type, int length) {
		writeRaw(type);// 0x12:적중, 0x0a:내성
		writeRaw(length);
	}
	
	void write_kind(ResistanceKind kind) {
		writeRaw(0x08);
		writeRaw(kind.value);
	}
	
	void write_value(int value) {
		writeRaw(0x10);
		writeBit(value);
	}
	
	public enum ResistanceKind{
		NONE(0),
		ABILITY(1),
		SPIRIT(2),
		DRAGON_SPELL(3),
		FEAR(4),
		ALL(5),
		;
		private int value;
		ResistanceKind(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(ResistanceKind v){
			return value == v.value;
		}
		public static ResistanceKind fromInt(int i){
			switch(i){
			case 0:
				return NONE;
			case 1:
				return ABILITY;
			case 2:
				return SPIRIT;
			case 3:
				return DRAGON_SPELL;
			case 4:
				return FEAR;
			case 5:
				return ALL;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments ResistanceKind, %d", i));
			}
		}
		public static final ResistanceKind[] ARRAY = ResistanceKind.values();
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
		return S_SPECIAL_RESISTANCE;
	}
}

