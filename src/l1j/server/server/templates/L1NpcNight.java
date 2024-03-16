package l1j.server.server.templates;

public class L1NpcNight {
	private int npcId;
	private L1Npc dayTemplate;
	private int targetNpcId;
	private L1Npc nightTemplate;
	private int targetMapId;
	
	public L1NpcNight(int npcId, L1Npc dayTemplate, int targetNpcId, L1Npc nightTemplate, int targetMapId) {
		this.npcId			= npcId;
		this.dayTemplate	= dayTemplate;
		this.targetNpcId	= targetNpcId;
		this.nightTemplate	= nightTemplate;
		this.targetMapId	= targetMapId;
	}
	
	public int getNpcId() {
		return npcId;
	}
	public L1Npc getDayTemplate() {
		return dayTemplate;
	}
	public int getTargetNpcId() {
		return targetNpcId;
	}
	public L1Npc getNightTemplate() {
		return nightTemplate;
	}
	public int getTargetMapId() {
		return targetMapId;
	}
}

