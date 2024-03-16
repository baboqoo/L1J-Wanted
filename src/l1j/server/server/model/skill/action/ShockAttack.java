package l1j.server.server.model.skill.action;

import l1j.server.Config;
import l1j.server.server.construct.skill.L1SkillInfo;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;
import l1j.server.server.utils.CommonUtil;

public class ShockAttack extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		if (magic.calcProbabilityMagic(SHOCK_ATTACK)) {// 성공시
			if (cha.getSkill().hasSkillEffect(_skillId)) {
				cha.getSkill().removeSkillEffect(_skillId);
			}
			if (cha.getSkill().hasSkillEffect(STATUS_SHOCK_ATTACK_TEL)) {
				cha.getSkill().removeSkillEffect(STATUS_SHOCK_ATTACK_TEL);
			}
			boolean shokAttackType = CommonUtil.random(100) + 1 <= Config.SPELL.SHOCK_ATTACK_TELEPORT;
			int buffTime = CommonUtil.randomIntChoice(L1SkillInfo.SHOCK_ATTACK_ARRAY);
			if (attacker.getAbility().getStrangeTimeIncrease() > 0) {
				buffTime += attacker.getAbility().getStrangeTimeIncrease();
			}
			if (cha.getAbility().getStrangeTimeDecrease() > 0) {
				buffTime -= cha.getAbility().getStrangeTimeDecrease();
			}
			if (buffTime <= 0) {
				return 0;
			}
			cha.getSkill().setSkillEffect(shokAttackType ? STATUS_SHOCK_ATTACK_TEL : _skillId, buffTime);// 스킬 부여
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMoveSpeedDelayRate(-Config.SPELL.SHOCK_ATTACK_MOVE_SPEED_RATE);
				pc.sendPackets(new S_SpellBuffNoti(pc, shokAttackType ? STATUS_SHOCK_ATTACK_TEL : _skillId, true, buffTime / 1000), true);
			}
		}
		return 0;
	}

	@Override
	public void stop(L1Character cha) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.addMoveSpeedDelayRate(Config.SPELL.SHOCK_ATTACK_MOVE_SPEED_RATE);
			pc.sendPackets(new S_SpellBuffNoti(pc, _skillId, false, -1), true);
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
		return new ShockAttack().setValue(_skillId, _skill);
	}

}

