package l1j.server.server.model.npc.action.id;

import l1j.server.server.construct.skill.L1SkillInfo;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.model.skill.L1BuffUtil;
import l1j.server.server.utils.StringUtil;

public class MetisBuffAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new MetisBuffAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private MetisBuffAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		if (s.equals("A")) {
			L1BuffUtil.skillArrayAction(pc, L1SkillInfo.DRAGON_STATUE_BUFF_ARRAY);
			return StringUtil.EmptyString;					 
		}
		return null;
	}
}

