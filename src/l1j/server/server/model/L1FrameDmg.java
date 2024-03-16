package l1j.server.server.model;

import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_Effect;
import l1j.server.server.serverpackets.action.S_DoActionGFX;

public class L1FrameDmg {
	private Thread _timer;
	private final L1Character _attacker;
	private final L1Character _target;
	private final int _damage;

	public L1FrameDmg(L1Character attacker, L1Character target, int damage) {
		_attacker	= attacker;
		_target		= target;
		_damage		= damage;
		doInfection();
	}

	private class NormalPoisonTimer extends Thread {
		@Override
		public void run() {
			L1PcInstance player		= null;
			L1MonsterInstance mob	= null;
			while (true) {
				try {
					Thread.sleep(1000L);
				} catch (InterruptedException e) {
					break;
				}
				if (_target == null || _target.isDead() || !_target.getSkill().hasSkillEffect(L1SkillId.STATUS_FRAME)) {
					break;
				}
				if (_target instanceof L1PcInstance) {
					player = (L1PcInstance) _target;
					player.receiveDamage(_attacker, _damage);
					player.send_effect(18509);
					player.broadcastPacketWithMe(new S_DoActionGFX(player.getId(), ActionCodes.ACTION_Damage), true);
				} else if (_target instanceof L1MonsterInstance) {
					mob = (L1MonsterInstance) _target;
					mob.receiveDamage(_attacker, _damage);
					Broadcaster.broadcastPacket(mob, new S_Effect(mob.getId(), 18509), true);
					Broadcaster.broadcastPacket(mob, new S_DoActionGFX(mob.getId(), ActionCodes.ACTION_Damage), true);
				}
			}
			cure();
		}
	}

	boolean isDamageTarget(L1Character cha) {
		return (cha instanceof L1PcInstance) || (cha instanceof L1MonsterInstance);
	}

	private void doInfection() {
		_target.getSkill().setSkillEffect(L1SkillId.STATUS_FRAME, 3000);
		if (isDamageTarget(_target)) {
			_timer = new NormalPoisonTimer();
			GeneralThreadPool.getInstance().execute(_timer);
		}
	}

	public void cure() {
		if (_timer != null) {
			_timer.interrupt();
		}
		_target.getSkill().killSkillEffectTimer(L1SkillId.STATUS_FRAME);
	}
}

