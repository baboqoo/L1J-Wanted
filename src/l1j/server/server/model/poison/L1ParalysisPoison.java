package l1j.server.server.model.poison;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_Paralysis;

public class L1ParalysisPoison extends L1Poison {
	private final L1Character _target;
	private Thread _timer;
	private final int _delay;
	private final int _duration;
	private int _effectId = 1;

	private class ParalysisPoisonTimer extends Thread {
		private ParalysisPoisonTimer() {
			super("l1j.server.server.model.poison.L1ParalysisPoison.ParalysisPoisonTimer");
		}

		@Override
		public void run() {
			try {
				_target.getSkill().setSkillEffect(L1SkillId.STATUS_POISON_PARALYZING, 0);
				
				try {
					Thread.sleep(_delay);
				} catch (InterruptedException e) {
					_target.getSkill().killSkillEffectTimer(L1SkillId.STATUS_POISON_PARALYZING);
					return;
				}
				
				_effectId = 2;
				_target.setPoisonEffect(2);
				if (_target instanceof L1PcInstance) {
					L1PcInstance player = (L1PcInstance) _target;
					if (player.isDead() == false) {
						player.sendPackets(S_Paralysis.PARALYSIS_ON);
						// TODO 마비 중 타이머 시작
						_timer = new ParalysisTimer();
						GeneralThreadPool.getInstance().execute(_timer);
						if (isInterrupted())
							_timer.interrupt();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private class ParalysisTimer extends Thread {
		@Override
		public void run() {
			try {
				_target.getSkill().killSkillEffectTimer(L1SkillId.STATUS_POISON_PARALYZING);
				_target.getSkill().setSkillEffect(L1SkillId.STATUS_POISON_PARALYZED, 0);
				_target.setPoisonParalyzed(true);
				try {
					if (_target instanceof L1PcInstance){
						((L1PcInstance) _target).sendPackets(new S_PacketBox(S_PacketBox.POSION_ICON, 2, _duration / 1000), true);
					}
					Thread.sleep(_duration);
				} catch (InterruptedException e) {
				}

				_target.getSkill().killSkillEffectTimer(L1SkillId.STATUS_POISON_PARALYZED);
				if (_target instanceof L1PcInstance) {
					L1PcInstance player = (L1PcInstance) _target;
					if (!player.isDead()) {
						player.sendPackets(S_Paralysis.PARALYSIS_OFF);
					}
				}
				cure();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private L1ParalysisPoison(L1Character target, int delay, int duration) {
		_target		= target;
		_delay		= delay;
		_duration	= duration;
		
		_target.setPoisonEffect(1);
		if (_target instanceof L1PcInstance) {
			((L1PcInstance) _target).sendPackets(L1ServerMessage.sm212);// 커스-페럴라이즈: 석화 상태(진행 중)
			((L1PcInstance) _target).sendPackets(new S_PacketBox(S_PacketBox.POSION_ICON, 2, _delay / 1000), true);
			// TODO 마비 독 진행 타이머 시작
			_timer = new ParalysisPoisonTimer();
			GeneralThreadPool.getInstance().execute(_timer);
		}
	}

	/**
	 * 마비 독 시작
	 * @param cha
	 * @param delay
	 * @param duration
	 * @return boolean
	 */
	public static boolean doInfection(L1Character cha, int delay, int duration) {
		if (!L1Poison.isValidTarget(cha)) {
			return false;
		}
		cha.setPoison(new L1ParalysisPoison(cha, delay, duration));
		return true;
	}

	@Override
	public int getEffectId() {
		return _effectId;
	}

	@Override
	public void cure() {
		if (_timer != null) {
			_timer.interrupt();
		}
		if (_target instanceof L1PcInstance) {
			((L1PcInstance) _target).sendPackets(S_PacketBox.POISON_ICON_OFF);
		}
		_target.setPoisonEffect(0);
		_target.setPoison(null);
		_target.setPoisonParalyzed(false);
	}
}
