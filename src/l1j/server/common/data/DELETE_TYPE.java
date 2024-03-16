package l1j.server.common.data;

public enum DELETE_TYPE{
	DELETE_NOTHING(0),
	DELETE_COUNT(1),
	DELETE_ALL(2),
	;
	private int value;
	DELETE_TYPE(int val){
		value = val;
	}
	public int toInt(){
		return value;
	}
	public boolean equals(DELETE_TYPE v){
		return value == v.value;
	}
	public static DELETE_TYPE fromInt(int i){
		switch(i){
		case 0:
			return DELETE_NOTHING;
		case 1:
			return DELETE_COUNT;
		case 2:
			return DELETE_ALL;
		default:
			throw new IllegalArgumentException(String.format("invalid arguments DELETE_TYPE, %d", i));
		}
	}
}

