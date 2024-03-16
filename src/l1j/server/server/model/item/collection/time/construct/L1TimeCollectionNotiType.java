package l1j.server.server.model.item.collection.time.construct;

/**
 * 실렉티스 전시회 상태
 * @author LinOffice
 */
public enum L1TimeCollectionNotiType {
	NEW(1),		// 시작하기
	END(2),		// 제거하기
	SOON_END(3),// 종료하기
	REFRESH(4),	// 변경하기
	;
	private int value;
	L1TimeCollectionNotiType(int value) {
		this.value = value;
	}
	public int toInt(){
		return value;
	}
	public boolean equals(L1TimeCollectionNotiType v){
		return value == v.value;
	}
	public static L1TimeCollectionNotiType fromInt(int i){
		switch(i){
		case 1:
			return NEW;
		case 2:
			return END;
		case 3:
			return SOON_END;
		case 4:
			return REFRESH;
		default:
			throw new IllegalArgumentException(String.format("invalid arguments L1TimeCollectionNotiType, %d", i));
		}
	}
}

