package l1j.server.server.model.npc.action.id;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Quest;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;

public class SilreinAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new SilreinAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private SilreinAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		String htmlid = null;
		if (s.equalsIgnoreCase("a")) {
			htmlid = "silrein4";
			pc.getInventory().storeItem(210092, 5);
			pc.getInventory().storeItem(210093, 1);
			pc.getQuest().setStep(L1Quest.QUEST_LEVEL15, 1);
		} else if (s.equalsIgnoreCase("b")) {
			if (pc.getInventory().checkItem(210091, 10) || pc.getInventory().checkItem(40510) || pc.getInventory().checkItem(40511) || pc.getInventory().checkItem(40512) || pc.getInventory().checkItem(41080)) {
				htmlid = "silrein7";
				pc.getInventory().consumeItem(210091, 10);
				pc.getInventory().consumeItem(40510, 1);
				pc.getInventory().consumeItem(40511, 1);
				pc.getInventory().consumeItem(40512, 1);
				pc.getInventory().consumeItem(41080, 1);
				pc.getInventory().storeItem(505, 1);
				pc.getInventory().storeItem(210004, 1);
				pc.getQuest().setStep(L1Quest.QUEST_LEVEL15, 255);
			} else {
				htmlid = "silrein8";
			}
		}
		return htmlid;
	}
}

