package l1j.server.server.model.skill.action;

import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;

public class BurningShot extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		if (attacker instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance)attacker;
			excute(pc, !pc._isBurningShot);
		}
		return 0;
	}
	
	void excute(L1PcInstance pc, boolean flag) {
		pc._isBurningShot = flag;
		pc.getResistance().addToleranceAll(flag ? 3 : -3);
		pc.getAbility().addPVPDamageReduction(flag ? 8 : -8);
		pc.sendPackets(flag ? S_Paralysis.RESTRICT_ON : S_Paralysis.RESTRICT_OFF);
		pc.sendPackets(new S_SpellBuffNoti(pc, _skillId, flag, 0), true);
	}
	
	/**
	 * 버닝샷 강제 해제
	 * @param pc
	 */
	public static void dispose(L1PcInstance pc) {
		pc._isBurningShot = false;
		pc.getResistance().addToleranceAll(-3);
		pc.getAbility().addPVPDamageReduction(-8);
		pc.sendPackets(S_Paralysis.RESTRICT_OFF);
		pc.sendPackets(new S_SpellBuffNoti(pc, BURNING_SHOT, false, 0), true);
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
		pc.sendPackets(new S_PacketBox(S_PacketBox.ATTACK_RANGE, pc, pc.getWeapon()), true);
		super.wrap(pc, flag);
	}
	
	@Override
	public L1SkillActionHandler copyInstance() {
		return new BurningShot().setValue(_skillId, _skill);
	}

}

