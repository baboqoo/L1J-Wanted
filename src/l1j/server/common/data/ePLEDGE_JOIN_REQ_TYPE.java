package l1j.server.common.data;

public enum ePLEDGE_JOIN_REQ_TYPE {
	ePLEDGE_JOIN_REQ_TYPE_IMMEDIATLY(0),
	ePLEDGE_JOIN_REQ_TYPE_CONFIRMATION(1),
	ePLEDGE_JOIN_REQ_TYPE_PASSWORD(2),
	ePLEDGE_JOIN_REQ_TYPE_IMPOSSIBLE(3),
	;
	private int value;
	ePLEDGE_JOIN_REQ_TYPE(int val){
		value = val;
	}
	public int toInt(){
		return value;
	}
	public boolean equals(ePLEDGE_JOIN_REQ_TYPE v){
		return value == v.value;
	}
	public static ePLEDGE_JOIN_REQ_TYPE fromInt(int i){
		switch(i){
		case 0:
			return ePLEDGE_JOIN_REQ_TYPE_IMMEDIATLY;
		case 1:
			return ePLEDGE_JOIN_REQ_TYPE_CONFIRMATION;
		case 2:
			return ePLEDGE_JOIN_REQ_TYPE_PASSWORD;
		case 3:
			return ePLEDGE_JOIN_REQ_TYPE_IMPOSSIBLE;
		default:
			throw new IllegalArgumentException(String.format("invalid arguments ePLEDGE_JOIN_REQ_TYPE, %d", i));
		}
	}
}

