package l1j.server.server.model.skill.action;

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
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.S_Effect;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;
import l1j.server.server.utils.CommonUtil;

public class Empire extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		boolean overload = attacker.isPassiveStatus(L1PassiveId.EMPIRE_OVERLOAD);
		int diffLevel = attacker.getLevel() - cha.getLevel();
		int[] stunTimeArray = null;
		if (attacker instanceof L1PcInstance && overload) {
			if (diffLevel <= -3) {
				stunTimeArray = EMPIRE_OVERLOAD_ARRAY_1;
			} else if (diffLevel >= -2 && diffLevel <= 2) {
				stunTimeArray = EMPIRE_OVERLOAD_ARRAY_2;
			} else if (diffLevel >= 3) {
				stunTimeArray = EMPIRE_OVERLOAD_ARRAY_3;
			}
		} else {
			if (diffLevel <= -3) {
				stunTimeArray = EMPIRE_ARRAY_1;
			} else if (diffLevel >= -2 && diffLevel <= 2) {
				stunTimeArray = EMPIRE_ARRAY_2;
			} else if (diffLevel >= 3 && diffLevel <= 5) {
				stunTimeArray = EMPIRE_ARRAY_3;
			} else if (diffLevel >= 6) {
				stunTimeArray = EMPIRE_ARRAY_4;
			}
		}
		int changeBuffDuration = CommonUtil.randomIntChoice(stunTimeArray);
		L1Ability ability = attacker.getAbility();
		if (ability.getStunDuration() > 0) {
			changeBuffDuration += ability.getStunDuration() * 1000;
		}
		if (ability.getStrangeTimeIncrease() > 0) {
			changeBuffDuration += ability.getStrangeTimeIncrease();
		}
		if (cha.getAbility().getStrangeTimeDecrease() > 0) {
			changeBuffDuration -= cha.getAbility().getStrangeTimeDecrease();
		}
		if (changeBuffDuration <= 0) {
			return 0;
		}
		L1EffectSpawn.getInstance().spawnEffect(overload ? 181163 : 181162, changeBuffDuration, cha.getX(), cha.getY(), cha.getMapId());
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (overload) {
				pc._empireDg = 15;
				pc.getAbility().addDg(-pc._empireDg);
				pc.send_effect(20406);// 적중 이팩트
			}
			pc.sendPackets(S_Paralysis.STURN_ON);
			pc.sendPackets(new S_SpellBuffNoti(pc, STATUS_STUN, true, changeBuffDuration / 1000), true);
		} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof L1PeopleInstance) {
			L1NpcInstance npc = (L1NpcInstance) cha;
			if (overload) {
				npc.broadcastPacket(new S_Effect(npc.getId(), 20406), true);
			}
			npc.setParalyzed(true);
			npc.setParalysisTime(changeBuffDuration);
		}
		return changeBuffDuration;
	}

	@Override
	public void stop(L1Character cha) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (pc._empireDg > 0) {
				pc.getAbility().addDg(pc._empireDg);
				pc._empireDg = 0;
			}
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
		super.wrap(pc, flag);
	}
	
	@Override
	public L1SkillActionHandler copyInstance() {
		return new Empire().setValue(_skillId, _skill);
	}

}

