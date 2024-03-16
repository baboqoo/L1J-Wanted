package l1j.server.common.data;

public enum eArenaTeam{
	NEUTRAL_PASSIVE(-2),
	NEUTRAL_HOSTILE(-1),
	TEAM_NONE(0),
	TEAM_A(1),
	TEAM_B(2),
	TEAM_C(3),
	;
	private int value;
	eArenaTeam(int val){
		value = val;
	}
	public int toInt(){
		return value;
	}
	public boolean equals(eArenaTeam v){
		return value == v.value;
	}
	public static eArenaTeam fromInt(int i){
		switch(i){
		case -2:
			return NEUTRAL_PASSIVE;
		case -1:
			return NEUTRAL_HOSTILE;
		case 0:
			return TEAM_NONE;
		case 1:
			return TEAM_A;
		case 2:
			return TEAM_B;
		case 3:
			return TEAM_C;
		default:
			throw new IllegalArgumentException(String.format("invalid arguments eArenaTeam, %d", i));
		}
	}
}

