package l1j.server.server.model.skill.action;

import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.serverpackets.S_OwnCharStatus;

public class BuffBalracas extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		option(cha, true);
		return 0;
	}

	@Override
	public void stop(L1Character cha) {
		option(cha, false);
	}
	
	void option(L1Character cha, boolean flag) {
		cha.getAbility().addShortHitup(flag ? 5 : -5);
		cha.getAbility().addShortDmgup(flag ? 1 : -1);
		cha.getAbility().addLongHitup(flag ? 5 : -5);
		cha.getAbility().addLongDmgup(flag ? 1 : -1);
		cha.addMaxHp(flag ? 100 : -100);
		cha.addMaxMp(flag ? 50 : -50);
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.addHpRegen(flag ? 3 : -3);
			pc.addMpRegen(flag ? 3 : -3);
		}
	}
	
	@Override
	public void icon(L1PcInstance pc, int time) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void wrap(L1PcInstance pc, boolean flag) {
		pc.sendPackets(new S_OwnCharStatus(pc), true);
		super.wrap(pc, flag);
	}
	
	@Override
	public L1SkillActionHandler copyInstance() {
		return new BuffBalracas().setValue(_skillId, _skill);
	}

}

