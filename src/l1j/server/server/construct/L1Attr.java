package l1j.server.server.construct;

public enum L1Attr {
	/*NONE(0, 	""),
	EARTH(1, 	"땅"),
	FIRE(2,		"불"),
	WATER(4,	"물"),
	WIND(8,		"바람"),
	RAY(16,		"");*/
	NONE(0, 	""),
	EARTH(1, 	"EARTH"),
	FIRE(2,		"FIRE"),
	WATER(4,	"WATER"),
	WIND(8,		"WIND"),
	RAY(16,		"");	

	private int value;
	private String desc;
	L1Attr(int val, String desc) {
		this.value	= val;
		this.desc	= desc;
	}
	public int toInt(){
		return value;
	}
	public String getDesc() {
		return desc;
	}
	public static L1Attr fromInt(int i) {
		switch(i) {
		case 0:
			return NONE;
		case 1:
			return EARTH;
		case 2:
			return FIRE;
		case 4:
			return WATER;
		case 8:
			return WIND;
		case 16:
			return RAY;
		default:
			throw new IllegalArgumentException(String.format("invalid arguments L1Attr, %d", i));
		}
	}
	// get the attribute from enum values NONE, EARTH, FIRE, ETC.
	public static L1Attr fromString(String i) {
		switch(i) {
		case "NONE":
			return NONE;
		case "EARTH":
			return EARTH;
		case "FIRE":
			return FIRE;
		case "WATER":
			return WATER;
		case "WIND":
			return WIND;
		case "RAY":
			return RAY;
		default:
			throw new IllegalArgumentException(String.format("invalid arguments L1Attr, %s", i));
		}
	}
}

