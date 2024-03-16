package l1j.server.server.model.skill.action;

import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.serverpackets.S_CurseBlind;

public class Darkness extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			boolean is_floating_eye = pc.getSkill().hasSkillEffect(STATUS_FLOATING_EYE);
			pc.sendPackets(is_floating_eye ?  S_CurseBlind.BLIND_FLOATING_EYE : S_CurseBlind.BLIND_ON);
			if (is_floating_eye && !pc.isBlind()) {
				pc.showFloatingEyeToInvis();
			}
		}
		return 0;
	}

	@Override
	public void stop(L1Character cha) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.sendPackets(S_CurseBlind.BLIND_OFF);
			boolean is_floating_eye = pc.getSkill().hasSkillEffect(STATUS_FLOATING_EYE);
			if (is_floating_eye && !pc.isBlind()) {
				pc.hideBlindToInvis();
			}
		}
	}
	
	@Override
	public void icon(L1PcInstance pc, int time) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void wrap(L1PcInstance pc, boolean flag) {
		super.wrap(pc, flag);
	}
	
	@Override
	public L1SkillActionHandler copyInstance() {
		return new Darkness().setValue(_skillId, _skill);
	}

}

