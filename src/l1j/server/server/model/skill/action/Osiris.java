package l1j.server.server.model.skill.action;

import java.util.ArrayList;
import java.util.Collections;

import l1j.server.Config;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.construct.L1RegionStatus;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1EffectSpawn;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.action.S_UseAttackSkill;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;
import l1j.server.server.utils.CommonUtil;

public class Osiris extends L1SkillActionHandler {
	
	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		int changeBuffDuration = CommonUtil.randomIntChoice(OSIRIS_ARRAY);
		if (attacker.getAbility().getStrangeTimeIncrease() > 0) {
			changeBuffDuration += attacker.getAbility().getStrangeTimeIncrease();
		}
		if (cha.getAbility().getStrangeTimeDecrease() > 0) {
			changeBuffDuration -= cha.getAbility().getStrangeTimeDecrease();
		}
		if (changeBuffDuration <= 0) {
			return 0;
		}
		L1EffectSpawn.getInstance().spawnEffect(64018, changeBuffDuration, cha.getX(), cha.getY(), cha.getMapId());
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.sendPackets(S_Paralysis.OSIRIS_ON);
			pc.sendPackets(new S_SpellBuffNoti(pc, _skillId, true, changeBuffDuration / 1000), true);
			transition((L1PcInstance)attacker, pc, Config.SPELL.OSIRIS_TRANSITION_COUNT);
		}
		return changeBuffDuration;
	}

	@Override
	public void stop(L1Character cha) {
		if (cha instanceof L1PcInstance) {
			((L1PcInstance) cha).sendPackets(S_Paralysis.OSIRIS_OFF);
		}
	}
	
	/**
	 * 스킬을 전이 시킨다
	 * @param first 최초 전이자
	 * @param second 전이자
	 * @param cnt 전이시킬 횟수
	 */
	private void transition(L1PcInstance first, L1PcInstance second, int cnt){
		if (first == null || cnt <= 0) {
			return;
		}
		ArrayList<L1PcInstance> list = L1World.getInstance().getVisiblePlayer(second, 2);
		if (list == null || list.isEmpty()) {
			return;
		}
		Collections.shuffle(list);// 순서 석기
		for (L1PcInstance third : list) {
			if (third == null || third.isDead() || !isTransitionTarget(first, second, third)) {
				continue;
			}
			L1Magic magic = new L1Magic(first, third);
			if (magic.calcProbabilityMagic(_skillId)) {
				Runnable r = () -> {
					casting(first, second, third, cnt, magic);
				};
				GeneralThreadPool.getInstance().schedule(r, 100L);
			}
			break;
		}
	}
	
	/**
	 * 스킬 전이
	 * @param first
	 * @param third
	 * @param cnt
	 */
	private void casting(L1PcInstance first, L1PcInstance second, L1PcInstance third, int cnt, L1Magic magic) {
		if (first == null || second == null || third == null) {
			return;
		}
		second.broadcastPacketWithMe(new S_UseAttackSkill(second, third.getId(), _skill.getCastGfx(), third.getX(), third.getY(), 0, false), true);
		int changeBuffDuration = CommonUtil.randomIntChoice(OSIRIS_ARRAY);
		if (first.getAbility().getStrangeTimeIncrease() > 0) {
			changeBuffDuration += first.getAbility().getStrangeTimeIncrease();
		}
		if (third.getAbility().getStrangeTimeDecrease() > 0) {
			changeBuffDuration -= third.getAbility().getStrangeTimeDecrease();
		}
		L1EffectSpawn.getInstance().spawnEffect(64018, changeBuffDuration, third.getX(), third.getY(), third.getMapId());
		
		if (_skill.getDamageValue() > 0) {
			int dmg = magic.calcMagicDamage(_skillId);
			if ((_skillId == TEMPEST || _skillId == OSIRIS) && (dmg = (int)(third.getMaxHp() * 0.1D)) > 500) {
				dmg = 500;// HP에 비례하여 대미지 설정(최대 500)
			}
			magic.commit(dmg, 0);// 대미지 전달
		}
		
		if (!third.isDead()) {
			third.sendPackets(S_Paralysis.OSIRIS_ON);
			third.sendPackets(new S_SpellBuffNoti(third, _skillId, true, changeBuffDuration / 1000), true);
			third.getSkill().setSkillEffect(_skillId, changeBuffDuration);
		}
		transition(first, third, --cnt);
	}
	
	/**
	 * 전이시킬 대상 유효성 검사
	 * @param first 최초 전이자
	 * @param second 전이자
	 * @param third 전이 시킬 대상
	 * @return boolean
	 */
	private boolean isTransitionTarget(L1PcInstance first, L1PcInstance second, L1PcInstance third) {
		try {
			if (third.getId() == second.getId()
					|| third.getId() == first.getId()
					|| third.getSkill().hasSkillEffect(_skillId)
					|| third.isBind() || third.isAbsol() || third.isGhost()
					|| third.getRegion() == L1RegionStatus.SAFETY
					|| first.isInParty() && first.getParty().isMember(third)) {
				return false;
			}
			if (third.getClanid() > 0) {
				if (third.getClanid() == first.getClanid()) {
					return false;// 최초 전이자와 같은 혈맹원
				}
				L1Clan clan = first.getClan();
				if (clan != null && clan.getAlliance() != null && !clan.getAlliance().isEmpty() 
						&& clan.getAlliance().containsKey(third.getClanid())) {// 최초 전이자와 같은 동맹원
					return false;
				}
			}
			if (!second.glanceCheck(2, third.getX(), third.getY(), false)) {
				return false;
			}
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
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
		return new Osiris().setValue(_skillId, _skill);
	}

}

