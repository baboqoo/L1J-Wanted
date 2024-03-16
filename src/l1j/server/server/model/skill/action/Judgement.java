package l1j.server.server.model.skill.action;

import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.L1PinkName;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;

public class Judgement extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		if (attacker != null && cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (pc.getSkill().hasSkillEffect(JUDGEMENT)) {
				pc.getSkill().removeSkillEffect(JUDGEMENT);
			}
			L1PinkName.onHelp(cha, attacker);
			pc.sendPackets(new S_SpellBuffNoti(pc, _skillId, true, time), true);
			pc.setJudgementStr(attacker.getAbility().getTotalStr() / 15);
			pc.getResistance().addToleranceAll(-pc.getJudgementStr());
		}
		return 0;
	}

	@Override
	public void stop(L1Character cha) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.getResistance().addToleranceAll(pc.getJudgementStr());
			pc.setJudgementStr(0);
			pc.sendPackets(new S_SpellBuffNoti(pc, _skillId, false, 0), true);
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
		return new Judgement().setValue(_skillId, _skill);
	}

}

