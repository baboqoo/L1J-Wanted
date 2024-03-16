package l1j.server.server.model.classes;

class L1RoyalClassFeature extends L1ClassFeature {
	//private static final String CLASS_DESC = "군주";
	private static final String CLASS_DESC = "Crown";
	private static final String CLASS_FLAG = "P";

	@Override
	public int getMagicLevel(int playerLevel) {
		return Math.min(2, playerLevel / 10);
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

