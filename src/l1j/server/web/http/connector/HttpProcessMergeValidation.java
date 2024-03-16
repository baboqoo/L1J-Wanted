package l1j.server.web.http.connector;

/**
 * 런처 유저 프로세스 갱신 응답
 * @author LinOffice
 */
public enum HttpProcessMergeValidation {
	SUCCESS(0, "SUCCESS"),										// 갱신 성공
	FAIL_NOT_FOUND_PARAMETERS(1, "FAIL_NOT_FOUND_PARAMETERS"),	// 웹 파라미터가 누락
	FAIL_NOT_FOUND_CLIENT(2, "FAIL_NOT_FOUND_ACCOUNT"),			// 클라이언트를 찾을 수 없습니다.
	FAIL(3, "FAIL"),											// 실패
	CLOSE(4, "CLOSE")											// 서버 종료.
	;
	private int type;
	private String message;
	HttpProcessMergeValidation(int type, String message){
		this.type		= type;
		this.message	= message;
	}
	
	public int getType(){
		return type;
	}
	
	public String getMessage() {
		return message;
	}
	
	public boolean equals(HttpProcessMergeValidation validation) {
		return validation.getType() == getType();
	}
}

