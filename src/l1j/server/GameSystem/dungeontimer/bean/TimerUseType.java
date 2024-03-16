package l1j.server.GameSystem.dungeontimer.bean;

public enum TimerUseType {
	ACCOUNT,
	CHARACTER,
	;
	public static TimerUseType fromString(String val) {
		switch(val){
		case "ACCOUNT":
			return ACCOUNT;
		case "CHARACTER":
			return CHARACTER;
		default:
			throw new IllegalArgumentException(String.format("invalid arguments TimerUseType, %s", val));
		}
	}
}

