package l1j.server.server.model.npc.action.id;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Quest;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;

public class CadmusAction implements L1NpcIdAction {// 카좀스(해적섬)
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new CadmusAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private CadmusAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		if (s.equalsIgnoreCase("A")) {
			if (pc.getInventory().consumeItem(40647, 3)) {
				pc.getQuest().setStep(L1Quest.QUEST_CADMUS, 2);
				return "cadmus6";
			}
			pc.getQuest().setStep(L1Quest.QUEST_CADMUS, 1);
			return "cadmus5";
		}
		return null;
	}
}

