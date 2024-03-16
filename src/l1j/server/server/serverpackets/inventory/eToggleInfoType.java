package l1j.server.server.serverpackets.inventory;

public enum eToggleInfoType {
	TOGGLE_INFO_NONE_TYPE(0),
	TOGGLE_INFO_FAITH_OF_HALPAH_USABLE_TYPE(1),
	;
	private int value;
	eToggleInfoType(int val){
		value = val;
	}
	public int toInt(){
		return value;
	}
	public boolean equals(eToggleInfoType v){
		return value == v.value;
	}
	public static eToggleInfoType fromInt(int i){
		switch(i){
		case 0:
			return TOGGLE_INFO_NONE_TYPE;
		case 1:
			return TOGGLE_INFO_FAITH_OF_HALPAH_USABLE_TYPE;
		default:
			throw new IllegalArgumentException(String.format("invalid arguments eToggleInfoType, %d", i));
		}
	}
}

