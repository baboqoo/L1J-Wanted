package l1j.server.server.serverpackets.revenge;

public enum eRevengeResult{
	SUCCESS(0),					// 성공
	FAIL_USER(1),				// 찾기 실패
	FAIL_TIME(2),				// 시간 실패
	FAIL_COST(3),				// 비용 실패
	FAIL_LIST(4),				// 리스트 실패
	FAIL_ACTION(5),				// 액션 실패
	FAIL_COUNT(6),				// 카운트 실패
	FAIL_SERVER(7),				// 서버 실패
	FAIL_UPDATE_PERIOD(8),		// 업데이트 실패
	FAIL_ALREADY_PURSUITING(9),	// 이미 시작 실패
	FAIL_OTHER(10),				// 사용자 실패
	;
	private int value;
	eRevengeResult(int val){
		value = val;
	}
	public int toInt(){
		return value;
	}
	public boolean equals(eRevengeResult v){
		return value == v.value;
	}
	public static eRevengeResult fromInt(int i){
		switch(i){
		case 0:
			return SUCCESS;
		case 1:
			return FAIL_USER;
		case 2:
			return FAIL_TIME;
		case 3:
			return FAIL_COST;
		case 4:
			return FAIL_LIST;
		case 5:
			return FAIL_ACTION;
		case 6:
			return FAIL_COUNT;
		case 7:
			return FAIL_SERVER;
		case 8:
			return FAIL_UPDATE_PERIOD;
		case 9:
			return FAIL_ALREADY_PURSUITING;
		case 10:
			return FAIL_OTHER;
		default:
			throw new IllegalArgumentException(String.format("invalid arguments eRevengeResult, %d", i));
		}
	}
}

