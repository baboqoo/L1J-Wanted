package l1j.server.server.model.npc.action.id;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Quest;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.utils.StringUtil;

public class JcrystallAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new JcrystallAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private JcrystallAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		String htmlid = null;
		if (s.equalsIgnoreCase("a")) {
			htmlid = StringUtil.EmptyString;
			pc.getInventory().consumeItem(40654, 1);
			pc.getQuest().setStep(L1Quest.QUEST_CRYSTAL, L1Quest.QUEST_END);
			pc.getTeleport().start(32744, 32927, (short) 483, 4, true);
		}
		return htmlid;
	}
}

