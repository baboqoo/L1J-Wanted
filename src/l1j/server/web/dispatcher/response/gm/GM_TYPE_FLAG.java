package l1j.server.web.dispatcher.response.gm;

public enum GM_TYPE_FLAG {
	SERVER,
	USER,
	SUPPORT,
	REPORT,
	CHAT,
	CHAT_COMMAND,
	;
	public static GM_TYPE_FLAG fromString(String val) {
		switch (val) {
		case "SERVER":
			return SERVER;
		case "USER":
			return USER;
		case "SUPPORT":
			return SUPPORT;
		case "REPORT":
			return REPORT;
		case "CHAT":
			return CHAT;
		case "CHAT_COMMAND":
			return CHAT_COMMAND;
		default:
			throw new IllegalArgumentException(String.format("invalid arguments GM_TYPE_FLAG, %s", val));
		}
	}
}

