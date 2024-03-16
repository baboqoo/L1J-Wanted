package l1j.server.server.construct;

public enum L1Undead {
	NONE,
	UNDEAD,
	DEMON,
	UNDEAD_BOSS,
	DRANIUM,
	;
	private static final java.util.List<L1Undead> HOLY_UNDEAD = java.util.Arrays.asList(new L1Undead[] {
			UNDEAD, UNDEAD_BOSS
	});
	private static final java.util.List<L1Undead> BLESS_UNDEAD = java.util.Arrays.asList(new L1Undead[] {
			UNDEAD, DEMON, UNDEAD_BOSS
	});
	
	public static boolean isHolyUndead(L1Undead undead){
		return HOLY_UNDEAD.contains(undead);
	}
	
	public static boolean isBlessUndead(L1Undead undead){
		return BLESS_UNDEAD.contains(undead);
	}
	
	public static boolean isNotTurnUndead(L1Undead undead){
		return undead != UNDEAD;
	}
	
	public static L1Undead fromString(String val){
		switch(val){
		case "UNDEAD":
			return UNDEAD;
		case "DEMON":
			return DEMON;
		case "UNDEAD_BOSS":
			return UNDEAD_BOSS;
		case "DRANIUM":
			return DRANIUM;
		default:
			return NONE;
		}
	}
}

