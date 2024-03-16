package l1j.server.server.model.npc.action.id;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;

public class IcqwandAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new IcqwandAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private IcqwandAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		if (s.equalsIgnoreCase("a")) {
			if (pc.getInventory().checkItem(30055, 1)) { // 폭풍의 막대
				return "icqwand4";
			}
			pc.getInventory().createItem(((L1NpcInstance) obj).getNpcTemplate().getDesc(), 30055, 1, 0);
			return "icqwand2";
		}
		return null;
	}
}

