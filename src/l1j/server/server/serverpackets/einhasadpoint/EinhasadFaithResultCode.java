package l1j.server.server.serverpackets.einhasadpoint;

public enum EinhasadFaithResultCode{
	EINHASAD_FAITH_SUCCESS(1),
	EINHASAD_FAITH_FAIL_NEED_REFRESH(2),
	EINHASAD_FAITH_FAIL_WRONG_REQUEST(3),
	EINHASAD_FAITH_FAIL_IS_NOT_GAMESERVER(4),
	;
	private int value;
	EinhasadFaithResultCode(int val){
		value = val;
	}
	public int toInt(){
		return value;
	}
	public boolean equals(EinhasadFaithResultCode v){
		return value == v.value;
	}
	public static EinhasadFaithResultCode fromInt(int i){
		switch(i){
		case 1:
			return EINHASAD_FAITH_SUCCESS;
		case 2:
			return EINHASAD_FAITH_FAIL_NEED_REFRESH;
		case 3:
			return EINHASAD_FAITH_FAIL_WRONG_REQUEST;
		case 4:
			return EINHASAD_FAITH_FAIL_IS_NOT_GAMESERVER;
		default:
			throw new IllegalArgumentException(String.format("invalid arguments EinhasadFaithResultCode, %d", i));
		}
	}
}


