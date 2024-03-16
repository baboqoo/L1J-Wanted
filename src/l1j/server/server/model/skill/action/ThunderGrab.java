package l1j.server.server.model.skill.action;

import l1j.server.server.construct.skill.L1PassiveId;
import l1j.server.server.construct.skill.L1SkillInfo;
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

public class ThunderGrab extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		if (magic.calcProbabilityMagic(_skillId)) {
			int grabTime = CommonUtil.randomIntChoice(L1SkillInfo.THUNDER_GRAB_ARRAY); // 시간 랜덤을 위해
			if (attacker.isPassiveStatus(L1PassiveId.THUNDER_GRAB_BRAVE)) {
				grabTime += 1000;
			}
			if (attacker.getAbility().getStrangeTimeIncrease() > 0) {
				grabTime += attacker.getAbility().getStrangeTimeIncrease();
			}
			if (cha.getAbility().getStrangeTimeDecrease() > 0) {
				grabTime -= cha.getAbility().getStrangeTimeDecrease();
			}
			if (grabTime <= 0) {
				return 0;
			}
			L1EffectSpawn.getInstance().spawnEffect(81182, grabTime, cha.getX(), cha.getY(), cha.getMapId());
			cha.getSkill().setSkillEffect(STATUS_FREEZE, grabTime);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.send_effect(4184);
				pc.sendPackets(S_Paralysis.BIND_ON);
				pc.sendPackets(new S_SpellBuffNoti(pc, STATUS_HOLD, true, grabTime / 1000), true);
			} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof L1PeopleInstance) {
				L1NpcInstance npc = (L1NpcInstance) cha;
				npc.broadcastPacket(new S_Effect(npc.getId(), 4184), true);
				npc.setHold(true);
			}
		}
		return 0;
	}

	@Override
	public void stop(L1Character cha) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.sendPackets(S_Paralysis.BIND_OFF);
			pc.sendPackets(new S_SpellBuffNoti(pc, STATUS_HOLD, false, 0), true);
		} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof L1PeopleInstance) {
			L1NpcInstance npc = (L1NpcInstance) cha;
			npc.setHold(false);
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
		return new ThunderGrab().setValue(_skillId, _skill);
	}

}

