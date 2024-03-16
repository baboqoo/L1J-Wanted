package l1j.server.server.model;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.SingleTask;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PeopleInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_Paralysis;

public class L1CurseParalysis extends L1Paralysis {
	private final L1Character _target;
	private final int _delay;
	private final int _duration;

	private SingleTask _timer;

	private class ParalysisDelayTimer extends SingleTask {
		@Override
		public void execute() {
			if (isActive()) {
				_target.getSkill().killSkillEffectTimer(L1SkillId.STATUS_CURSE_PARALYZING);
				if (_target instanceof L1PcInstance) {
					L1PcInstance player = (L1PcInstance) _target;
					if (!player.isDead()) {
						player.sendPackets(S_Paralysis.PARALYSIS_ON);
						player.sendPackets(new S_PacketBox(S_PacketBox.POSION_ICON, player, 2, _duration / 1000), true);
					}
					_timer = new ParalysisTimer();
					_target.getSkill().setSkillEffect(L1SkillId.STATUS_CURSE_PARALYZED, 0);
					_target.setParalyzed(true);// 석화 시작
					GeneralThreadPool.getInstance().schedule(_timer, _duration);
				} else if (_target instanceof L1MonsterInstance || _target instanceof L1PetInstance || _target instanceof L1SummonInstance || _target instanceof L1PeopleInstance) {
					_timer = new ParalysisTimer();
					_target.getSkill().setSkillEffect(L1SkillId.STATUS_CURSE_PARALYZED, 0);
					_target.setParalyzed(true);// 석화 시작
					GeneralThreadPool.getInstance().schedule(_timer, _duration);
				}
			}
		}
	}

	private class ParalysisTimer extends SingleTask {
		@Override
		public void execute() {
			_target.getSkill().killSkillEffectTimer(L1SkillId.STATUS_CURSE_PARALYZED);
			if (_target instanceof L1PcInstance) {
				L1PcInstance player = (L1PcInstance) _target;
				if (!player.isDead()) {
					player.sendPackets(new S_PacketBox(S_PacketBox.POSION_ICON, player, 2, 0), true);
					player.sendPackets(S_Paralysis.PARALYSIS_OFF);
				}
			}
			_timer = null;
			cure();
		}
	}

	private L1CurseParalysis(L1Character cha, int delay, int duration) {
		_target		= cha;
		_delay		= delay;
		_duration	= duration;
		if (_target instanceof L1PcInstance) {
			((L1PcInstance) _target).sendPackets(L1ServerMessage.sm212);// 커스-페럴라이즈: 석화 상태(진행 중)
		}
		_target.setPoisonEffect(2);
		_timer = new ParalysisDelayTimer();
		_target.getSkill().setSkillEffect(L1SkillId.STATUS_CURSE_PARALYZING, 0);
		// TODO 타이머 시작
		GeneralThreadPool.getInstance().schedule(_timer, _delay);
	}

	/**
	 * 커스 페럴라이즈 시작
	 * @param cha
	 * @param delay
	 * @param time
	 * @return boolean
	 */
	public static boolean curse(L1Character cha, int delay, int time) {
		if (!(cha instanceof L1PcInstance || cha instanceof L1MonsterInstance)) {
			return false;
		}
		if (cha.getSkill().hasSkillEffect(L1SkillId.STATUS_CURSE_PARALYZING) || cha.getSkill().hasSkillEffect(L1SkillId.STATUS_CURSE_PARALYZED)) {
			return false;
		}
		cha.setParalaysis(new L1CurseParalysis(cha, delay, time));
		return true;
	}

	@Override
	public int getEffectId() {
		return 2;
	}

	@Override
	public void cure() {
		if (_timer != null) {
			_timer.cancel();
			if (_timer instanceof ParalysisTimer) {
				if (!_timer.isExecuted()) {
					_timer.execute();
				}
			} else {
				_target.getSkill().killSkillEffectTimer(L1SkillId.STATUS_POISON_PARALYZING);
			}
			_timer = null;
		}
		if (_target instanceof L1PcInstance) {
			L1PcInstance player = (L1PcInstance) _target;
			player.sendPackets(new S_PacketBox(S_PacketBox.POSION_ICON, player, 0, 0), true);
			if (player.getSkill().hasSkillEffect(L1SkillId.HALPAS_POISON_BRESS)) {
				player.getSkill().removeSkillEffect(L1SkillId.HALPAS_POISON_BRESS);
			}
		}
		_target.setPoisonEffect(0);
		_target.setParalaysis(null);
		_target.setParalyzed(false);// 석화 종료
	}
}

