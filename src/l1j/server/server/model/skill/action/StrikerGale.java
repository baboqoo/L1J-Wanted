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
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;
import l1j.server.server.utils.CommonUtil;

public class StrikerGale extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		int duration = time * 1000;
		int strangeTime	= attacker.getAbility().getStrangeTimeIncrease() > 0 ? attacker.getAbility().getStrangeTimeIncrease() : 0;
		if (cha.getAbility().getStrangeTimeDecrease() > 0) {
			strangeTime -= cha.getAbility().getStrangeTimeDecrease();
		}
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (attacker.isPassiveStatus(L1PassiveId.STRIKER_GALE_SHOT) && !pc.isStrikerGaleShotHold()) {
				int holdTime = CommonUtil.randomIntChoice(L1SkillInfo.STRIKER_GALE_SHOT_ARRAY) + strangeTime;
				L1EffectSpawn.getInstance().spawnEffect(61010, holdTime, pc.getX(), pc.getY(), pc.getMapId());
				pc.getSkill().setSkillEffect(STATUS_STRIKER_GALE_SHOT, holdTime);
				pc.sendPackets(S_Paralysis.BIND_ON);
				pc.setStrikerGaleShotHold(true);
			}
			pc.sendPackets(new S_SpellBuffNoti(pc, _skillId, true, time), true);
		} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof L1PeopleInstance) {
			L1NpcInstance npc = (L1NpcInstance) cha;
			if (attacker.isPassiveStatus(L1PassiveId.STRIKER_GALE_SHOT) && !npc.isStrikerGaleShotHold()) {
				int holdTime = CommonUtil.randomIntChoice(L1SkillInfo.STRIKER_GALE_SHOT_ARRAY) + strangeTime;
				L1EffectSpawn.getInstance().spawnEffect(61010, holdTime, npc.getX(), npc.getY(), npc.getMapId());
				npc.getSkill().setSkillEffect(STATUS_STRIKER_GALE_SHOT, holdTime);
				npc.setHold(true);
				npc.setStrikerGaleShotHold(true);
			}
		}
		return duration + strangeTime;
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
		pc.sendPackets(new S_OwnCharAttrDef(pc), true);
		super.wrap(pc, flag);
	}
	
	@Override
	public L1SkillActionHandler copyInstance() {
		return new StrikerGale().setValue(_skillId, _skill);
	}

}

