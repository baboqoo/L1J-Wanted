package l1j.server.server.model.npc.action.html;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcHtmlAction;

public class EnterSellerAction implements L1NpcHtmlAction {
	private static class newInstance {
		public static final L1NpcHtmlAction INSTANCE = new EnterSellerAction();
	}
	public static L1NpcHtmlAction getInstance(){
		return newInstance.INSTANCE;
	}
	private EnterSellerAction(){}

	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		L1PcInstance targetShop = L1World.getInstance().getPlayer(s2);
		if (targetShop != null && targetShop.isPrivateShop() && pc.getMapId() == targetShop.getMapId()) {
			pc.getTeleport().start(targetShop.getX() - 1, targetShop.getY() + 1, targetShop.getMapId(), 1, true);
		}
		return null;
	}
}

