package l1j.server.server.serverpackets.inventory;

public enum TIME_COLLECTION_STATE{
	TIME_COLLECTION_NONE(0),
	TIME_COLLECTION_COMPLETE(1),
	TIME_COLLECTION_BUFF_END(2),
	TIME_COLLECTION_ADDITIONAL(3),
	TIME_COLLECTION_ADDITIONAL_BUFF_END(4),
	TIME_COLLECTION_SOON_END(6),
	TIME_COLLECTION_SOON_ADDITIONAL_BUFF_END(7),
	TIME_COLLECTION_END(100),
	;
	private int value;
	TIME_COLLECTION_STATE(int val){
		value = val;
	}
	public int toInt(){
		return value;
	}
	public boolean equals(TIME_COLLECTION_STATE v){
		return value == v.value;
	}
	public static TIME_COLLECTION_STATE fromInt(int i){
		switch(i){
		case 0:
			return TIME_COLLECTION_NONE;
		case 1:
			return TIME_COLLECTION_COMPLETE;
		case 2:
			return TIME_COLLECTION_BUFF_END;
		case 3:
			return TIME_COLLECTION_ADDITIONAL;
		case 4:
			return TIME_COLLECTION_ADDITIONAL_BUFF_END;
		case 6:
			return TIME_COLLECTION_SOON_END;
		case 7:
			return TIME_COLLECTION_SOON_ADDITIONAL_BUFF_END;
		case 100:
			return TIME_COLLECTION_END;
		default:
			throw new IllegalArgumentException(String.format("invalid arguments TIME_COLLECTION_STATE, %d", i));
		}
	}
}

