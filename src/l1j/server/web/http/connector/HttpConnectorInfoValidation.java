package l1j.server.web.http.connector;

/**
 * 런처 설정 응답
 * @author LinOffice
 */
public enum HttpConnectorInfoValidation {
	SUCCESS(0, "SUCCESS"),							// 성공
	FAIL(1, "FAIL"),								// 실패
	CLOSE(2, "CLOSE"),								// 서버 종료
	FAIL_REQUIRED_INFO(3, "FAIL_REQUIRED_INFO")		// 필수 불충분
	;
	private int type;
	private String message;
	HttpConnectorInfoValidation(int type, String message){
		this.type = type;
		this.message = message;
	}
	
	public int getType(){
		return type;
	}
	
	public String getMessage() {
		return message;
	}
	
	public boolean equals(HttpConnectorInfoValidation validation) {
		return validation.getType() == getType();
	}
}

