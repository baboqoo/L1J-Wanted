package l1j.server.GameSystem.ai.bean;

import l1j.server.server.templates.L1Skills;

/**
 * AI 버프 오브젝트
 * @author LinOffice
 */
public class AiBuffObject {
	private int type, elfAttr;
	private L1Skills[] buffs;
	
	public AiBuffObject(int type, int elfAttr, L1Skills[] buffs) {
		this.type		= type;
		this.elfAttr	= elfAttr;
		this.buffs		= buffs;
	}
	
	public int getType() {
		return type;
	}
	public int getElfAttr() {
		return elfAttr;
	}
	public L1Skills[] getBuffs() {
		return buffs;
	}
	public void release(){
		buffs = null;
	}
}

