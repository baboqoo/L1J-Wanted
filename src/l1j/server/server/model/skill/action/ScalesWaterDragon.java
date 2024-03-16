package l1j.server.server.model.skill.action;

import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;

public class ScalesWaterDragon extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (pc.getLevel() > 87) {
				pc._statusScalesWaterDragonValue = 1 + ((pc.getLevel() - 87) / 3);
				if (pc._statusScalesWaterDragonValue > 5) {
					pc._statusScalesWaterDragonValue = 5;
				}
			}
			pc.getAbility().addEr(3 * pc._statusScalesWaterDragonValue);
			pc.addScalesDragon(1);
			pc.sendPackets(new S_SpellBuffNoti(pc, _skillId, true, time), true);
		}
		return 0;
	}

	@Override
	public void stop(L1Character cha) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.getAbility().addEr(-(3 * pc._statusScalesWaterDragonValue));
			pc.addScalesDragon(-1);
			pc._statusScalesWaterDragonValue = 1;
		}
	}
	
	@Override
	public void icon(L1PcInstance pc, int time) {
		pc.sendPackets(new S_SpellBuffNoti(pc, _skillId, true, time), true);
	}
	
	@Override
	public void wrap(L1PcInstance pc, boolean flag) {
		super.wrap(pc, flag);
	}
	
	@Override
	public L1SkillActionHandler copyInstance() {
		return new ScalesWaterDragon().setValue(_skillId, _skill);
	}

}

