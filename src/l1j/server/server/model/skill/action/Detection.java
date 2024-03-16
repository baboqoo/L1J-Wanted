package l1j.server.server.model.skill.action;

import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;

public class Detection extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		if (cha instanceof L1NpcInstance) {
			L1NpcInstance npc = (L1NpcInstance) cha;
			int hiddenStatus = npc.getHiddenStatus();
			if (attacker != null && hiddenStatus == L1NpcInstance.HIDDEN_STATUS_SINK) {
				npc.appearOnGround((L1PcInstance)attacker);// 숨은 몬스터 등장
			}
			if (npc.getSkill().hasSkillEffect(MOB_BLIND_HIDING)) {
				npc.getSkill().removeSkillEffect(MOB_BLIND_HIDING);// 인비지 몬스터 등장
			}
		}
		return 0;
	}

	@Override
	public void stop(L1Character cha) {
		// TODO Auto-generated method stub

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
		return new Detection().setValue(_skillId, _skill);
	}

}

