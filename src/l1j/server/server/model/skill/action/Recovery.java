package l1j.server.server.model.skill.action;

import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.L1SkillStatus;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;

public class Recovery extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			recovery(pc);
		}
		return 0;
	}

	@Override
	public void stop(L1Character cha) {
	}
	
	@Override
	public void icon(L1PcInstance pc, int time) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void wrap(L1PcInstance pc, boolean flag) {
		super.wrap(pc, flag);
	}
	
	private static final java.util.List<Integer> RECOVERY_REMOVE_SKILL_IDS = java.util.Arrays.asList(
		new Integer[] {
			SHOCK_STUN, EMPIRE, BONE_BREAK, PANTERA, POWER_GRIP, STATUS_FREEZE, STATUS_PHANTOM_NOMAL, 
			SHADOW_STEP, PRESSURE, CRUEL, ENSNARE,
			MOB_SHOCKSTUN_18, MOB_SHOCKSTUN_19, MOB_SHOCKSTUN_30, MOB_RANGESTUN_18, MOB_RANGESTUN_19, MOB_RANGESTUN_20, MOB_RANGESTUN_30, 
		}
	);
	
	private void recovery(L1PcInstance pc){
		L1SkillStatus status = pc.getSkill();
		for (int removeId : RECOVERY_REMOVE_SKILL_IDS) {
			if (status.hasSkillEffect(removeId)) {
				status.removeSkillEffect(removeId);
			}
		}
		if (status.hasSkillEffect(DESPERADO) && !pc._isDesperadoAbsolute) {
			status.removeSkillEffect(DESPERADO);
		}
	}
	
	@Override
	public L1SkillActionHandler copyInstance() {
		return new Recovery().setValue(_skillId, _skill);
	}

}

