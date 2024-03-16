package l1j.server.common.data;

public enum eBloodPledgeRankType{
	RANK_NORMAL_KING(10,			"Monarch"),
	RANK_NORMAL_PRINCE(14,			"Vice-Monarch"),
	RANK_NORMAL_KNIGHT(9,			"Guardian"),
	RANK_NORMAL_ELITE_KNIGHT(13,	"Elite"),
	RANK_NORMAL_JUNIOR_KNIGHT(8,	"Common"),
	RANK_NORMAL_REGULAR(7,			"Training"),
	;
	private int value;
	private String desc;
	eBloodPledgeRankType(int val, String desc){
		this.value	= val;
		this.desc	= desc;
	}
	public int toInt(){
		return value;
	}
	public String toDesc() {
		return desc;
	}
	public boolean equals(eBloodPledgeRankType v){
		return value == v.value;
	}
	public static eBloodPledgeRankType fromInt(int i){
		switch(i){
		case 10:
			return RANK_NORMAL_KING;
		case 14:
			return RANK_NORMAL_PRINCE;
		case 9:
			return RANK_NORMAL_KNIGHT;
		case 13:
			return RANK_NORMAL_ELITE_KNIGHT;
		case 8:
			return RANK_NORMAL_JUNIOR_KNIGHT;
		case 7:
			return RANK_NORMAL_REGULAR;
		default:
			throw new IllegalArgumentException(String.format("invalid arguments eBloodPledgeRankType, %d", i));
		}
	}
	public static eBloodPledgeRankType fromString(String val){
		switch(val){
		case "RANK_NORMAL_KING(Monarch)":
			return RANK_NORMAL_KING;
		case "RANK_NORMAL_PRINCE(Vice-Monarch)":
			return RANK_NORMAL_PRINCE;
		case "RANK_NORMAL_KNIGHT(Guardian)":
			return RANK_NORMAL_KNIGHT;
		case "RANK_NORMAL_ELITE_KNIGHT(Elite)":
			return RANK_NORMAL_ELITE_KNIGHT;
		case "RANK_NORMAL_JUNIOR_KNIGHT(Common)":
			return RANK_NORMAL_JUNIOR_KNIGHT;
		case "RANK_NORMAL_REGULAR(Training)":
			return RANK_NORMAL_REGULAR;
		default:
			return null;
		}
	}
	
	/**
	 * 승인 랭크(군주, 부군주)
	 * @param val
	 * @return
	 */
	public static boolean isAuthRankAtPrince(eBloodPledgeRankType val) {
		return val == RANK_NORMAL_KING || val == RANK_NORMAL_PRINCE;
	}
	
	/**
	 * 승인 랭크(군주, 부군주, 수호)
	 * @param val
	 * @return boolean
	 */
	public static boolean isAuthRankAtKnight(eBloodPledgeRankType val) {
		return val == RANK_NORMAL_KING || val == RANK_NORMAL_PRINCE || val == RANK_NORMAL_KNIGHT;
	}
	
	/**
	 * 승인 랭크(군주, 부군주, 수호, 정예)
	 * @param val
	 * @return boolean
	 */
	public static boolean isAuthRankAtEliteKnight(eBloodPledgeRankType val) {
		return val == RANK_NORMAL_KING || val == RANK_NORMAL_PRINCE || val == RANK_NORMAL_KNIGHT || val == RANK_NORMAL_ELITE_KNIGHT;
	}
	
}

