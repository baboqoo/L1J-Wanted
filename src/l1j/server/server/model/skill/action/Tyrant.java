package l1j.server.server.model.skill.action;

import l1j.server.Config;
import l1j.server.server.construct.skill.L1PassiveId;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;

public class Tyrant extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		if (attacker.isPassiveStatus(L1PassiveId.TYRANT_EXCUTION)) {
			do_excution(attacker, cha, magic);
		}
		return 0;
	}
	
	/**
	 * 타이런트 엑스큐션
	 * @param attacker
	 * @param cha
	 * @param magic
	 */
	void do_excution(L1Character attacker, L1Character cha, L1Magic magic) {
		if (!magic.calcProbabilityMagic(STATUS_TYRANT_EXCUTION)) {
			return;
		}
		if (cha.getSkill().hasSkillEffect(STATUS_TYRANT_EXCUTION)) {
			cha.getSkill().removeSkillEffect(STATUS_TYRANT_EXCUTION);
		}
		int buffTime = 4000;// 4초
		if (attacker.getAbility().getStrangeTimeIncrease() > 0) {
			buffTime += attacker.getAbility().getStrangeTimeIncrease();
		}
		if (cha.getAbility().getStrangeTimeDecrease() > 0) {
			buffTime -= cha.getAbility().getStrangeTimeDecrease();
		}
		if (buffTime <= 0) {
			return;
		}
		cha.getSkill().setSkillEffect(STATUS_TYRANT_EXCUTION, buffTime);// 스킬 부여
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.addMoveSpeedDelayRate(-Config.SPELL.TYRANT_EXCUTION_MOVE_SPEED_RATE);
			pc.sendPackets(new S_SpellBuffNoti(pc, STATUS_TYRANT_EXCUTION, true, buffTime / 1000), true);
		}
	}

	@Override
	public void stop(L1Character cha) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.addMoveSpeedDelayRate(Config.SPELL.TYRANT_EXCUTION_MOVE_SPEED_RATE);
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
		return new Tyrant().setValue(_skillId, _skill);
	}

}

