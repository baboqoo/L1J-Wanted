package l1j.server.server.model.skill.action;

import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;

public class AbsoluteMaan extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		ablity(cha, true);
		if (cha instanceof L1PcInstance) {
			icon((L1PcInstance) cha, time);
		}
		return 0;
	}

	@Override
	public void stop(L1Character cha) {
		ablity(cha, false);
	}
	
	void ablity(L1Character cha, boolean flag) {
		cha.getAbility().addMagicCritical(flag ? 1 : -1);
		cha.getAbility().addShortDmgup(flag ? 2 : -2);
		cha.getAbility().addLongDmgup(flag ? 2 : -2);
		cha.getResistance().addToleranceAll(flag ? 5 : -5);
		cha.getResistance().addHitupAll(flag ? 3 : -3);
		cha.getAbility().addDamageReduction(flag ? 5 : -5);
		cha.getResistance().addMr(flag ? 10 : -10);
		cha.getAbility().addEr(flag ? 10 : -10);
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
		return new AbsoluteMaan().setValue(_skillId, _skill);
	}

}

