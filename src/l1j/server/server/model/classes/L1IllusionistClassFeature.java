package l1j.server.server.model.classes;

class L1IllusionistClassFeature extends L1ClassFeature {
	//private static final String CLASS_DESC = "환술사";
	private static final String CLASS_DESC = "Illusionist";
	private static final String CLASS_FLAG = "B";

	@Override
	public int getMagicLevel(int playerLevel) {
		return Math.min(10, playerLevel / 6);
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
