package l1j.server.server.model.skill.action;

import l1j.server.Config;
import l1j.server.server.construct.L1RegionStatus;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.serverpackets.S_CharacterFollowEffect;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;
import l1j.server.server.utils.CommonUtil;

public class Behemoth extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		if (cha instanceof L1PcInstance == false) {
			return 0;
		}
		L1PcInstance targetPc	= (L1PcInstance) cha;
		int total_damage		= 0;
		for (L1PcInstance visible : L1World.getInstance().getVisiblePlayerWithTarget(targetPc, 1)) {// 대상 주위 1칸 내 모든 적들(대상 포함)
			if (targetPc != visible && !isEnemyTarget((L1PcInstance)attacker, visible)) {
				continue;
			}
			L1Magic visible_magic	= new L1Magic(attacker, visible);
			int spell_damage		= visible_magic.calcMagicDamage(_skillId);
			if (spell_damage > visible.getCurrentHp()) {
				spell_damage = visible.getCurrentHp();
			}
			visible_magic.commit(spell_damage, 0);// 대미지 전달
			total_damage += spell_damage;
			
			// 일정 확률로 귀환 불가(이동 가능) + 이동 속도 감소
			if (visible_magic.calcProbabilityMagic(_skillId)) {
				int duration = CommonUtil.randomIntChoice(BEHEMOTH_ARRAY);
				visible.getSkill().setSkillEffect(STATUS_BEHEMOTH_DEBUFF, duration);
				visible.addMoveSpeedDelayRate(-Config.SPELL.BEHEMOTH_MOVE_SPEED_RATE);
				visible.broadcastPacketWithMe(new S_CharacterFollowEffect(visible.getId(), true, 21964, true, duration), true);
				visible.sendPackets(new S_SpellBuffNoti(visible, STATUS_BEHEMOTH_DEBUFF, true, duration / 1000), true);
			}
		}
		
		if (total_damage > 0) {
			int drain_hp = (int)(total_damage * 0.4D);// 대미지의 40%
			if (drain_hp > 2000) {// 최대 2000
				drain_hp = 2000;
			}
			attacker.setCurrentHp(attacker.getCurrentHp() + drain_hp);// HP를 흡수한다.
		}
		return 0;
	}

	@Override
	public void stop(L1Character cha) {
	}
	
	@Override
	public void icon(L1PcInstance pc, int time) {
		pc.sendPackets(new S_SpellBuffNoti(pc, _skillId, true, time), true);
	}
	
	/**
	 * 적으로 간주할 대상 유효성 검사
	 * @param attacker
	 * @param target
	 * @return boolean
	 */
	boolean isEnemyTarget(L1PcInstance attacker, L1PcInstance target) {
		try {
			if (attacker == target || target.isBind() || target.isAbsol() || target.isGhost()
					|| target.getRegion() == L1RegionStatus.SAFETY
					|| (attacker.isInParty() && attacker.getParty().isMember(target))) {
				return false;
			}
			if (attacker.getClanid() > 0) {
				if (attacker.getClanid() == target.getClanid()) {
					return false;// 공격자와 같은 혈맹원
				}
				L1Clan clan = attacker.getClan();
				if (clan != null && clan.getAlliance() != null && !clan.getAlliance().isEmpty() 
						&& clan.getAlliance().containsKey(target.getClanid())) {// 같은 동맹원
					return false;
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	@Override
	public void wrap(L1PcInstance pc, boolean flag) {
		super.wrap(pc, flag);
	}
	
	@Override
	public L1SkillActionHandler copyInstance() {
		return new Behemoth().setValue(_skillId, _skill);
	}

}

