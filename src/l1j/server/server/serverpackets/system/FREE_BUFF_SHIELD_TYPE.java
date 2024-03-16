package l1j.server.server.serverpackets.system;

public enum FREE_BUFF_SHIELD_TYPE{
	PC_CAFE_SHIELD(0),
	EVENT_BUFF_SHIELD(1),
	FREE_BUFF_SHIELD(2),
	;
	private int value;
	FREE_BUFF_SHIELD_TYPE(int val){
		value = val;
	}
	public int toInt(){
		return value;
	}
	public boolean equals(FREE_BUFF_SHIELD_TYPE v){
		return value == v.value;
	}
	public static FREE_BUFF_SHIELD_TYPE fromInt(int i){
		switch(i){
		case 0:
			return PC_CAFE_SHIELD;
		case 1:
			return EVENT_BUFF_SHIELD;
		case 2:
			return FREE_BUFF_SHIELD;
		default:
			throw new IllegalArgumentException(String.format("invalid arguments FREE_BUFF_SHIELD_TYPE, %d", i));
		}
	}
}

