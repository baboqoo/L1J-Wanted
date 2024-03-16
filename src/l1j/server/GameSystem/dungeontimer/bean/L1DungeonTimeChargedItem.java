package l1j.server.GameSystem.dungeontimer.bean;

public class L1DungeonTimeChargedItem {
	private int itemId;
	private int timerId;
	private int groupId;
	public L1DungeonTimeChargedItem(int itemId, int timerId, int groupId) {
		this.itemId = itemId;
		this.timerId = timerId;
		this.groupId = groupId;
	}
	public int getItemId() {
		return itemId;
	}
	public int getTimerId() {
		return timerId;
	}
	public int getGroupId() {
		return groupId;
	}
}

