package l1j.server.server.model.skill.action;

import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;

public class Disease extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		ablity(cha, true);
		return 0;
	}

	@Override
	public void stop(L1Character cha) {
		ablity(cha, false);
	}
	
	void ablity(L1Character cha, boolean flag) {
		cha.getAC().addAc(flag ? 12 : -12);
		cha.getAbility().addShortDmgup(flag ? -6 : 6);
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
		return new Disease().setValue(_skillId, _skill);
	}

}

