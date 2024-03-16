package l1j.server.web.dispatcher.response.support;

public enum SupportStatus {
	STANBY,		// 후원 입금 완료
	FINISH,		// 후원 입금 정산
	;
	public static SupportStatus fromString(String val) {
		switch (val) {
		case "STANBY":
			return STANBY;
		case "FINISH":
			return FINISH;
		default:
			throw new IllegalArgumentException(String.format("invalid arguments SupportStatus, %s", val));
		}
	}
}

