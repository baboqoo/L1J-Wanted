package l1j.server.server.model.skill;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.construct.skill.L1SkillInfo;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.L1SkillStatus;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.model.skill.L1SkillId;

public class L1SkillTimer implements Runnable {
	private final L1Character _cha;
	private final int _skillId;
	private int _remainTime;
	private boolean _stop;
	private L1SkillActionHandler _handler;
	private L1Skills _skill;
	
	public L1SkillTimer(L1Character cha, int skillId, long timeMillis) {
		_cha			= cha;
		_skillId		= skillId;
		_remainTime		= (int)(timeMillis / 1000);
		_skill			= SkillsTable.getTemplate(_skillId);
		if (_skill != null && _skill.getHandler() != null) {
			_handler	= _skill.getHandler().copyInstance();
		}
	}

	@Override
	public void run() {
		if (_stop) {
			return;
		}
		if (--_remainTime <= 0) {
			if (_cha.getSkill().get_chain_reaction_attacker() != null && L1SkillInfo.CHAIN_REACTION_SKILL_LIST.contains(_skillId)) {
				do_chain_reaction();
				return;
			}
			_cha.getSkill().removeSkillEffect(_skillId);
			return;
		}
		GeneralThreadPool.getInstance().schedule(this, 1000L);
	}

	public void begin() {
		GeneralThreadPool.getInstance().schedule(this, 1000L);
	}

	public void end() {
		_stop = true;
		if (_handler != null) {
			endHandler();
			return;
		}
		L1SkillStop.stopSkill(_cha, _skillId, _skill);
	}

	public void kill() {		
		if (_skillId == L1SkillId.STATUS_PSS) {
			L1PcInstance target = (L1PcInstance) _cha;
			target.getAccount().setPSSTime(_remainTime);			
		}
		_stop = true;
	}
	
	public void on(){
		if (!_stop) {
			return;
		}
		_stop = false;
		run();
	}
	
	public int getSkillId(){
		return _skillId;
	}

	public int getRemainTime() {
		return _remainTime;
	}
	
	private void endHandler(){
		_handler.stop(_cha);
		if (_cha instanceof L1PcInstance) {
			_handler.wrap((L1PcInstance)_cha, false);
		}
		_handler = null;
	}
	
	/**
	 * 체인 리액션 발동
	 */
	private void do_chain_reaction() {
		if (_cha instanceof L1PcInstance == false) {
			_cha.getSkill().removeSkillEffect(_skillId);
			return;
		}
		L1PcInstance target = (L1PcInstance) _cha;
		if (target == null) {
			return;
		}
		L1SkillStatus status = target.getSkill();
		L1Magic magic = new L1Magic(status.get_chain_reaction_attacker(), target);
		magic.commit(magic.calcMagicDamage(L1SkillId.CHAIN_REACTION), 0);// 발동 대미지 전달
		status.removeSkillEffect(L1SkillId.CHAIN_REACTION);
		
		target.send_effect(21955);// 머리위 발동 이팩트
		status.setSkillEffect(L1SkillId.STATUS_CHAIN_REACTION, 1000L);
		target.sendPackets(new S_SpellBuffNoti(target, L1SkillId.STATUS_CHAIN_REACTION, true, 1), true);
		
		GeneralThreadPool.getInstance().schedule(this, 1000L);// 디버프 1초 증가
	}
	
}
