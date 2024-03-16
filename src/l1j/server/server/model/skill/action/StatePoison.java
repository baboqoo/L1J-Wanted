package l1j.server.server.model.skill.action;

import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.poison.L1DamagePoison;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.utils.CommonUtil;

public class StatePoison extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (pc.isDead() || pc.isGhost() || pc.isBind() || pc.isAbsol()) {
				return 0;
			}
			int poisonTime = CommonUtil.random(5) + 1;
			if (poisonTime > 2) {
				L1DamagePoison.doInfection(attacker, pc, poisonTime * 1000, 30, false);
			}
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
		return new StatePoison().setValue(_skillId, _skill);
	}

}

