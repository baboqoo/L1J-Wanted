package l1j.server.server.model.npc.action.id;

import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.utils.CommonUtil;

public class CamusAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new CamusAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private CamusAction(){}
	
	private static final int[] prismIds = { 210106, 210107, 210108, 210109 };
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		if (s.equalsIgnoreCase("1")) {
			return "camus5";
		}
		if (s.equalsIgnoreCase("2")) {
			if (pc.getInventory().consumeItem(L1ItemId.ADENA, 10000)) {
				int idx = CommonUtil.random(prismIds.length);
				pc.getInventory().storeItem(prismIds[idx], 1);
				L1ItemInstance item = ItemTable.getInstance().createItem(prismIds[idx]);
				item.setCount(1);
				pc.sendPackets(new S_ServerMessage(403, item.getLogNameRef()), true);
				return "camus2";
			}
			return "camus3";
		}
		return null;
	}

}

