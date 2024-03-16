package l1j.server.server.model.skill.action;

import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;

public class MovingMonster extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		if (attacker instanceof L1NpcInstance) {
			L1NpcInstance npc	= (L1NpcInstance)attacker;
			if (npc._statusEscape) {
				return 0;
			}
			npc._statusEscape = true;
			int npcId = npc.getNpcId();
			if (npcId == 7800219) {// 몽환의 서큐버스
				npc._locEscape = npc.getLocation().randomLocation(1, 2, false);
			} else {
				npc._locEscape = npc.getLocation().randomLocation(10, 14, false);
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
		// TODO Auto-generated method stub
	}
	
	@Override
	public L1SkillActionHandler copyInstance() {
		return new MovingMonster().setValue(_skillId, _skill);
	}

}

