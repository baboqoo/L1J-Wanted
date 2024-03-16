package l1j.server.server.model.npc.action.id;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.npc.L1NpcIdAction;

public class RabbitaAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new RabbitaAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private RabbitaAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		if (s.equalsIgnoreCase("0")) {
			if (pc.getLevel() > 30) {
				if (!pc.getInventory().checkItem(22253, 1)) {
					if (pc.getInventory().consumeItem(L1ItemId.ADENA, 5000)) {
						pc.getInventory().storeItem(22253, 1);
						return "rabbita5";
					}
					return "rabbita4";
				}
				return "rabbita3";
			}
			return "rabbita2";
		}
		return null;
	}
}

