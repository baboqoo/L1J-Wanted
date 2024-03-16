package l1j.server.common.data;

public enum eMonsterBookV2RewardType{
	BaseReward(1),
	ExtraReward(2),
	;
	private int value;
	eMonsterBookV2RewardType(int val){
		value = val;
	}
	public int toInt(){
		return value;
	}
	public boolean equals(eMonsterBookV2RewardType v){
		return value == v.value;
	}
	public static eMonsterBookV2RewardType fromInt(int i){
		switch(i){
		case 1:
			return BaseReward;
		case 2:
			return ExtraReward;
		default:
			throw new IllegalArgumentException(String.format("invalid arguments eMonsterBookV2RewardType, %d", i));
		}
	}
}

