package l1j.server.server.model.skill.action;

import java.util.Random;

import l1j.server.Config;
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
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;
import l1j.server.server.utils.CommonUtil;

public class TripleArrow extends L1SkillActionHandler {
	private static final Random random = new Random(System.nanoTime());

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		if (attacker instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) attacker;
			boolean boost = attacker.isPassiveStatus(L1PassiveId.TRIPLE_ARROW_BOOST);
			pc.send_effect(boost ? 20750 : attacker.isPassiveStatus(L1PassiveId.GLORY_EARTH) ? 19317 : 15103);
			if (pc.getAbility().getTripleArrowStun() > 0 && !pc.getSkill().hasSkillEffect(STATUS_TRIPLE_STUN_DELAY) && pc.getLocation().getTileLineDistance(cha.getLocation()) <= 3 && random.nextInt(100) + 1 <= Config.SPELL.TRIPLE_STUN_CHANCE) {
				tripleArrowStun(pc, cha);
			}
			pc.isTriple = true;
			int tripleCount = boost ? 4 : 3;
			for (int i = tripleCount; i > 0; i--) {
				if (cha instanceof L1MonsterInstance && (cha.isDead() || cha.getCurrentHp() <= 0)) {
					break;
				}
				cha.onAction(pc);
			}
			pc.isTriple = false;
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
		pc.sendPackets(new S_OwnCharStatus(pc), true);
		super.wrap(pc, flag);
	}
	
	private void tripleArrowStun(L1PcInstance attacker, L1Character cha){
		int time = CommonUtil.randomIntChoice(L1SkillInfo.TRIPLE_STUN_ARRAY); // 시간 랜덤을 위해
		if (attacker.getAbility().getStrangeTimeIncrease() > 0) {
			time += attacker.getAbility().getStrangeTimeIncrease();
		}
		if (cha.getAbility().getStrangeTimeDecrease() > 0) {
			time -= cha.getAbility().getStrangeTimeDecrease();
		}
		if (time <= 0) {
			return;
		}
		L1EffectSpawn.getInstance().spawnEffect(81068, time, cha.getX(), cha.getY(), cha.getMapId());
		attacker.getSkill().setSkillEffect(STATUS_TRIPLE_STUN_DELAY, 6000); // 발동후 딜레이처리
		cha.getSkill().setSkillEffect(PANTERA, time);
		if (cha instanceof L1PcInstance) {
			L1PcInstance target = (L1PcInstance) cha;
			target.sendPackets(S_Paralysis.STURN_ON);
			target.sendPackets(new S_SpellBuffNoti(target, STATUS_STUN, true, time / 1000), true);
		} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof L1PeopleInstance) {
			L1NpcInstance npc = (L1NpcInstance) cha;
			npc.setParalyzed(true);
		}
	}
	
	@Override
	public L1SkillActionHandler copyInstance() {
		return new TripleArrow().setValue(_skillId, _skill);
	}

}

