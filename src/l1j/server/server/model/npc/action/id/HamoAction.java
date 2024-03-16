package l1j.server.server.model.npc.action.id;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Quest;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.utils.StringUtil;

public class HamoAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new HamoAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private HamoAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		return ham(pc, s);
	}
	
	private String ham(L1PcInstance pc, String s){
		if (s.equalsIgnoreCase("a")) {
			if (pc.getLevel() < 60) {
				return "hamo3";
			}
			L1Quest quest = pc.getQuest();
			int questStep = quest.getStep(L1Quest.QUEST_HAMO);
			if (!pc.getInventory().checkItem(820000) && questStep != L1Quest.QUEST_END) {
				pc.getQuest().setEnd(L1Quest.QUEST_HAMO); 
				pc.getInventory().storeItem(820000, 1);//햄의주머니
				return StringUtil.EmptyString;
			}
			return "hamo1";
		}
		return null;
	}
}

