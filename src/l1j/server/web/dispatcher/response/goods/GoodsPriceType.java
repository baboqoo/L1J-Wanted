package l1j.server.web.dispatcher.response.goods;

public enum GoodsPriceType {
	NCOIN,
	NPOINT,
	;
	public static GoodsPriceType fromString(String str) {
		switch (str) {
		case "NCOIN":
			return NCOIN;
		case "NPOINT":
			return NPOINT;
		default:
			throw new IllegalArgumentException(String.format("invalid arguments GoodsPriceType, %s", str));
		}
	}
}

