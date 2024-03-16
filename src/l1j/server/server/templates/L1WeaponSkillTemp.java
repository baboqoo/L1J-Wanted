package l1j.server.server.templates;

import l1j.server.server.model.L1WeaponSkill;

public class L1WeaponSkillTemp {
	private L1WeaponSkill skillPVP, skillPVE;
	
	public L1WeaponSkill getSkillPVP() {
		return skillPVP;
	}
	public void setSkillPVP(L1WeaponSkill skillPVP) {
		this.skillPVP = skillPVP;
	}
	public L1WeaponSkill getSkillPVE() {
		return skillPVE;
	}
	public void setSkillPVE(L1WeaponSkill skillPVE) {
		this.skillPVE = skillPVE;
	}
}

