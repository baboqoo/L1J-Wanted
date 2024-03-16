package l1j.server.server.model.skill.action;

import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.serverpackets.S_OwnCharStatus;

public class Avenger extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		int dmg = 0;
		if (cha instanceof L1PcInstance) {
			L1PcInstance target = (L1PcInstance) cha;
			int hpPercent = target.getCurrentHpPercent();
			if (magic.calcProbabilityMagic(_skillId) && hpPercent <= 30) {// 30%이하 즉사
				dmg = target.getCurrentHp();
				target.send_effect(18404);
			} else {
				int addDmg = (int)(target.getCurrentHp() * 0.3D);// 추가 대미지 대상의 HP 30%
				dmg = addDmg > 1000 ? 1000 : addDmg;
			}
		}
		return dmg;
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
		pc.sendPackets(new S_OwnCharStatus(pc), true);
		super.wrap(pc, flag);
	}
	
	@Override
	public L1SkillActionHandler copyInstance() {
		return new Avenger().setValue(_skillId, _skill);
	}

}

