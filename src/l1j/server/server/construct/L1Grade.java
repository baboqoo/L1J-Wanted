package l1j.server.server.construct;

public enum L1Grade {
	NORMAL,	// 일반
	ADVANC,	// 고급
	RARE,	// 희귀
	HERO,	// 영웅
	LEGEND,	// 전설
	MYTH,	// 신화
	ONLY,	// 유일
	;
	public static L1Grade fromString(String str){
		switch(str){
		case "ADVANC":	return ADVANC;
		case "RARE":	return RARE;
		case "HERO":	return HERO;
		case "LEGEND":	return LEGEND;
		case "MYTH":	return MYTH;
		case "ONLY":	return ONLY;
		default:		return NORMAL;
		}
	}
}

