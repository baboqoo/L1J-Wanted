package l1j.server.server.model.skill.action;

import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.serverpackets.S_SkillHaste;

public class Slow extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (pc.getHasteItemEquipped() > 0) {
				return 0;
			}
		}
		if (cha.getMoveState().getMoveSpeed() == 0) {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillHaste(pc.getId(), 2, time), true);
			}
			cha.broadcastPacket(new S_SkillHaste(cha.getId(), 2, time), true);
			cha.getMoveState().setMoveSpeed(2);
		} else if (cha.getMoveState().getMoveSpeed() == 1) {
			int skillNum = 0;
			if (cha.getSkill().hasSkillEffect(HASTE)) {
				skillNum = HASTE;
			} else if (cha.getSkill().hasSkillEffect(GREATER_HASTE)) {
				skillNum = GREATER_HASTE;
			} else if (cha.getSkill().hasSkillEffect(STATUS_HASTE)) {
				skillNum = STATUS_HASTE;
			}
			if (skillNum != 0) {
				cha.getSkill().removeSkillEffect(skillNum);
				cha.getSkill().killSkillEffectTimer(skillNum);
				cha.getSkill().removeSkillEffect(_skillId);
				((L1PcInstance) cha).sendPackets(new S_SkillHaste(cha.getId(), 1, 0), true);
				cha.getMoveState().setMoveSpeed(0);
			}
		}
		return 0;
	}

	@Override
	public void stop(L1Character cha) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.broadcastPacketWithMe(new S_SkillHaste(pc.getId(), 0, 0), true);
		}
		cha.getMoveState().setMoveSpeed(0);
	}
	
	@Override
	public void icon(L1PcInstance pc, int time) {
		pc.sendPackets(new S_SkillHaste(pc.getId(), 2, time), true);
		pc.broadcastPacket(new S_SkillHaste(pc.getId(), 2, 0), true);
	}
	
	@Override
	public void wrap(L1PcInstance pc, boolean flag) {
		super.wrap(pc, flag);
	}
	
	@Override
	public L1SkillActionHandler copyInstance() {
		return new Slow().setValue(_skillId, _skill);
	}

}

