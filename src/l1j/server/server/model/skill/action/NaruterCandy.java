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

public class NaruterCandy extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		byte value = cha.getLevel() >= 1 && cha.getLevel() <= 60 ? (byte)7 : (byte)6;
		cha.getAbility().addAddedDex(value);
		cha.getAbility().addAddedStr(value);
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.sendPackets(new S_Dexup(pc, value, time), true);
			pc.sendPackets(new S_Strup(pc, value, time), true);
		}
		return 0;
	}

	@Override
	public void stop(L1Character cha) {
		byte value = cha.getLevel() >= 1 && cha.getLevel() <= 60 ? (byte)-7 : (byte)-6;
		cha.getAbility().addAddedDex(value);
		cha.getAbility().addAddedStr(value);
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.sendPackets(new S_Dexup(pc, 1, 0), true);
			pc.sendPackets(new S_Strup(pc, 1, 0), true);
		}
	}
	
	@Override
	public void icon(L1PcInstance pc, int time) {
		int levelValue = pc.getLevel() >= 1 && pc.getLevel() <= 60 ? 7 : 6;
		pc.sendPackets(new S_Dexup(pc, levelValue, time), true);
		pc.sendPackets(new S_Strup(pc, levelValue, time), true);
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
		return new NaruterCandy().setValue(_skillId, _skill);
	}

}

