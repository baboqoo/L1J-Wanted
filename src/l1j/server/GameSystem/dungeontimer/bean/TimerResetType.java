package l1j.server.GameSystem.dungeontimer.bean;

public enum TimerResetType {
	NONE,
	DAY,
	WEEK,
	;
	public static TimerResetType fromString(String val) {
		switch(val){
		case "DAY":
			return DAY;
		case "WEEK":
			return WEEK;
		case "NONE":
			return WEEK;
		default:
			throw new IllegalArgumentException(String.format("invalid arguments TimerResetType, %s", val));
		}
	}
}

