package l1j.server.common.data;

public enum CharacterClass{
	PRINCE(0),
	KNIGHT(1),
	ELF(2),
	MAGICIAN(3),
	DARKELF(4),
	DRAGON_KNIGHT(5),
	ILLUSIONIST(6),
	WARRIOR(7),
	FENCER(8),
	LANCER(9),
	CLASS_ALL(511),
	;
	private int value;
	CharacterClass(int val){
		value = val;
	}
	public int toInt(){
		return value;
	}
	public boolean equals(CharacterClass v){
		return value == v.value;
	}
	public static CharacterClass fromInt(int i){
		switch(i){
		case 0:
			return PRINCE;
		case 1:
			return KNIGHT;
		case 2:
			return ELF;
		case 3:
			return MAGICIAN;
		case 4:
			return DARKELF;
		case 5:
			return DRAGON_KNIGHT;
		case 6:
			return ILLUSIONIST;
		case 7:
			return WARRIOR;
		case 8:
			return FENCER;
		case 9:
			return LANCER;
		case 511:
			return CLASS_ALL;
		default:
			throw new IllegalArgumentException(String.format("invalid arguments CharacterClass, %d", i));
		}
	}
}

