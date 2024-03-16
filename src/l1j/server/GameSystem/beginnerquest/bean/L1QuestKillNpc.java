package l1j.server.GameSystem.beginnerquest.bean;

public class L1QuestKillNpc {
	private int classId;
	private int questId;
	private int index;
	private int requiredQuantity;
	
	public L1QuestKillNpc(int classId, int questId, int index, int requiredQuantity) {
		this.classId			= classId;
		this.questId			= questId;
		this.index				= index;
		this.requiredQuantity	= requiredQuantity;
	}
	
	public int getClassId() {
		return classId;
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

