package l1j.server.server.model.npc.action.id;

import l1j.server.server.controller.CrockController;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;

public class TikalgateAction implements L1NpcIdAction {// 티칼 문지기
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new TikalgateAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private TikalgateAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		if (s.equalsIgnoreCase("e") && CrockController.getInstance().isBoss()){
			if (pc.getInventory().consumeItem(500210)) {
				CrockController.getInstance().add(pc);
				pc.getTeleport().start(32732, 32862, (short) 784, 4, false);
			}
		}
		return null;
	}
}

