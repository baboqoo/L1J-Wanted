package l1j.server.server.model.skill.action;

import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;

public class BlackMaan extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		cha.getAbility().addDamageReduction(5);
		cha.getResistance().addMr(10);
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.sendPackets(new S_SpellBuffNoti(pc, _skillId, true, time), true);
			pc.sendPackets(new S_ServerMessage(7453), true);
		}
		return 0;
	}

	@Override
	public void stop(L1Character cha) {
		cha.getAbility().addDamageReduction(-5);
		cha.getResistance().addMr(-10);
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
		return new BlackMaan().setValue(_skillId, _skill);
	}

}

