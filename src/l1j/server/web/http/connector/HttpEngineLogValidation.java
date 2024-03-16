package l1j.server.web.http.connector;

/**
 * 런처 불허가 엔진 사용 로그 응답
 * @author LinOffice
 */
public enum HttpEngineLogValidation {
	SUCCESS(0, "SUCCESS"),							// 성공
	FAIL(1, "FAIL"),								// 실패
	CLOSE(2, "CLOSE")								// 서버 종료
	;
	private int type;
	private String message;
	HttpEngineLogValidation(int type, String message){
		this.type = type;
		this.message = message;
	}
	
	public int getType(){
		return type;
	}
	
	public String getMessage() {
		return message;
	}
	
	public boolean equals(HttpEngineLogValidation validation) {
		return validation.getType() == getType();
	}
}

