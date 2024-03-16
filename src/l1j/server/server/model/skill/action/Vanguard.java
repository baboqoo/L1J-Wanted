package l1j.server.server.model.skill.action;

import l1j.server.Config;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;

public class Vanguard extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			pc._vanguardFlag = pc._isLancerForm ? 1 : 2;
			switch (pc._vanguardFlag) {
			case 1:
				pc.addAttackSpeedDelayRate(Config.SPELL.VANGUARD_ATTACK_SPEED_RATE);
				break;
			case 2:
				pc.addSpeedDelayRate(Config.SPELL.VANGUARD_MOVE_SPEED_RATE, Config.SPELL.VANGUARD_ATTACK_SPEED_RATE);
				break;
			}
			icon(pc, time);
		}
		return 0;
	}

	@Override
	public void stop(L1Character cha) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			switch (pc._vanguardFlag) {
			case 1:
				pc.addAttackSpeedDelayRate(-Config.SPELL.VANGUARD_ATTACK_SPEED_RATE);
				break;
			case 2:
				pc.addSpeedDelayRate(-Config.SPELL.VANGUARD_MOVE_SPEED_RATE, -Config.SPELL.VANGUARD_ATTACK_SPEED_RATE);
				break;
			}
			pc.sendPackets(new S_SpellBuffNoti(pc, _skillId, false, -1), true);
			pc._vanguardFlag = 0;
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
		return new Vanguard().setValue(_skillId, _skill);
	}

}

