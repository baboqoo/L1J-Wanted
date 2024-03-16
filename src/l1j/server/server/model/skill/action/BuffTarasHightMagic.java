package l1j.server.server.model.skill.action;

import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.serverpackets.S_Dexup;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_OwnCharStatus2;
import l1j.server.server.serverpackets.S_Strup;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;

public class BuffTarasHightMagic extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		cha.getAbility().addAddedStr((byte) 6);
		cha.getAbility().addAddedDex((byte) 6);
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.sendPackets(new S_Strup(pc, 6, time), true);
			pc.sendPackets(new S_Dexup(pc, 6, time), true);
			pc.sendPackets(new S_SpellBuffNoti(pc, _skillId, true, time), true);
		}
		return 0;
	}

	@Override
	public void stop(L1Character cha) {
		cha.getAbility().addAddedStr((byte) -6);
		cha.getAbility().addAddedDex((byte) -6);
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.sendPackets(new S_Strup(pc, 1, 0), true);
			pc.sendPackets(new S_Dexup(pc, 1, 0), true);
			pc.sendPackets(new S_SpellBuffNoti(pc, _skillId, false, 0), true);
		}
	}
	
	@Override
	public void icon(L1PcInstance pc, int time) {
		pc.sendPackets(new S_Strup(pc, 6, time), true);
		pc.sendPackets(new S_Dexup(pc, 6, time), true);
		pc.sendPackets(new S_SpellBuffNoti(pc, _skillId, true, time), true);
	}
	
	@Override
	public void wrap(L1PcInstance pc, boolean flag) {
		pc.sendPackets(new S_OwnCharStatus(pc), true);
		pc.sendPackets(new S_OwnCharStatus2(pc), true);
		pc.sendPackets(new S_OwnCharAttrDef(pc), true);
		super.wrap(pc, flag);
	}
	
	@Override
	public L1SkillActionHandler copyInstance() {
		return new BuffTarasHightMagic().setValue(_skillId, _skill);
	}

}

