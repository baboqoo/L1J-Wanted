package l1j.server.server.model.npc.action.id;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.serverpackets.message.S_ServerMessage;

public class JoegolemAction implements L1NpcIdAction {// 조우 티칼
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new JoegolemAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private JoegolemAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		String htmlid = null;
		L1NpcInstance npc = (L1NpcInstance) obj;
		if (s.equalsIgnoreCase("A")) {
			if (pc.getInventory().consumeItem(210081, 100)) {
				L1ItemInstance item = pc.getInventory().storeItem(210082, 1);
				pc.sendPackets(new S_ServerMessage(143, npc.getNpcTemplate().getDesc(), item.getItem().getDesc()), true);
				htmlid = "joegolem18";
			} else {
				htmlid = "joegolem19";
			}
		} else if (s.equalsIgnoreCase("B")) {
			pc.getTeleport().start(34090, 33168, (short) 4, 4, false);
		}
		return htmlid;
	}
}

