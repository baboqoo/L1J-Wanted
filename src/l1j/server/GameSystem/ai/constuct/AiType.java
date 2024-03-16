package l1j.server.GameSystem.ai.constuct;

public enum AiType {
	AI_BATTLE,			// 거울전쟁
	COLOSEUM,			// 무한대전(현재 미사용)
	HUNT,				// 사냥(현재 미사용)
	FISHING,			// 낚시(현재 미사용)
	TOWN_MOVE,			// 마을(현재 미사용)
	SCARECROW_ATTACK,	// 허수아비 공격(현재 미사용)
	;
	
	public static AiType fromString(String val) {
		switch (val) {
		case "AI_BATTLE":
			return AI_BATTLE;
		case "COLOSEUM":
			return COLOSEUM;
		case "HUNT":
			return HUNT;
		case "FISHING":
			return FISHING;
		case "TOWN_MOVE":
			return TOWN_MOVE;
		case "SCARECROW_ATTACK":
			return SCARECROW_ATTACK;
		default:
			throw new IllegalArgumentException(String.format("invalid arguments AiType, %s", val));
		}
	}
	
	public static boolean isBrain(AiType val) {
		return val == AI_BATTLE || val == COLOSEUM || val == HUNT || val == TOWN_MOVE || val == SCARECROW_ATTACK;
	}
	
	public static boolean isScene(AiType val) {
		return val == AI_BATTLE || val == COLOSEUM;
	}
}

