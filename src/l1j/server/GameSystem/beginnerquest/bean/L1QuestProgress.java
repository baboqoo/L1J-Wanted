package l1j.server.GameSystem.beginnerquest.bean;

import java.util.HashMap;

import l1j.server.common.bin.quest.QuestT;

public class L1QuestProgress {
	private int questId;
	private long startTime;
	private long finishTime;
	private HashMap<Integer, Integer> objectives;
	private QuestT bin;
	
	public L1QuestProgress() {
	}
	
	public L1QuestProgress(int questId, QuestT bin) {
		this.questId	= questId;
		this.bin		= bin;
	}
	
	public int getQuestId() {
		return questId;
	}
	public void setQuestId(int questId) {
		this.questId = questId;
	}
	
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	
	public long getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(long finishTime) {
		this.finishTime = finishTime;
	}
	
	public HashMap<Integer, Integer> getObjectives() {
		return objectives;
	}
	public int getQuantity(int key) {
		if (objectives == null) {
			return 0;
		}
		Integer value = objectives.get(key);
		return value == null ?  0 : value;
	}
	public void setQuantity(int key, int val) {
		if (objectives == null) {
			objectives = new HashMap<Integer, Integer>();
		}
		this.objectives.put(key, val);
	}
	
	public QuestT getBin() {
		return bin;
	}
	public void setBin(QuestT bin) {
		this.bin = bin;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("QUEST_ID : ").append(questId);
		sb.append(", START_TIME : ").append(startTime);
		sb.append(", FINISH_TIME : ").append(finishTime);
		sb.append(", OBJECTIVES : ").append(objectives);
		return sb.toString();
	}
}
