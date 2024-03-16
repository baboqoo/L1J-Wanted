package l1j.server.server.model.skill;

public enum L1SkillType {
	NONE("none"),
	ACTIVE("active"),
	PASSIVE("passive"),
	;
	private String _name;
	L1SkillType(String name) {
		_name = name;
	}
	public String getName() {
		return _name;
	}
	public static L1SkillType fromString(String type){
		switch(type){
		case "active":	return ACTIVE;
		case "passive":	return PASSIVE;
		default:		return NONE;
		}
	}
}

