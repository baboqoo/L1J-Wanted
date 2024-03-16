package l1j.server.web.http.connector;

/**
 * 런처 로그인 응답
 * @author LinOffice
 */
public enum HttpLoginValidation {
	SUCCESS(0, "SUCCESS"),										// 로그인 성공
	FAIL_NOT_FOUND_PARAMETERS(1, "FAIL_NOT_FOUND_PARAMETERS"),	// 웹 파라미터가 누락
	FAIL_NOT_FOUND_ACCOUNT(2, "FAIL_NOT_FOUND_ACCOUNT"),		// 계정 정보를 찾을 수 없음
	FAIL_INVALID_ACCOUNT(3, "FAIL_INVALID_ACCOUNT"),			// 계정 정보가 틀림
	FAIL_HDD_BAN(4, "FAIL_HDD_BAN"),							// 해당 사용자는 이용정지 대상 입니다.
	FAIL_BOARD_BAN(5, "FAIL_BOARD_BAN"),						// 해당 사용자는 이용정지 대상 입니다.
	FAIL_IP_BAN(6, "FAIL_IP_BAN"),								// 해당 사용자는 아이피 밴 처리된 대상 입니다.
	FAIL_AUTH_IP(7, "FAIL_AUTH_IP"),							// VPN또는 해외아이피는 접속하실 수 없습니다.
	FAIL_HMAC(8, "FAIL_HMAC"),									// mac 정보가 틀림
	CLOSE(9, "CLOSE"),											// 접속기 종료.
	FAIL_MERGE_ACCOUNT(10, "FAIL_MERGE_ACCOUNT"),				// 이미 접속중인 계정입니다.
	FAIL_DEVELOPMENT(11, "FAIL_DEVELOPMENT"),					// 개발 환경상태입니다.
	MAC_INFO_EMPTY(12, "MAC_INFO_EMPTY"),						// 정상적인 경로가 아닙니다.
	FAIL_NOT_FOUND_IP(13, "FAIL_NOT_FOUND_IP")					// 아이피 누락
	;
	private int type;
	private String message;
	HttpLoginValidation(int type, String message){
		this.type		= type;
		this.message	= message;
	}
	
	public int getType(){
		return type;
	}
	
	public String getMessage() {
		return message;
	}
	
	public boolean equals(HttpLoginValidation validation) {
		return validation.getType() == getType();
	}
}

