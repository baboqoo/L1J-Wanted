package l1j.server.server.model.skill.action;

import java.util.Arrays;
import java.util.List;

import l1j.server.server.construct.item.L1ItemWeaponType;
import l1j.server.server.construct.skill.L1PassiveId;
import l1j.server.server.model.L1Ability;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1EffectSpawn;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PeopleInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;

public class FouSlayer extends L1SkillActionHandler {
	
	private static final List<L1ItemWeaponType> ENABLE_WEAPON = Arrays.asList(new L1ItemWeaponType[] {
			L1ItemWeaponType.SWORD,
			L1ItemWeaponType.TOHAND_SWORD,
			L1ItemWeaponType.SPEAR,
			L1ItemWeaponType.SINGLE_SPEAR,
			L1ItemWeaponType.CHAINSWORD
	});

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		if (attacker instanceof L1PcInstance == false) {
			return 0;
		}
		
		L1PcInstance attack_pc	= (L1PcInstance) attacker;
		L1ItemInstance weapon	= attack_pc.getWeapon();
		if (weapon == null || !ENABLE_WEAPON.contains(weapon.getItem().getWeaponType())) {// 무기 검증
			return 0;
		}
		boolean brave	= attack_pc.isPassiveStatus(L1PassiveId.FOU_SLAYER_BRAVE);// 포우 슬레이어:브레이브
		boolean force	= attack_pc.isPassiveStatus(L1PassiveId.FOU_SLAYER_FORCE);// 포우 슬레이어:포스
		boolean expose	= cha.getSkill().hasSkillEffect(STATUS_EXPOSE_WEAKNESS);// 약점 노출 상태
		if (brave && expose && !attack_pc.getSkill().hasSkillEffect(_skillId) && !attack_pc.isStun()) {
			do_expose_weakness_stun(attack_pc, cha, magic, force);
		}
		int addDmg = 0;
		L1Ability ability = attack_pc.getAbility();
		if (ability.getFowSlayerDamage() > 0) {
			addDmg += ability.getFowSlayerDamage();
		}
		if (expose) {
			addDmg = 40;
		}
		if (addDmg > 0) {
			ability.addShortDmgup(addDmg);
		}
		attack_pc.isFouSlayer = true;
		try {
			for (int i = 3; i > 0; i--) {
				if (cha instanceof L1MonsterInstance && (cha.isDead() || cha.getCurrentHp() <= 0)) {
					break;
				}
				cha.onAction(attack_pc);
			}
		} catch (Exception e) {
		}
		if (addDmg > 0) {
			ability.addShortDmgup(-addDmg);
		}
		attack_pc.isFouSlayer = false;
		attack_pc.send_effect(cha.getId(), force ? 21928 : (brave ? 17231 : 6509));
		return 0;
	}
	
	/**
	 * 대상 스턴 발생
	 * @param attacker
	 * @param cha
	 * @param magic
	 * @param force
	 */
	void do_expose_weakness_stun(L1PcInstance attacker, L1Character cha, L1Magic magic, boolean force) {
		if (!magic.calcProbabilityMagic(_skillId)) {
			return;
		}
		
		//int stun_duration = CommonUtil.randomIntChoice(L1SkillInfo.FOU_SLAYER_STUN_ARRAY); // 시간 랜덤을 위해
		int stun_duration = 1000;// 용기사 리뉴얼에 의해 기본 브레이브 스턴 1초
		if (attacker.getAbility().getStrangeTimeIncrease() > 0) {
			stun_duration += attacker.getAbility().getStrangeTimeIncrease();
		}
		if (cha.getAbility().getStrangeTimeDecrease() > 0) {
			stun_duration -= cha.getAbility().getStrangeTimeDecrease();
		}
		if (force) {
			stun_duration += 1000;
		}
		if (stun_duration <= 0) {
			return;
		}
		
		attacker.getSkill().setSkillEffect(_skillId, 10000L);// 딜레이
		// 약점 노출 단계 변경
		int expose_duration = 8000;
		attacker.getSkill().setSkillEffect(L1SkillId.EXPOSE_WEAKNESS, expose_duration);
		cha.getSkill().setSkillEffect(L1SkillId.STATUS_EXPOSE_WEAKNESS, expose_duration);
		attacker.sendPackets(force ? S_PacketBox.EXPOSE_WEAKNESS_FORCE : S_PacketBox.EXPOSE_WEAKNESS_BRAVE);
		
		L1EffectSpawn.getInstance().spawnEffect(force ? 81075 : 81055, stun_duration, cha.getX(), cha.getY(), cha.getMapId());
		cha.getSkill().setSkillEffect(force ? STATUS_FOU_SLAYER_FORCE_STUN : STATUS_STUN, stun_duration);
		if (cha instanceof L1PcInstance) {
			L1PcInstance target = (L1PcInstance) cha;
			target.sendPackets(S_Paralysis.STURN_ON);
			target.sendPackets(new S_SpellBuffNoti(target, force ? STATUS_FOU_SLAYER_FORCE_STUN : STATUS_STUN, true, stun_duration / 1000), true);
		} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof L1PeopleInstance) {
			L1NpcInstance npc = (L1NpcInstance) cha;
			if (npc.getNpcTemplate().isBossMonster()) {
				return;
			}
			npc.setParalyzed(true);
		}
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
	
	@Override
	public L1SkillActionHandler copyInstance() {
		return new FouSlayer().setValue(_skillId, _skill);
	}

}

