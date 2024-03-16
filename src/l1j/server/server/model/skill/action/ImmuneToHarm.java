package l1j.server.server.model.skill.action;

import l1j.server.server.construct.skill.L1PassiveId;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.L1PinkName;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;

public class ImmuneToHarm extends L1SkillActionHandler {
	
	double get_damage_reduction_rate(int owner_level) {
		if (owner_level >= 95) {
			return 0.4D;
		}
		if (owner_level >= 93) {
			return 0.35D;
		}
		if (owner_level >= 91) {
			return 0.3D;
		}
		if (owner_level >= 89) {
			return 0.25D;
		}
		if (owner_level >= 87) {
			return 0.2D;
		}
		return 0.15D;
	}

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		if (attacker == null) {
			return 0;
		}
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			L1PinkName.onHelp(cha, attacker);
			if (attacker.getId() == pc.getId()) {// 시전자
				pc.immunToHarmValue = 0.5D;
				if (pc.isPassiveStatus(L1PassiveId.IMMUNE_T0_HARM_SAINT)) {
					pc._isImmunToHarmSaint = true;
				}
			} else {
				pc.immunToHarmValue = get_damage_reduction_rate(attacker.getLevel());
				pc._isImmunToHarmSaint = false;
			}
			icon(pc, time);
		}
		return 0;
	}

	@Override
	public void stop(L1Character cha) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.immunToHarmValue = 0;
			pc._isImmunToHarmSaint = false;
		}
	}
	
	@Override
	public void icon(L1PcInstance pc, int time) {
		pc.sendPackets(new S_SpellBuffNoti(pc, _skillId, true, time), true);
	}
	
	@Override
	public void wrap(L1PcInstance pc, boolean flag) {
		super.wrap(pc, flag);
	}
	
	@Override
	public L1SkillActionHandler copyInstance() {
		return new ImmuneToHarm().setValue(_skillId, _skill);
	}

}

