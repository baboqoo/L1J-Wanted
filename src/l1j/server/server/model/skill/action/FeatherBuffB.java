package l1j.server.server.model.skill.action;

import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_SPMR;

public class FeatherBuffB extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		cha.getAbility().addShortHitup(2);
		cha.getAbility().addSp(1);
		cha.addMaxHp(50);
		cha.addMaxMp(30);
		cha.getAbility().addDamageReduction(2);
		return 0;
	}

	@Override
	public void stop(L1Character cha) {
		cha.getAbility().addShortHitup(-2);
		cha.getAbility().addSp(-1);
		cha.addMaxHp(-50);
		cha.addMaxMp(-30);
		cha.getAbility().addDamageReduction(-2);
	}
	
	@Override
	public void icon(L1PcInstance pc, int time) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void wrap(L1PcInstance pc, boolean flag) {
		pc.sendPackets(new S_OwnCharStatus(pc), true);
		pc.sendPackets(new S_SPMR(pc), true);
		super.wrap(pc, flag);
	}
	
	@Override
	public L1SkillActionHandler copyInstance() {
		return new FeatherBuffB().setValue(_skillId, _skill);
	}

}

