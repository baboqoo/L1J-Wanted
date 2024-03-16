package l1j.server.server.model.skill.action;

import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.L1PinkName;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;

public class RemoveCurse extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		if (cha instanceof L1PcInstance) {
			L1PinkName.onHelp(cha, attacker);
		}
		cha.curePoison();
		if (cha.getSkill().hasSkillEffect(STATUS_CURSE_PARALYZING) || cha.getSkill().hasSkillEffect(STATUS_CURSE_PARALYZED)
				|| cha.getSkill().hasSkillEffect(ANTA_MESSAGE_1) || cha.getSkill().hasSkillEffect(ANTA_MESSAGE_6) || cha.getSkill().hasSkillEffect(ANTA_MESSAGE_7)
				|| cha.getSkill().hasSkillEffect(ANTA_MESSAGE_8)) {
			cha.cureParalaysis();
		}
		if (cha.getSkill().hasSkillEffect(CURSE_BLIND)) {
			cha.getSkill().removeSkillEffect(CURSE_BLIND);
		}
		if (cha.getSkill().hasSkillEffect(DARKNESS)) {
			cha.getSkill().removeSkillEffect(DARKNESS);
		}
		return 0;
	}

	@Override
	public void stop(L1Character cha) {
		// TODO Auto-generated method stub

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
		return new RemoveCurse().setValue(_skillId, _skill);
	}

}

