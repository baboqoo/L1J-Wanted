package l1j.server.web.http;

public enum CookieSameSiteType {
	STRICT(	"Strict"),
	LAX(	"Lax"),
	;
	private String desc;
	CookieSameSiteType(String desc) {
		this.desc = desc;
	}
	public String toDesc() {
		return desc;
	}
	public static CookieSameSiteType fromString(String val) {
		switch (val) {
		case "STRICT":
			return STRICT;
		case "LAX":
			return LAX;
		default:
			throw new IllegalArgumentException(String.format("invalid arguments CookieSameSiteType, %s", val));
		}
	}
}

