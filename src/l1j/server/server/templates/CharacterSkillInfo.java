package l1j.server.server.templates;

public class CharacterSkillInfo {
	private int icon;
	private String name;
	
	public CharacterSkillInfo(int icon, String name) {
		this.icon = icon;
		this.name = name;
	}
	
	public int getIcon() {
		return icon;
	}
	public String getName() {
		return name;
	}
}

