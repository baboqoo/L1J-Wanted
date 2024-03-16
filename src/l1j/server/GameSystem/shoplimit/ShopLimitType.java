package l1j.server.GameSystem.shoplimit;

public enum ShopLimitType {
	ACCOUNT,
	CHARACTER,
	;
	public static ShopLimitType fromString(String val) {
		switch (val) {
		case "ACCOUNT":
			return ACCOUNT;
		case "CHARACTER":
			return CHARACTER;
		default:
			throw new IllegalArgumentException(String.format("invalid arguments ShopLimitType, %d", val));
		}
	}
	
}

