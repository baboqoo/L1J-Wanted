package l1j.server.GameSystem.beginnerquest.bean;

public class L1QuestDropItem {
	private int npcId;
	private int mainQuestId;
	private int mainItemNameId;
	private int subQuestId;
	private int subItemNameId;
	
	public L1QuestDropItem(int npcId, int mainQuestId, int mainItemNameId, int subQuestId, int subItemNameId) {
		this.npcId			= npcId;
		this.mainQuestId	= mainQuestId;
		this.mainItemNameId	= mainItemNameId;
		this.subQuestId		= subQuestId;
		this.subItemNameId	= subItemNameId;
	}
	
	public int getNpcId() {
		return npcId;
	}
	public int getMainQuestId() {
		return mainQuestId;
	}
	public int getMainItemNameId() {
		return mainItemNameId;
	}
	public int getSubQuestId() {
		return subQuestId;
	}
	public int getSubItemNameId() {
		return subItemNameId;
	}
}

