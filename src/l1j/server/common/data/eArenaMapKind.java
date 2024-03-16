package l1j.server.common.data;

public enum eArenaMapKind{
	None(0),
	LastShowdown(1),
	ArenaMatch(11),
	WarSquare(12),
	WarSquare2(13),
	BattleHunter(14),
	ArenaMatch_LFC(15),
	WarSquare_LFC(16),
	WarSquare_LFC1(17),
	WarSquare_LFC2(18),
	WarSquare_LFC3(19),
	WarSquare_LFC4(20),
	WarSquare_LFC5(21),
	BreakingTower(22),
	BreakingTower_LFC(23),
	LastShowdown_LFC(24),
	OrimLab_Minor(201),
	OrimLab_Normal(202),
	CrocodileIsle(203),
	Island_Of_Dreams(204),
	Dimension_Intertwined(205),
	Aurakia_Purification(206),
	;
	private int value;
	eArenaMapKind(int val){
		value = val;
	}
	public int toInt(){
		return value;
	}
	public boolean equals(eArenaMapKind v){
		return value == v.value;
	}
	public static eArenaMapKind fromInt(int i){
		switch(i){
		case 0:
			return None;
		case 1:
			return LastShowdown;
		case 11:
			return ArenaMatch;
		case 12:
			return WarSquare;
		case 13:
			return WarSquare2;
		case 14:
			return BattleHunter;
		case 15:
			return ArenaMatch_LFC;
		case 16:
			return WarSquare_LFC;
		case 17:
			return WarSquare_LFC1;
		case 18:
			return WarSquare_LFC2;
		case 19:
			return WarSquare_LFC3;
		case 20:
			return WarSquare_LFC4;
		case 21:
			return WarSquare_LFC5;
		case 22:
			return BreakingTower;
		case 23:
			return BreakingTower_LFC;
		case 24:
			return LastShowdown_LFC;
		case 201:
			return OrimLab_Minor;
		case 202:
			return OrimLab_Normal;
		case 203:
			return CrocodileIsle;
		case 204:
			return Island_Of_Dreams;
		case 205:
			return Dimension_Intertwined;
		case 206:
			return Aurakia_Purification;
		default:
			throw new IllegalArgumentException(String.format("invalid arguments eArenaMapKind, %d", i));
		}
	}
}

