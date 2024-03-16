package l1j.server.server.construct;

import l1j.server.server.utils.StringUtil;

public enum L1Alignment {
	LAWFUL(1),
	NEUTRAL(0),
	CAOTIC(-1),
	;
	private int _value;         
	L1Alignment(int val) {
		_value = val;
	}
	public int toInt() {
		return _value;
	}
	public static L1Alignment fromString(String str) {
		if (StringUtil.isNullOrEmpty(str)) {
			return null;
		}
		switch (str) {
		case "LAWFUL":
			return LAWFUL;
		case "NEUTRAL":
			return NEUTRAL;
		case "CAOTIC":
			return CAOTIC;
		default:
			return null;
		}
	}
}

