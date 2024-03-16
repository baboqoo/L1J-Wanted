package l1j.server.common.data;

public enum eNotiAnimationType{
	NO_ANIMATION(0),
	ANT_QUEEN(1),
	OMAN_MORPH(2),
	AI_BATTLE(3),
	;
	private int value;
	eNotiAnimationType(int val){
		value = val;
	}
	public int toInt(){
		return value;
	}
	public boolean equals(eNotiAnimationType v){
		return value == v.value;
	}
	public static eNotiAnimationType fromInt(int i){
		switch(i){
		case 0:
			return NO_ANIMATION;
		case 1:
			return ANT_QUEEN;
		case 2:
			return OMAN_MORPH;
		case 3:
			return AI_BATTLE;
		default:
			throw new IllegalArgumentException(String.format("invalid arguments eNotiAnimationType, %d", i));
		}
	}
}

