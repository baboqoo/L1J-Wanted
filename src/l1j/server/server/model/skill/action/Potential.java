package l1j.server.server.model.skill.action;

import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;

public class Potential extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.setPotentialHp(pc.getMaxHp() / 5);
			pc.setPotentialMp(pc.getMaxMp() / 5);
			pc.setPotentialDg(pc.getAbility().getDg() / 5);
			pc.setPotentialEr(pc.getAbility().getEr() / 5);
			pc.setPotentialMr(pc.getResistance().getMr() / 5);
			pc.setPotentialSp(pc.getAbility().getSp() / 5);
			pc.addMaxHp(pc.getPotentialHp());
			pc.addMaxMp(pc.getPotentialMp());
			pc.getAbility().addDg(pc.getPotentialDg());
			pc.getAbility().addEr(pc.getPotentialEr());
			pc.getResistance().addMr(pc.getPotentialMr());
			pc.getAbility().addSp(pc.getPotentialSp());
			icon(pc, time);
		}
		return 0;
	}

	@Override
	public void stop(L1Character cha) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.addMaxHp(-pc.getPotentialHp());
			pc.addMaxMp(-pc.getPotentialMp());
			pc.getAbility().addDg(-pc.getPotentialDg());
			pc.getAbility().addEr(-pc.getPotentialEr());
			pc.getResistance().addMr(-pc.getPotentialMr());
			pc.getAbility().addSp(-pc.getPotentialSp());
			pc.setPotentialHp(0);
			pc.setPotentialMp(0);
			pc.setPotentialDg(0);
			pc.setPotentialEr(0);
			pc.setPotentialMr(0);
			pc.setPotentialSp(0);
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
		return new Potential().setValue(_skillId, _skill);
	}

}

