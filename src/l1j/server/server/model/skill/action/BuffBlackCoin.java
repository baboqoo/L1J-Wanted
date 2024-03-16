package l1j.server.server.model.skill.action;

import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.serverpackets.S_Effect;
import l1j.server.server.serverpackets.S_OwnCharStatus;

public class BuffBlackCoin extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		cha.getAC().addAc(-2);
		cha.getAbility().addShortHitup(3);
		cha.addMaxHp(20);
		cha.addMaxMp(13);
		cha.getResistance().addToleranceSpirit(10); 
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.sendPackets(new S_Effect(pc.getId(), 4914), true);
		}
		return 0;
	}

	@Override
	public void stop(L1Character cha) {
		cha.getAC().addAc(2);
		cha.getAbility().addShortHitup(-3);
		cha.addMaxHp(-20);
		cha.addMaxMp(-13);
		cha.getResistance().addToleranceSpirit(-10);
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
		return new BuffBlackCoin().setValue(_skillId, _skill);
	}

}

