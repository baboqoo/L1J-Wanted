package l1j.server.common.data;

public enum Gender {
	MALE(0),	// 남성
	FEMALE(1),	// 여성
	;
	private int value;
	Gender(int val){
		value = val;
	}
	public int toInt(){
		return value;
	}
	public boolean equals(Gender v){
		return value == v.value;
	}
	public static Gender fromInt(int i){
		switch(i){
		case 0:
			return MALE;
		case 1:
			return FEMALE;
		default:
			throw new IllegalArgumentException(String.format("invalid arguments Gender, %d", i));
		}
	}
}

