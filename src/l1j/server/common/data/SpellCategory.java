package l1j.server.common.data;

public enum SpellCategory{
	SPELL(0),
	SPELL_BUFF(1),
	COMPANION_SPELL_BUFF(2),
	;
	private int value;
	SpellCategory(int val){
		value = val;
	}
	public int toInt(){
		return value;
	}
	public boolean equals(SpellCategory v){
		return value == v.value;
	}
	public static SpellCategory fromInt(int i){
		switch(i){
		case 0:
			return SPELL;
		case 1:
			return SPELL_BUFF;
		case 2:
			return COMPANION_SPELL_BUFF;
		default:
			throw new IllegalArgumentException(String.format("invalid arguments SpellCategory, %d", i));
		}
	}
}

