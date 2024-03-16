package l1j.server.server.model.npc.action.id;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Quest;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;

public class RestaAction implements L1NpcIdAction {// 레스타(해적섬)
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new RestaAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private RestaAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		String htmlid = null;
		if (s.equalsIgnoreCase("A")) {
			htmlid = pc.getQuest().getStep(L1Quest.QUEST_RUDIAN) == L1Quest.QUEST_END ? "resta6" : "resta4";
		} else if (s.equalsIgnoreCase("B")) {
			htmlid = "resta10";
			pc.getQuest().setStep(L1Quest.QUEST_RESTA, 2);
		}
		return htmlid;
	}
}

