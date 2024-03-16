package l1j.server.server.model.classes;

public class L1LancerClassFeature extends L1ClassFeature {
	//private static final String CLASS_DESC = "창기사";
	private static final String CLASS_DESC = "Lancer";
	private static final String CLASS_FLAG = "L";

	@Override
	public int getMagicLevel(int playerLevel) {
		return playerLevel / 50;
	}
	
	@Override
	public String getClassDesc() {
		return CLASS_DESC;
	}
	
	@Override
	public String getClassFlag() {
		return CLASS_FLAG;
	}
	
}
