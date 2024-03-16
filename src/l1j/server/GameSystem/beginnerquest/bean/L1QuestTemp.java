package l1j.server.GameSystem.beginnerquest.bean;

import l1j.server.common.bin.quest.QuestT;

public class L1QuestTemp {
	private int questId;
	private String desc;
	private boolean use;
	private boolean autoComplete;
	private int fastLevel;
	private QuestT bin;
	
	public L1QuestTemp(int questId, String desc, boolean use, boolean autoComplete, int fastLevel, QuestT bin) {
		this.questId		= questId;
		this.desc			= desc;
		this.use			= use;
		this.autoComplete	= autoComplete;
		this.fastLevel		= fastLevel;
		this.bin			= bin;
	}
	
	public int getQuestId() {
		return questId;
	}
	public String getDesc() {
		return desc;
	}
	public boolean isUse() {
		return use;
	}
	public boolean isAutoComplete() {
		return autoComplete;
	}
	public int getFastLevel() {
		return fastLevel;
	}
	public QuestT getBin() {
		return bin;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("QUEST_ID : ").append(questId);
		sb.append(", DESC : ").append(desc);
		sb.append(", USE : ").append(use);
		return sb.toString();
	}
}

