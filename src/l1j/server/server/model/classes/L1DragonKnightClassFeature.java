package l1j.server.server.model.classes;

class L1DragonKnightClassFeature extends L1ClassFeature {
	//private static final String CLASS_DESC = "용기사";
	private static final String CLASS_DESC = "Dragonknight";
	private static final String CLASS_FLAG = "T";

	@Override
	public int getMagicLevel(int playerLevel) {
		return Math.min(4, playerLevel / 9);
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

