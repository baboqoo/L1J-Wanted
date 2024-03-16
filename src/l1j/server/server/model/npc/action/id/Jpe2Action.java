package l1j.server.server.model.npc.action.id;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.serverpackets.message.S_ServerMessage;

public class Jpe2Action implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new Jpe2Action();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private Jpe2Action(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		String htmlid = null;
		if (s.equalsIgnoreCase("0")) {
			final int[] item_ids = { 41225 };
			final int[] item_amounts = { 1 };
			for (int i = 0; i < item_ids.length; i++) {
				L1ItemInstance item = pc.getInventory().storeItem(item_ids[i], item_amounts[i]);
				pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().getDesc(), item.getItem().getDesc()), true);
			}
			htmlid = "jpe0083";
		}
		return htmlid;
	}
}

