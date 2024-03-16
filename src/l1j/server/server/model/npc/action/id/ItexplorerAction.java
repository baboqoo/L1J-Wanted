package l1j.server.server.model.npc.action.id;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.serverpackets.message.S_ServerMessage;

public class ItexplorerAction implements L1NpcIdAction {// 티칼 탐험가(달력주는애)
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new ItexplorerAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private ItexplorerAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		L1NpcInstance npc = (L1NpcInstance) obj;
		if (s.equalsIgnoreCase("1") && !pc.getInventory().checkItem(500211, 1)) {
			L1ItemInstance item = pc.getInventory().storeItem(500211, 1);
			//String itemName = item.getItem().getDescKr();
			String itemName = item.getItem().getDesc();
			pc.sendPackets(new S_ServerMessage(143, npc.getNpcTemplate().getDesc(), itemName), true);
		}
		return null;
	}
}

