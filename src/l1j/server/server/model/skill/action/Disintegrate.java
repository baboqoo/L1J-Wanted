package l1j.server.server.model.skill.action;

import l1j.server.Config;
import l1j.server.server.construct.skill.L1PassiveId;
import l1j.server.server.model.L1Ability;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1EffectSpawn;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PeopleInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;
import l1j.server.server.utils.CommonUtil;

public class Disintegrate extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		int dmgUp = 0;
		if (attacker.isPassiveStatus(L1PassiveId.DISINTEGREATE_NEMESIS)) {
			dmgUp = 1;// 네메시스 피격 대미지 상승
			nemesis(attacker, cha);
		}
		return dmgUp;
	}

	@Override
	public void stop(L1Character cha) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.sendPackets(S_Paralysis.STURN_OFF);
			pc.sendPackets(new S_SpellBuffNoti(pc, STATUS_STUN, false, 0), true);
		} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance) {
			L1NpcInstance npc = (L1NpcInstance) cha;
			npc.setParalyzed(false);
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
	
	/**
	 * 네메시스 스턴
	 * @param attacker
	 * @param cha
	 */
	private void nemesis(L1Character attacker, L1Character cha){
		int chance = Config.SPELL.NEMESIS_STUN_PROB;
		if (attacker.getResistance().getHitupSkill() > 0) {
			chance += attacker.getResistance().getHitupSkill();
		}
		if (cha.getResistance().getToleranceSkill() > 0) {
			chance -= cha.getResistance().getToleranceSkill();
		}
		if (CommonUtil.random(100) + 1 <= chance) {
			int duration = 4000;
			L1Ability ablity = attacker.getAbility();
			if (ablity.getStunDuration() > 0) {
				duration += ablity.getStunDuration() * 1000;
			}
			if (ablity.getStrangeTimeIncrease() > 0) {
				duration += ablity.getStrangeTimeIncrease();
			}
			if (cha.getAbility().getStrangeTimeDecrease() > 0) {
				duration -= cha.getAbility().getStrangeTimeDecrease();
			}
			if (duration <= 0) {
				return;
			}
			L1EffectSpawn.getInstance().spawnEffect(81262, duration, cha.getX(), cha.getY(), cha.getMapId());
			cha.getSkill().setSkillEffect(STATUS_DISINTEGRATE_NEMESIS, duration);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(S_Paralysis.STURN_ON);
				pc.sendPackets(new S_SpellBuffNoti(pc, STATUS_STUN, true, duration / 1000), true);
			} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof L1PeopleInstance) {
				L1NpcInstance npc = (L1NpcInstance) cha;
				npc.setParalyzed(true);
				npc.setParalysisTime(duration);
			}
		}
	}
	
	@Override
	public L1SkillActionHandler copyInstance() {
		return new Disintegrate().setValue(_skillId, _skill);
	}

}

