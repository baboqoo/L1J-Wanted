package l1j.server.GameSystem.shoplimit;

public enum ShopLimitTerm {
	DAY,
	WEEK,
	NONE,
	;
	public static ShopLimitTerm fromString(String val) {
		switch (val) {
		case "DAY":
			return DAY;
		case "WEEK":
			return WEEK;
		case "NONE":
			return NONE;
		default:
			throw new IllegalArgumentException(String.format("invalid arguments ShopLimitTerm, %d", val));
		}
	}
}

