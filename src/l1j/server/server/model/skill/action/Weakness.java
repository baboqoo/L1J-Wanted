package l1j.server.server.model.skill.action;

import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;

public class Weakness extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		ablity(cha, true);
		return 0;
	}

	@Override
	public void stop(L1Character cha) {
		ablity(cha, false);
	}
	
	@Override
	public void icon(L1PcInstance pc, int time) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void wrap(L1PcInstance pc, boolean flag) {
		super.wrap(pc, flag);
	}
	
	private void ablity(L1Character cha, boolean flag){
		cha.getAbility().addShortDmgup(flag ? -5 : 5);
		cha.getAbility().addShortHitup(flag ? -1 : 1);
	}
	
	@Override
	public L1SkillActionHandler copyInstance() {
		return new Weakness().setValue(_skillId, _skill);
	}

}

