package l1j.server.server.model.skill.action;

import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;

public class Grace extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		if (attacker instanceof L1PcInstance) {
			L1PcInstance user = (L1PcInstance) attacker;
			int value = 1 + (user.getLevel() > 80 ? (user.getLevel() - 80) : 0);
			if (value > 15) {
				value = 15;
			}
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				if(pc.getSkill().hasSkillEffect(GRACE))pc.getSkill().removeSkillEffect(GRACE);
				pc.setGraceAvatarState(value);
				pc.getResistance().addToleranceAll(pc.getGraceAvatarState());
				pc.sendPackets(L1ServerMessage.sm4734);
			}
		}
		return 0;
	}

	@Override
	public void stop(L1Character cha) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.getResistance().addToleranceAll(-pc.getGraceAvatarState());
			pc.setGraceAvatarState(0);
			pc.sendPackets(L1ServerMessage.sm4741);
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
		return new Grace().setValue(_skillId, _skill);
	}

}

