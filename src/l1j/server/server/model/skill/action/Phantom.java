package l1j.server.server.model.skill.action;

import l1j.server.server.construct.skill.L1PassiveId;
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

public class Phantom extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		boolean run = magic.calcProbabilityMagic(_skillId);
		if (attacker.isPassiveStatus(L1PassiveId.PHANTOM_RIPER)) {
			int flag = 1;
			if (attacker.isPassiveStatus(L1PassiveId.PHANTOM_DEATH)) {
				flag++;
			}
			
			// 리퍼 OR 데스 랜덤
			if (flag > 1 && CommonUtil.random(flag) + 1 == 2) {
				preShowEffect(cha, 18580);
				if (run) {
					excute(attacker, cha, 81059, STATUS_PHANTOM_DEATH);
				}
			} else {
				preShowEffect(cha, 18578);
				if (run) {
					boolean isReguiem	= attacker.isPassiveStatus(L1PassiveId.PHANTOM_REQUIEM);
					excute(attacker, cha, isReguiem ? 81060 : 81058, isReguiem ? STATUS_PHANTOM_REQUIEM : STATUS_PHANTOM_RIPER);
				}
			}
		} else {
			preShowEffect(cha, 18578);
			if (run) {
				excute(attacker, cha);
			}
		}
		return 0;
	}
	
	void excute(L1Character attacker, L1Character cha) {
		int phantomTime = duration(attacker, cha);
		if (phantomTime <= 0) {
			return;
		}
		L1EffectSpawn.getInstance().spawnEffect(81057, phantomTime, cha.getX(), cha.getY(), cha.getMapId());
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.getSkill().setSkillEffect(STATUS_PHANTOM_NOMAL, phantomTime);
			pc.sendPackets(new S_SpellBuffNoti(pc, STATUS_PHANTOM_NOMAL, true, phantomTime / 1000), true);
		}
	}
	
	void excute(L1Character attacker, L1Character cha, int npcId, int phandomSkillId) {
		int phantomTime = duration(attacker, cha);
		if (phantomTime <= 0) {
			return;
		}
		L1EffectSpawn.getInstance().spawnEffect(npcId, phantomTime, cha.getX(), cha.getY(), cha.getMapId());
		cha.getSkill().setSkillEffect(phandomSkillId, phantomTime);
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.sendPackets(S_Paralysis.PHANTOM_ON);
			pc.sendPackets(new S_SpellBuffNoti(pc, phandomSkillId, true, phantomTime / 1000), true);
		} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof L1PeopleInstance) {
			L1NpcInstance npc = (L1NpcInstance) cha;
			npc.setHold(true);
		}
	}
	
	void preShowEffect(L1Character cha, int effectId) {
		if (cha instanceof L1PcInstance) {
			((L1PcInstance) cha).send_effect(effectId);
		} else if (cha instanceof L1NpcInstance) {
			cha.broadcastPacket(new S_Effect(cha.getId(), effectId), true);
		}
	}
	
	int duration(L1Character attacker, L1Character cha) {
		int phantomTime = CommonUtil.randomIntChoice(PHANTOM_ARRAY);
		if (attacker.getAbility().getStrangeTimeIncrease() > 0) {
			phantomTime += attacker.getAbility().getStrangeTimeIncrease();
		}
		if (cha.getAbility().getStrangeTimeDecrease() > 0) {
			phantomTime -= cha.getAbility().getStrangeTimeDecrease();
		}
		return phantomTime;
	}

	@Override
	public void stop(L1Character cha) {
		switch(_skillId){
		case STATUS_PHANTOM_NOMAL:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SpellBuffNoti(pc, _skillId, false, 0), true);
			}
			break;
		default:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(S_Paralysis.PHANTOM_OFF);
				pc.sendPackets(new S_SpellBuffNoti(pc, _skillId, false, 0), true);
			} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance) {
				L1NpcInstance npc = (L1NpcInstance) cha;
				npc.setHold(false);
			}
			break;
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
		return new Phantom().setValue(_skillId, _skill);
	}

}

