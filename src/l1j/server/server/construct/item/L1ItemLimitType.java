package l1j.server.server.construct.item;

import l1j.server.server.utils.StringUtil;

public enum L1ItemLimitType {
	NONE,
	BEGIN_ZONE,
	WORLD_WAR,
	;
	public static L1ItemLimitType fromString(String str){
		if (StringUtil.isNullOrEmpty(str)) {
			return L1ItemLimitType.NONE;
		}
		switch(str){
		case "BEGIN_ZONE":	return L1ItemLimitType.BEGIN_ZONE;
		case "WORLD_WAR":	return L1ItemLimitType.WORLD_WAR;
		default:			return L1ItemLimitType.NONE;
		}
	}
}

