package l1j.server.common.data;

public enum eMonsterBookV2RewardConnective{
	And(1),
	Or(2),
	;
	private int value;
	eMonsterBookV2RewardConnective(int val){
		value = val;
	}
	public int toInt(){
		return value;
	}
	public boolean equals(eMonsterBookV2RewardConnective v){
		return value == v.value;
	}
	public static eMonsterBookV2RewardConnective fromInt(int i){
		switch(i){
		case 1:
			return And;
		case 2:
			return Or;
		default:
			throw new IllegalArgumentException(String.format("invalid arguments eMonsterBookV2RewardConnective, %d", i));
		}
	}
}

