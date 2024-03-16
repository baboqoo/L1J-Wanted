package l1j.server.server.model.skill.action;

import l1j.server.server.ActionCodes;
import l1j.server.server.construct.skill.L1PassiveId;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1EffectSpawn;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.action.S_DoActionGFX;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;

public class Pressure extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		if (cha instanceof L1PcInstance == false) {
			return 0;
		}
		int duration = time * 1000;
		boolean deathRecall = attacker.isPassiveStatus(L1PassiveId.PRESSURE_DEATH_RECALL);
		if (attacker.getAbility().getStrangeTimeIncrease() > 0) {
			duration += attacker.getAbility().getStrangeTimeIncrease();
		}
		if (cha.getAbility().getStrangeTimeDecrease() > 0) {
			duration -= cha.getAbility().getStrangeTimeDecrease();
		}
		if (duration <= 0) {
			return 0;
		}
		L1EffectSpawn.getInstance().spawnEffect(deathRecall ? 61013 : 61012, duration, cha.getX(), cha.getY(), cha.getMapId());
		cha._pressureAttacker = (L1PcInstance) attacker;
		if (deathRecall) {
			cha.getSkill().setSkillEffect(STATUS_PRESSURE_DEATH_RECAL, duration);
		}
		L1PcInstance pc = (L1PcInstance) cha;
		pc.sendPackets(S_Paralysis.BIND_ON);
		pc.sendPackets(new S_SpellBuffNoti(pc, _skillId, true, duration / 1000), true);
		return duration;
	}

	@Override
	public void stop(L1Character cha) {
		if (cha instanceof L1PcInstance == false) {
			return;
		}
		L1PcInstance pc = (L1PcInstance) cha;
		if (pc._pressureDmg > 0) {
			if (!pc.isDead()) {
				if (pc._pressureAttacker != null) {
					L1Magic _magic = new L1Magic(pc._pressureAttacker, pc);
					_magic.commit(pc._pressureDmg, 0);
				}
				pc.broadcastPacketWithMe(new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Damage), true);
			}
			pc.send_effect(19335);
		}
		pc.sendPackets(S_Paralysis.BIND_OFF);
		pc.sendPackets(new S_SpellBuffNoti(pc, PRESSURE, false, 0), true);
		cha._pressureDmg = 0;
		cha._pressureAttacker = null;
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
		return new Pressure().setValue(_skillId, _skill);
	}

}

