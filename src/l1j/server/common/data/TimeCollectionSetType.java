package l1j.server.common.data;

public enum TimeCollectionSetType{
	TC_INFINITY(0),
	TC_LIMITED(1),
	TC_BONUS_INFINITY(2),
	TC_BONUS_LIMITED(3),
	TC_ADENA_REFILL(4),
	TC_ADENA_REFILL_ERROR(5),
	TC_BONUS_ADENA_REFILL(6),
	TC_BONUS_ADENA_REFILL_ERROR(7),
	;
	private int value;
	TimeCollectionSetType(int val){
		value = val;
	}
	public int toInt(){
		return value;
	}
	public boolean equals(TimeCollectionSetType v){
		return value == v.value;
	}
	public static TimeCollectionSetType fromInt(int i){
		switch(i){
		case 0:
			return TC_INFINITY;
		case 1:
			return TC_LIMITED;
		case 2:
			return TC_BONUS_INFINITY;
		case 3:
			return TC_BONUS_LIMITED;
		case 4:
			return TC_ADENA_REFILL;
		case 5:
			return TC_ADENA_REFILL_ERROR;
		case 6:
			return TC_BONUS_ADENA_REFILL;
		case 7:
			return TC_BONUS_ADENA_REFILL_ERROR;
		default:
			throw new IllegalArgumentException(String.format("invalid arguments TimeCollectionSetType, %d", i));
		}
	}
}

