package l1j.server.server.construct.item;

import l1j.server.server.utils.StringUtil;

public enum L1ItemSpellBookAttr {
	FIRE(1),
	WATER(2),
	AIR(3),
	EARTH(4),
	;
	private int _value;
	L1ItemSpellBookAttr(int val) {
		_value = val;
	}
	public int toInt() {
		return _value;
	}
	public static L1ItemSpellBookAttr fromString(String str) {
		if (StringUtil.isNullOrEmpty(str)) {
			return null;
		}
		switch (str) {
		case "FIRE":
			return FIRE;
		case "WATER":
			return WATER;
		case "AIR":
			return AIR;
		case "EARTH":
			return EARTH;
		default:
			return null;
		}
	}
}

