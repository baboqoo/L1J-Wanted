package l1j.server.server.model.classes;

class L1KnightClassFeature extends L1ClassFeature {
	//private static final String CLASS_DESC = "기사";
	private static final String CLASS_DESC = "Knight";
	private static final String CLASS_FLAG = "K";

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
