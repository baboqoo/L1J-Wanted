package l1j.server.common.data;

public enum eEinhasadBonusType{
	BonusNone(0),
	SectionBonus(1),
	PCCafe(2),				// 아인하사드의 가호: pc
	EinhasadFavor(3),		// 아인하사드의 가호
	EinhasadDragonFavor(4),
	;
	private int value;
	eEinhasadBonusType(int val){
		value = val;
	}
	public int toInt(){
		return value;
	}
	public boolean equals(eEinhasadBonusType v){
		return value == v.value;
	}
	public static eEinhasadBonusType fromInt(int i){
		switch(i){
		case 0:
			return BonusNone;
		case 1:
			return SectionBonus;
		case 2:
			return PCCafe;
		case 3:
			return EinhasadFavor;
		case 4:
			return EinhasadDragonFavor;
		default:
			throw new IllegalArgumentException(String.format("invalid arguments eEinhasadBonusType, %d", i));
		}
	}
}

