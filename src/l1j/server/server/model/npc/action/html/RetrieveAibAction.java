package l1j.server.server.model.npc.action.html;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcHtmlAction;
import l1j.server.server.serverpackets.warehouse.S_WareHouseItemListNoti;

public class RetrieveAibAction implements L1NpcHtmlAction {
	private static class newInstance {
		public static final L1NpcHtmlAction INSTANCE = new RetrieveAibAction();
	}
	public static L1NpcHtmlAction getInstance(){
		return newInstance.INSTANCE;
	}
	private RetrieveAibAction(){}

	private static final String NONE_VALUE_ACTION = "noitemret";
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		String htmlid	=	null;
		if (pc.isTwoLogin()) {
			return null;
		}
		S_WareHouseItemListNoti rpl = new S_WareHouseItemListNoti(S_WareHouseItemListNoti.eWarehouseType.TRADE_RETRIEVE_CONTRACT, null, pc);
		if (rpl.is_non_value) {
			htmlid = NONE_VALUE_ACTION;
		} else {
			pc.sendPackets(rpl);
		}
		rpl.clear();
		rpl = null;
		return htmlid;
	}
}

