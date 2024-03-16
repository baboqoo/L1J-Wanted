package l1j.server.server.model.npc.action.id;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Quest;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;

public class ProkelAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new ProkelAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private ProkelAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		String htmlid = null;
		if (s.equalsIgnoreCase("a")) {
			htmlid = "prokel3";
			pc.getInventory().storeItem(210087, 1);
			pc.getQuest().setStep(L1Quest.QUEST_LEVEL15, 1);
		} else if (s.equalsIgnoreCase("b")) {
			if (pc.getInventory().checkItem(210088) || pc.getInventory().checkItem(210089) || pc.getInventory().checkItem(210090)) {
				htmlid = "prokel5";
				pc.getInventory().consumeItem(210088, 1);
				pc.getInventory().consumeItem(210089, 1);
				pc.getInventory().consumeItem(210090, 1);
				pc.getInventory().storeItem(502, 1);
				pc.getInventory().storeItem(210020, 1);
				pc.getQuest().setStep(L1Quest.QUEST_LEVEL15, 255);
			} else {
				htmlid = "prokel6";
			}
		}
		return htmlid;
	}
}

