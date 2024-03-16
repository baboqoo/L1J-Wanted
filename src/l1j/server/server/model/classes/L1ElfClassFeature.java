package l1j.server.server.model.classes;

class L1ElfClassFeature extends L1ClassFeature {
	//private static final String CLASS_DESC = "요정";
	private static final String CLASS_DESC = "Elf";
	private static final String CLASS_FLAG = "E";

	@Override
	public int getMagicLevel(int playerLevel) {
		return Math.min(6, playerLevel >> 3);
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
