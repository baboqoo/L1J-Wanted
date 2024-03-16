package l1j.server.common.data;

public enum ePolymorphAnonymityType {
	eNone(0),
	eNormal(1),
	eRandom(2),
	eSpecialChar(3),
	eRandomExceptOurTeam(4),
	eRandomIncludetombstone(5),
	eUnknownUser(6),
	eUnknownUserForLocal(7),
	;
	private int value;
	ePolymorphAnonymityType(int val){
		value = val;
	}
	public int toInt(){
		return value;
	}
	public boolean equals(ePolymorphAnonymityType v){
		return value == v.value;
	}
	public static ePolymorphAnonymityType fromInt(int i){
		switch(i){
		case 0:
			return eNone;
		case 1:
			return eNormal;
		case 2:
			return eRandom;
		case 3:
			return eSpecialChar;
		case 4:
			return eRandomExceptOurTeam;
		case 5:
			return eRandomIncludetombstone;
		case 6:
			return eUnknownUser;
		case 7:
			return eUnknownUserForLocal;
		default:
			throw new IllegalArgumentException(String.format("invalid arguments ePolymorphAnonymityType, %d", i));
		}
	}
}

