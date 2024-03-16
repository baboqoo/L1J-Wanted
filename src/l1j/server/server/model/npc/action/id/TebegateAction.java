package l1j.server.server.model.npc.action.id;

import l1j.server.server.controller.CrockController;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;

public class TebegateAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new TebegateAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private TebegateAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		if (s.equalsIgnoreCase("e") && CrockController.getInstance().isBoss() && CrockController.getInstance().add(pc) && pc.getInventory().consumeItem(100036, 1)) {
			pc.getTeleport().start(32735, 32831, (short) 782, 5, true);
		}
		return null;
	}
}

