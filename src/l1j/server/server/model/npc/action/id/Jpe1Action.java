package l1j.server.server.model.npc.action.id;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.utils.StringUtil;

public class Jpe1Action implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new Jpe1Action();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private Jpe1Action(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		String htmlid = null;
		if (s.equalsIgnoreCase("0")) {
			if (!pc.getInventory().checkItem(41209)) {
				final int[] item_ids = { 41209, };
				final int[] item_amounts = { 1, };
				for (int i = 0; i < item_ids.length; i++) {
					L1ItemInstance item = pc.getInventory().storeItem(item_ids[i], item_amounts[i]);
					pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().getDesc(), item.getItem().getDesc()), true);
				}
			}
			htmlid = StringUtil.EmptyString;
		} else if (s.equalsIgnoreCase("1")) {
			if (pc.getInventory().consumeItem(41213, 1)) {
				final int[] item_ids = { 40029, };
				final int[] item_amounts = { 20, };
				L1ItemInstance item = null;
				for (int i = 0; i < item_ids.length; i++) {
					item = pc.getInventory().storeItem(item_ids[i], item_amounts[i]);
					pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().getDesc(), item.getItem().getDesc() + " (" + item_amounts[i] + ")"), true);
				}
				htmlid = StringUtil.EmptyString;
			}
		}
		return htmlid;
	}
}

