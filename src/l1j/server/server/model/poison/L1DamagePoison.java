package l1j.server.server.model.poison;

import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.RepeatTask;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.action.S_DoActionGFX;

public class L1DamagePoison extends L1Poison {
	private RepeatTask _timer;
	private final L1Character _attacker;
	private final L1Character _target;
	private final int _damageSpan;
	private final int _damage;
	private boolean _damaeMotion;

	private L1DamagePoison(L1Character attacker, L1Character target, int damageSpan, int damage, boolean damaeMotion) {
		_attacker		= attacker;
		_target			= target;
		_damageSpan		= damageSpan;
		_damage			= damage;
		_damaeMotion	= damaeMotion;
		doInfection();
	}

	private class NormalPoisonTimer extends RepeatTask {

		NormalPoisonTimer() {
			super(_damageSpan);
		}

		@Override
		public void execute() {
			L1PcInstance player = null;
			L1MonsterInstance mob = null;
			do {
				if (!_target.getSkill().hasSkillEffect(L1SkillId.STATUS_POISON) && !_target.getSkill().hasSkillEffect(L1SkillId.STATUS_TOMAHAWK)) {
					cure();
					break;
				}

				if (_target.isBind()) {
					cure();
					break;
				}

				if (_target instanceof L1PcInstance) {
					player = (L1PcInstance) _target;
					player.receiveDamage(_attacker, _damage);
					if (_target.getSkill().hasSkillEffect(L1SkillId.STATUS_TOMAHAWK)) {
						player.broadcastPacketWithMe(new S_DoActionGFX(player.getId(), ActionCodes.ACTION_Damage), true);
					}
					if (player.isDead()) {
						cure();
						break;
					}
				} else if (_target instanceof L1MonsterInstance) {
					mob = (L1MonsterInstance) _target;
					mob.receiveDamage(_attacker, _damage);
					if (mob.getSkill().hasSkillEffect(L1SkillId.STATUS_TOMAHAWK)){
						mob.broadcastPacket(new S_DoActionGFX(mob.getId(), ActionCodes.ACTION_Damage), true);
					}
					if (mob.isDead()) {
						cure();
						break;
					}
				}
			} while (false);
		}
	}

	boolean isDamageTarget(L1Character cha) {
		return (cha instanceof L1PcInstance) || (cha instanceof L1MonsterInstance);
	}

	private void doInfection() {
		if (_damaeMotion) {
			_target.getSkill().setSkillEffect(L1SkillId.STATUS_TOMAHAWK, 7000);
		} else {
			_target.getSkill().setSkillEffect(L1SkillId.STATUS_POISON, 30000);
			_target.setPoisonEffect(1);
		}
		if (isDamageTarget(_target)) {
			if (_target instanceof L1PcInstance){
			    ((L1PcInstance) _target).sendPackets(S_PacketBox.POISON_ICON_ON);
			}
			_timer = new NormalPoisonTimer();
			GeneralThreadPool.getInstance().execute(_timer);
		}
	}
	
	public static boolean doInfection(L1Character attacker, L1Character cha, int damageSpan, int damage) {
		return doInfection(attacker, cha, damageSpan, damage, false);
	}

	public static boolean doInfection(L1Character attacker, L1Character cha, int damageSpan, int damage, boolean damaeMotion) {
		if (!isValidTarget(cha)) {
			return false;
		}
		cha.setPoison(new L1DamagePoison(attacker, cha, damageSpan, damage, damaeMotion));
		return true;
	}

	@Override
	public int getEffectId() {
		return 1;
	}

	@Override
	public void cure() {
		if (_timer != null) {
			_timer.cancel();
		}
		if (_damaeMotion) {
			_target.getSkill().killSkillEffectTimer(L1SkillId.STATUS_TOMAHAWK);
		} else {
			if (_target instanceof L1PcInstance) {
			    ((L1PcInstance) _target).sendPackets(S_PacketBox.POISON_ICON_OFF);
			}
			_target.setPoisonEffect(0);
			_target.getSkill().killSkillEffectTimer(L1SkillId.STATUS_POISON);
		}
		_target.setPoison(null);
	}
}

