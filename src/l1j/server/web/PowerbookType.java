package l1j.server.web;

public enum PowerbookType {
	ALL(0),
	ITEM(1),
	NPC(2),
	SKILL(3),
	GUIDE(4),
	;
	private int _value;
	PowerbookType(int value) {
		_value = value;
	}
	public int toInt() {
		return _value;
	}
	public static PowerbookType fromInt(int val) {
		switch (val) {
		case 0:
			return ALL;
		case 1:
			return ITEM;
		case 2:
			return NPC;
		case 3:
			return SKILL;
		case 4:
			return GUIDE;
		default:
			throw new IllegalArgumentException(String.format("invalid arguments PowerbookType, %d", val));
		}
	}
	public static PowerbookType fromString(String val) {
		switch (val) {
		case "0":
			return ALL;
		case "1":
			return ITEM;
		case "2":
			return NPC;
		case "3":
			return SKILL;
		case "4":
			return GUIDE;
		default:
			throw new IllegalArgumentException(String.format("invalid arguments PowerbookType, %s", val));
		}
	}
}

