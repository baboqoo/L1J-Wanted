package l1j.server.GameSystem.ai.bean;

import l1j.server.server.templates.L1PassiveSkills;
import l1j.server.server.templates.L1Skills;

/**
 * AI 스킬 오브젝트
 * @author LinOffice
 */
public class AiSkillObject {
	private int type;
	private L1Skills[] active;
	private L1PassiveSkills[] passive;
	
	public AiSkillObject(int type, L1Skills[] active, L1PassiveSkills[] passive) {
		this.type		= type;
		this.active		= active;
		this.passive	= passive;
	}
	
	public int getType() {
		return type;
	}
	public L1Skills[] getActive() {
		return active;
	}
	public L1PassiveSkills[] getPassive() {
		return passive;
	}
	public void release(){
		active	= null;
		passive	= null;
	}
}

