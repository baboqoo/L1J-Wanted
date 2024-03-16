package l1j.server.server.model.skill.action;

import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;

public class DeathPotionMonster extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (pc.isGhost() || pc.isDead() || pc.isAbsol() || pc.isBind()) {
				return 0;
			}
			pc.getSkill().setSkillEffect(MOB_RANGE_DEATH_POTION, 12 * 1000);
			pc.send_effect(7781);
			pc.sendPackets(new S_SpellBuffNoti(pc, MOB_RANGE_DEATH_POTION, true, 12), true);
			//pc.sendPackets(new S_PacketBox(S_PacketBox.NONE_TIME_ICON, 199, true), true);
		}
		return 0;
	}

	@Override
	public void stop(L1Character cha) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.sendPackets(new S_SpellBuffNoti(pc, MOB_RANGE_DEATH_POTION, false, -1), true);
		}
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
		return new DeathPotionMonster().setValue(_skillId, _skill);
	}

}

