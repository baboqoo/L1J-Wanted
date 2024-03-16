package l1j.server.server.construct;

public enum L1Status {
	STR("str"),
	DEX("dex"),
	CON("con"),
	INT("int"),
	WIS("wis"),
	CHA("cha"),
	;
	private String _name;
	L1Status(String name) {
		_name	= name;
	}
	public String getName(){
		return _name;
	}
}

