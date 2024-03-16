package l1j.server.server.model.npc.action.html;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcHtmlAction;
import l1j.server.server.serverpackets.S_SoldierGiveList;

public class ArrangeAction implements L1NpcHtmlAction {
	private static class newInstance {
		public static final L1NpcHtmlAction INSTANCE = new ArrangeAction();
	}
	public static L1NpcHtmlAction getInstance(){
		return newInstance.INSTANCE;
	}
	private ArrangeAction(){}

	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		pc.sendPackets(new S_SoldierGiveList(obj.getId(), pc.getClan().getCastleId()), true);
		return null;
	}
}

