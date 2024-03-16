package l1j.server.server.model.npc.action.html;

import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcHtmlAction;
import l1j.server.server.serverpackets.warehouse.S_PledgeWarehouseHistory;

public class HistoryAction implements L1NpcHtmlAction {
	
	private static class newInstance {
		public static final L1NpcHtmlAction INSTANCE = new HistoryAction();
	}
	public static L1NpcHtmlAction getInstance(){
		return newInstance.INSTANCE;
	}
	private HistoryAction(){}

	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		if (pc.getClanid() > 0) {
			pc.sendPackets(new S_PledgeWarehouseHistory(pc), true);
		} else {
        	pc.sendPackets(L1ServerMessage.sm1064);
        }
		return null;
	}
}

