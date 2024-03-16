package l1j.server.GameSystem.beginnerquest.bean;

public class L1QuestCollectItem {
	private int itemNameId;
	private int questId;
	private int index;
	private int requiredQuantity;
	
	public L1QuestCollectItem(int itemNameId, int questId, int index, int requiredQuantity) {
		this.itemNameId			= itemNameId;
		this.questId			= questId;
		this.index				= index;
		this.requiredQuantity	= requiredQuantity;
	}
	
	public int getItemNameId() {
		return itemNameId;
	}
	public int getQuestId() {
		return questId;
	}
	public int getIndex() {
		return index;
	}
	public int getRequiredQuantity() {
		return requiredQuantity;
	}
}

