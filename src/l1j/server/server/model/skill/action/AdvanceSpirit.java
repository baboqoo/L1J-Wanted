package l1j.server.server.model.skill.action;

import l1j.server.server.construct.skill.L1PassiveId;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;

public class AdvanceSpirit extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		if (cha.isPassiveStatus(L1PassiveId.GIGANTIC) || cha.isPassiveStatus(L1PassiveId.ADVANCE_SPIRIT)) {
			return 0;
		}
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.setAdvenHp(pc.getBaseMaxHp() / 5);
			pc.setAdvenMp(pc.getBaseMaxMp() / 5);
			pc.addMaxHp(pc.getAdvenHp());
			pc.addMaxMp(pc.getAdvenMp());
			pc.sendPackets(new S_SpellBuffNoti(pc, _skillId, true, time), true);
		}
		return 0;
	}

	@Override
	public void stop(L1Character cha) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.addMaxHp(-pc.getAdvenHp());
			pc.addMaxMp(-pc.getAdvenMp());
			pc.setAdvenHp(0);
			pc.setAdvenMp(0);
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
		return new AdvanceSpirit().setValue(_skillId, _skill);
	}

}

