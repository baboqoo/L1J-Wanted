package l1j.server.server.model.skill.action;

import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;

public class ClassTopGrace1st extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		cha.getAbility().addPVPDamageReduction(10);
		cha.addMaxHp(300);
		if (cha instanceof L1PcInstance) {
			icon((L1PcInstance) cha, time);
		}
		return 0;
	}

	@Override
	public void stop(L1Character cha) {
		cha.getAbility().addPVPDamageReduction(-10);
		cha.addMaxHp(-300);
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.sendPackets(new S_SpellBuffNoti(pc, _skillId, false, -1), true);
		}
	}
	
	@Override
	public void icon(L1PcInstance pc, int time) {
		pc.sendPackets(new S_SpellBuffNoti(pc, _skillId, true, time), true);
	}
	
	@Override
	public void wrap(L1PcInstance pc, boolean flag) {
		pc.sendPackets(new S_OwnCharStatus(pc), true);
		super.wrap(pc, flag);
	}
	
	@Override
	public L1SkillActionHandler copyInstance() {
		return new ClassTopGrace1st().setValue(_skillId, _skill);
	}
	
}

