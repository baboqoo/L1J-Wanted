package l1j.server.server.model.npc.action.id;

import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.serverpackets.message.S_ServerMessage;

public class JeronAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new JeronAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private JeronAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		String htmlid = null;
		if (s.equalsIgnoreCase("A")) {
			if (pc.getQuest().getStep(71199) != 0 || pc.getInventory().checkItem(21059, 1)) {
				return null;
			}
			if (pc.getInventory().checkItem(41340, 1)) {
				pc.getQuest().setStep(71199, 1);
				htmlid = "jeron2";
			} else {
				htmlid = "jeron10";
			}
		} else if (s.equalsIgnoreCase("B")) {
			if (pc.getQuest().getStep(71199) != 1 || pc.getInventory().checkItem(21059, 1)) {
				return null;
			}
			if (pc.getInventory().consumeItem(L1ItemId.ADENA, 1000000)) {
				L1ItemInstance item = ItemTable.getInstance().createItem(41341);
				if (item != null) {
					if (pc.getInventory().checkAddItem(item, 1) == 0) {
						pc.getInventory().storeItem(item);
						pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().getDesc(), item.getItem().getDesc()), true);
					}
				}
				pc.getInventory().consumeItem(41340, 1);
				pc.getQuest().setStep(71199, 255);
				htmlid = "jeron6";
			} else {
				htmlid = "jeron8";
			}
		} else if (s.equalsIgnoreCase("C")) {
			if (pc.getQuest().getStep(71199) != 1 || pc.getInventory().checkItem(21059, 1)) {
				return null;
			}
			if (pc.getInventory().consumeItem(41342, 1)) {
				L1ItemInstance item = ItemTable.getInstance().createItem(41341);
				if (item != null) {
					if (pc.getInventory().checkAddItem(item, 1) == 0) {
						pc.getInventory().storeItem(item);
						pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().getDesc(), item.getItem().getDesc()), true);
					}
				}
				pc.getInventory().consumeItem(41340, 1);
				pc.getQuest().setStep(71199, 255);
				htmlid = "jeron5";
			} else {
				htmlid = "jeron9";
			}
		}
		return htmlid;
	}
}

