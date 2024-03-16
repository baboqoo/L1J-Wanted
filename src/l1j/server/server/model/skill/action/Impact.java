package l1j.server.server.model.skill.action;

import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.serverpackets.message.S_ServerMessage;

public class Impact extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		if (attacker instanceof L1PcInstance) {
			L1PcInstance user = (L1PcInstance) attacker;
			int value = 5 + (user.getLevel() > 84 ? ((int)(user.getLevel() - 84) / 3) * 2 : 0);
			if (value > 15) {
				value = 15;
			}
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				if (pc.getSkill().hasSkillEffect(IMPACT)) {
					pc.getSkill().removeSkillEffect(IMPACT);
				}
				pc.setImpactState(value);
				pc.getResistance().addHitupAll(pc.getImpactState());
				pc.sendPackets(L1ServerMessage.sm4761);
			}
		}
		return 0;
	}

	@Override
	public void stop(L1Character cha) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.getResistance().addHitupAll(-pc.getImpactState());
			pc.setImpactState(0);
			pc.sendPackets(new S_ServerMessage(4754), true);
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
		return new Impact().setValue(_skillId, _skill);
	}

}

