package l1j.server.server.model.npc.action.html;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcHtmlAction;
import l1j.server.server.serverpackets.warehouse.S_WareHouseItemListNoti;

public class RetrieveAction implements L1NpcHtmlAction {
	private static class newInstance {
		public static final L1NpcHtmlAction INSTANCE = new RetrieveAction();
	}
	public static L1NpcHtmlAction getInstance(){
		return newInstance.INSTANCE;
	}
	private RetrieveAction(){}
	
	private static final String NONE_VALUE_ACTION = "noitemret";

	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		String htmlid	=	null;
		if (pc.getLevel() >= 5) {
			if (pc.isTwoLogin()) {
				return null;
			}
			L1NpcInstance npc = (L1NpcInstance) obj;
			S_WareHouseItemListNoti rpl = new S_WareHouseItemListNoti(S_WareHouseItemListNoti.eWarehouseType.TRADE_RETRIEVE, npc, pc);
			if (rpl.is_non_value) {
				htmlid = NONE_VALUE_ACTION;
			} else {
				pc.sendPackets(rpl);
			}
			rpl.clear();
			rpl = null;
		}
		return htmlid;
	}
}

