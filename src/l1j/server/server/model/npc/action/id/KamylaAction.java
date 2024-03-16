package l1j.server.server.model.npc.action.id;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Quest;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;

public class KamylaAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new KamylaAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private KamylaAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		String htmlid = null;
		if (s.equalsIgnoreCase("a")) {
			htmlid = "kamyla7";
			pc.getQuest().setStep(L1Quest.QUEST_KAMYLA, 1);
		} else if (s.equalsIgnoreCase("c")) {
			htmlid = "kamyla10";
			pc.getInventory().consumeItem(40644, 1);
			pc.getQuest().setStep(L1Quest.QUEST_KAMYLA, 3);
		} else if (s.equalsIgnoreCase("e")) {
			htmlid = "kamyla13";
			pc.getInventory().consumeItem(40630, 1);
			pc.getQuest().setStep(L1Quest.QUEST_KAMYLA, 4);
		} else if (s.equalsIgnoreCase("i")) {
			htmlid = "kamyla25";
		} else if (s.equalsIgnoreCase("b") && pc.getQuest().getStep(L1Quest.QUEST_KAMYLA) == 1) { // 카 미라(흐랑코의 미궁)
			pc.getTeleport().start(32679, 32742, (short) 482, 5, true);
		} else if (s.equalsIgnoreCase("d") && pc.getQuest().getStep(L1Quest.QUEST_KAMYLA) == 3) { // 카 미라(디에고가 닫힌 뇌)
			pc.getTeleport().start(32736, 32800, (short) 483, 5, true);
		} else if (s.equalsIgnoreCase("f") && pc.getQuest().getStep(L1Quest.QUEST_KAMYLA) == 4) { // 카 미라(호세 지하소굴)
			pc.getTeleport().start(32746, 32807, (short) 484, 5, true);
		}
		return htmlid;
	}
}

