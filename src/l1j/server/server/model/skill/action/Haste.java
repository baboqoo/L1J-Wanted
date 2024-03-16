package l1j.server.server.model.skill.action;

import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.serverpackets.S_SkillHaste;

public class Haste extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		if (cha.getMoveState().getMoveSpeed() != 2) {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				if (pc.getHasteItemEquipped() > 0) {
					return 0;
				}
				pc.setDrink(false);
				pc.sendPackets(new S_SkillHaste(pc.getId(), 1, time), true);
			}
			cha.broadcastPacket(new S_SkillHaste(cha.getId(), 1, 0), true);
			cha.getMoveState().setMoveSpeed(1);
		} else {
			int skillNum = 0;
			if (cha.getSkill().hasSkillEffect(SLOW)) {
				skillNum = SLOW;
			} else if (cha.getSkill().hasSkillEffect(GREATER_SLOW)) {
				skillNum = GREATER_SLOW;
			} else if (cha.getSkill().hasSkillEffect(ENTANGLE)) {
				skillNum = ENTANGLE;
			} else if (cha.getSkill().hasSkillEffect(MOB_SLOW_1)) {
				skillNum = MOB_SLOW_1;
			} else if (cha.getSkill().hasSkillEffect(MOB_SLOW_18)) {
				skillNum = MOB_SLOW_18;
			}
			
			if (skillNum != 0) {
				cha.getSkill().removeSkillEffect(skillNum);
				cha.getSkill().removeSkillEffect(HASTE);
				cha.getMoveState().setMoveSpeed(0);
			}
		}
		return 0;
	}

	@Override
	public void stop(L1Character cha) {
		cha.getMoveState().setMoveSpeed(0);
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.broadcastPacketWithMe(new S_SkillHaste(pc.getId(), 0, 0), true);
		}
	}
	
	@Override
	public void icon(L1PcInstance pc, int time) {
		pc.sendPackets(new S_SkillHaste(pc.getId(), 1, time), true);
		pc.broadcastPacket(new S_SkillHaste(pc.getId(), 1, 0), true);
	}
	
	@Override
	public void wrap(L1PcInstance pc, boolean flag) {
		super.wrap(pc, flag);
	}
	
	@Override
	public L1SkillActionHandler copyInstance() {
		return new Haste().setValue(_skillId, _skill);
	}

}

