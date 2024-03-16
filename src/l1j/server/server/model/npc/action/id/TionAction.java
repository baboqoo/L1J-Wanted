package l1j.server.server.model.npc.action.id;

import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.serverpackets.message.S_ServerMessage;

public class TionAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new TionAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private TionAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		String htmlid = null;
		if (s.equalsIgnoreCase("A")) {
			if (pc.getQuest().getStep(71198) != 0 || pc.getInventory().checkItem(21059, 1)) {
				return null;
			}
			if (pc.getInventory().consumeItem(41339, 5)) {
				L1ItemInstance item = ItemTable.getInstance().createItem(41340);
				if (item != null) {
					if (pc.getInventory().checkAddItem(item, 1) == 0) {
						pc.getInventory().storeItem(item);
						pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().getDesc(), item.getItem().getDesc()), true);
					}
				}
				pc.getQuest().setStep(71198, 1);
				htmlid = "tion4";
			} else {
				htmlid = "tion9";
			}
		} else if (s.equalsIgnoreCase("B")) {
			if (pc.getQuest().getStep(71198) != 1 || pc.getInventory().checkItem(21059, 1)) {
				return null;
			}
			if (pc.getInventory().consumeItem(41341, 1)) {
				pc.getQuest().setStep(71198, 2);
				htmlid = "tion5";
			} else {
				htmlid = "tion10";
			}
		} else if (s.equalsIgnoreCase("C")) {
			if (pc.getQuest().getStep(71198) != 2 || pc.getInventory().checkItem(21059, 1)) {
				return null;
			}
			if (pc.getInventory().consumeItem(41343, 1)) {
				L1ItemInstance item = ItemTable.getInstance().createItem(21057);
				if (item != null) {
					if (pc.getInventory().checkAddItem(item, 1) == 0) {
						pc.getInventory().storeItem(item);
						pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().getDesc(), item.getItem().getDesc()), true);
					}
				}
				pc.getQuest().setStep(71198, 3);
				htmlid = "tion6";
			} else {
				htmlid = "tion12";
			}
		} else if (s.equalsIgnoreCase("D")) {
			if (pc.getQuest().getStep(71198) != 3 || pc.getInventory().checkItem(21059, 1)) {
				return null;
			}
			if (pc.getInventory().consumeItem(41344, 1)) {
				L1ItemInstance item = ItemTable.getInstance().createItem(21058);
				if (item != null) {
					pc.getInventory().consumeItem(21057, 1);
					if (pc.getInventory().checkAddItem(item, 1) == 0) {
						pc.getInventory().storeItem(item);
						pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().getDesc(), item.getItem().getDesc()), true);
					}
				}
				pc.getQuest().setStep(71198, 4);
				htmlid = "tion7";
			} else {
				htmlid = "tion13";
			}
		} else if (s.equalsIgnoreCase("E")) {
			if (pc.getQuest().getStep(71198) != 4 || pc.getInventory().checkItem(21059, 1)) {
				return null;
			}
			if (pc.getInventory().consumeItem(41345, 1)) {
				L1ItemInstance item = ItemTable.getInstance().createItem(21059);
				if (item != null) {
					pc.getInventory().consumeItem(21058, 1);
					if (pc.getInventory().checkAddItem(item, 1) == 0) {
						pc.getInventory().storeItem(item);
						pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().getDesc(), item.getItem().getDesc()), true);
					}
				}
				pc.getQuest().setStep(71198, 0);
				pc.getQuest().setStep(71199, 0);
				htmlid = "tion8";
			} else {
				htmlid = "tion15";
			}
		}
		return htmlid;
	}
}

