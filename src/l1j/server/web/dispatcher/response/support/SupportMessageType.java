package l1j.server.web.dispatcher.response.support;

public enum SupportMessageType {
	AGREE,
	PROGRESS,
	REWARD,
	;
	public static SupportMessageType fromString(String val) {
		switch (val) {
		case "AGREE":
			return AGREE;
		case "PROGRESS":
			return PROGRESS;
		case "REWARD":
			return REWARD;
		default:
			throw new IllegalArgumentException(String.format("invalid arguments SupportMessageType, %s", val));	
		}
	}
}

