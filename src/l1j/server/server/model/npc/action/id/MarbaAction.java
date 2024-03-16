package l1j.server.server.model.npc.action.id;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.serverpackets.message.S_ServerMessage;

public class MarbaAction implements L1NpcIdAction {// 마르바
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new MarbaAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private MarbaAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		String htmlid = null;
		L1NpcInstance npc = (L1NpcInstance) obj;
		if (pc.getInventory().checkItem(40665)) {
			htmlid = "marba17";
			if (s.equalsIgnoreCase("B")) {
				htmlid = "marba7";
				if (pc.getInventory().checkItem(214) && pc.getInventory().checkItem(20389) && pc.getInventory().checkItem(20393) && pc.getInventory().checkItem(20401)
						&& pc.getInventory().checkItem(20406) && pc.getInventory().checkItem(20409)) {
					htmlid = "marba15";
				}
			}
		} else if (s.equalsIgnoreCase("A")) {
			if (pc.getInventory().checkItem(40637)) {
				htmlid = "marba20";
			} else {
				L1ItemInstance item = pc.getInventory().storeItem(40637, 1);
				pc.sendPackets(new S_ServerMessage(143, npc.getNpcTemplate().getDesc(), item.getItem().getDesc()), true);
				htmlid = "marba6";
			}
		}
		return htmlid;
	}
}

