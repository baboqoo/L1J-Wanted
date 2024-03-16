package l1j.server.server.model.classes;

class L1DarkElfClassFeature extends L1ClassFeature {
	//private static final String CLASS_DESC = "다크엘프";
	private static final String CLASS_DESC = "Darkelf";
	private static final String CLASS_FLAG = "D";

	@Override
	public int getMagicLevel(int playerLevel) {
		return Math.min(2, playerLevel / 12);
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

