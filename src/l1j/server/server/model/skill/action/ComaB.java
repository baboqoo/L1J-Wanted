package l1j.server.server.model.skill.action;

import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_OwnCharStatus2;

public class ComaB extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		cha.getAbility().addSp(1);
		cha.getAbility().addAddedCon(3);
		cha.getAbility().addAddedDex(5);
		cha.getAbility().addAddedStr(5);
		cha.getAbility().addShortHitup(5);
		cha.getAC().addAc(-8);
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.add_exp_boosting_ratio(20);
		}
		return 0;
	}

	@Override
	public void stop(L1Character cha) {
		cha.getAbility().addSp(-1);
		cha.getAbility().addAddedCon(-3);
		cha.getAbility().addAddedDex(-5);
		cha.getAbility().addAddedStr(-5);
		cha.getAbility().addShortHitup(-5);
		cha.getAC().addAc(8);
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.add_exp_boosting_ratio(-20);
		}
	}
	
	@Override
	public void icon(L1PcInstance pc, int time) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void wrap(L1PcInstance pc, boolean flag) {
		pc.sendPackets(new S_OwnCharStatus(pc), true);
		pc.sendPackets(new S_OwnCharStatus2(pc), true);
		super.wrap(pc, flag);
	}
	
	@Override
	public L1SkillActionHandler copyInstance() {
		return new ComaB().setValue(_skillId, _skill);
	}

}

