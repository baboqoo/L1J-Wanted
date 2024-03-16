package l1j.server.server.model.skill.action;

import l1j.server.server.construct.skill.L1PassiveId;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.serverpackets.S_CharacterFollowEffect;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;

public class Tomahawk extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		if (attacker.isPassiveStatus(L1PassiveId.TOMAHAWK_HUNTER) && cha instanceof L1PcInstance && magic.calcProbabilityMagic(_skillId)) {
			L1PcInstance pc = (L1PcInstance) cha;
			int huntTime = 4000;
			if (attacker.getAbility().getStrangeTimeIncrease() > 0) {
				huntTime += attacker.getAbility().getStrangeTimeIncrease();
			}
			if (cha.getAbility().getStrangeTimeDecrease() > 0) {
				huntTime -= cha.getAbility().getStrangeTimeDecrease();
			}
			pc._tomahawkHuntPc = (L1PcInstance)attacker;// 공격자 세팅
			pc.getSkill().setSkillEffect(STATUS_TOMAHAWK_HUNT, huntTime);
			pc.sendPackets(new S_SpellBuffNoti(pc, STATUS_TOMAHAWK_HUNT, true, huntTime / 1000), true);// 피격자에게 아이콘 출력
			pc._tomahawkHuntPc.sendPackets(new S_CharacterFollowEffect(pc.getId(), true, 20597, true, huntTime / 1000), true);// 공격자에게 표식 출력
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
		return new Tomahawk().setValue(_skillId, _skill);
	}

}

