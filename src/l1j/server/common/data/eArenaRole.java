package l1j.server.common.data;

public enum eArenaRole{
	Player(1),
	Observer(2),
	BuilderCaster(3),
	;
	private int value;
	eArenaRole(int val){
		value = val;
	}
	public int toInt(){
		return value;
	}
	public boolean equals(eArenaRole v){
		return value == v.value;
	}
	public static eArenaRole fromInt(int i){
		switch(i){
		case 1:
			return Player;
		case 2:
			return Observer;
		case 3:
			return BuilderCaster;
		default:
			throw new IllegalArgumentException(String.format("invalid arguments eArenaRole, %d", i));
		}
	}
}

