package l1j.server.server.model.classes;

class L1WizardClassFeature extends L1ClassFeature {
	//private static final String CLASS_DESC = "마법사";
	private static final String CLASS_DESC = "Wizard";
	private static final String CLASS_FLAG = "W";

	@Override
	public int getMagicLevel(int playerLevel) {
		return Math.min(13, playerLevel >> 2);
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
