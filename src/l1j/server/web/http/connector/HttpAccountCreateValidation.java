package l1j.server.web.http.connector;

/**
 * 런처 계정생성 응답
 * @author LinOffice
 */
public enum HttpAccountCreateValidation {
	SUCCESS(0, "SUCCESS"),											// 로그인 성공
	FAIL_NOT_FOUND_PARAMETERS(1, "FAIL_NOT_FOUND_PARAMETERS"),		// 웹 파라미터가 누락
	FAIL_DUPLICATE_ACCOUNT(2, "FAIL_DUPLICATE_ACCOUNT"),			// 이미 있는 계정입니다.
	FAIL_2CHECK_IP(3, "FAIL_2CHECK_IP"),							// 동일 ip로 계정 생성초과
	FAIL_HDD_BAN(4, "FAIL_HDD_BAN"),								// 해당 사용자는 이용정지 대상 입니다.
	FAIL_BOARD_BAN(5, "FAIL_BOARD_BAN"),							// 해당 사용자는 이용정지 대상 입니다.
	FAIL_IP_BAN(6, "FAIL_IP_BAN"),									// 해당 사용자는 아이피 밴 처리된 대상 입니다.
	FAIL_HMAC(7, "FAIL_HMAC"),										// mac 정보가 틀림
	FAIL_AUTH_IP(8, "FAIL_AUTH_IP"),								// VPN또는 해외아이피는 이용하실 수 없습니다.
	CLOSE(9, "CLOSE"),												// 접속기 종료.
	FAIL_ERROR(10, "FAIL_ERROR"),									// 에러 발생.
	FAIL_ACCOUNT_NAME(11, "FAIL_ACCOUNT_NAME"),						// 계정명이 올바르지 않습니다.
	FAIL_PASSWORD_NAME(12, "FAIL_PASSWORD_NAME"),					// 비밀번호가 올바르지 않습니다.
	MAC_INFO_EMPTY(13, "MAC_INFO_EMPTY"),							// 정상적인 경로가 아닙니다.
	AUTO_CREATE_ACCOUNT_FALSE(14, "AUTO_CREATE_ACCOUNT_FALSE"),		// 계정 생성 금지
	FAIL_NOT_FOUND_IP(15, "FAIL_NOT_FOUND_IP"),						// 아이피 누락
	FAIL_2CHECK_HDD(16, "FAIL_2CHECK_HDD"),							// 동일 hdd로 계정 생성 초과
	;
	private int type;
	private String message;
	HttpAccountCreateValidation(int type, String message){
		this.type = type;
		this.message = message;
	}
	
	public int getType(){
		return type;
	}
	
	public String getMessage() {
		return message;
	}
	
	public boolean equals(HttpAccountCreateValidation validation) {
		return validation.getType() == getType();
	}
}

