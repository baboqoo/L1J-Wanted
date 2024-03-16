package l1j.server.common.data;

public enum eMonsterBookV2RewardGrade{
	RG_NORMAL(1),
	RG_DRAGON(2),
	RG_HIGH_DRAGON(3),
	;
	private int value;
	eMonsterBookV2RewardGrade(int val){
		value = val;
	}
	public int toInt(){
		return value;
	}
	public boolean equals(eMonsterBookV2RewardGrade v){
		return value == v.value;
	}
	public static eMonsterBookV2RewardGrade fromInt(int i){
		switch(i){
		case 1:
			return RG_NORMAL;
		case 2:
			return RG_DRAGON;
		case 3:
			return RG_HIGH_DRAGON;
		default:
			throw new IllegalArgumentException(String.format("invalid arguments eMonsterBookV2RewardGrade, %d", i));
		}
	}
}

