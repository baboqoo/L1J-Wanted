package l1j.server.server.model.npc.action.id;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.serverpackets.message.S_ServerMessage;

public class OrcfhuwoomoAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new OrcfhuwoomoAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private OrcfhuwoomoAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		String htmlid = null;
		if (s.equalsIgnoreCase("A")) {
			L1NpcInstance npc = (L1NpcInstance) obj;
			L1ItemInstance item = pc.getInventory().storeItem(41064, 1);
			pc.sendPackets(new S_ServerMessage(143, npc.getNpcTemplate().getDesc(), item.getItem().getDesc()), true);
			htmlid = "orcfhuwoomo4";
		} else if (s.equalsIgnoreCase("Z")) {
			if (pc.getInventory().consumeItem(41064, 1)) {
				htmlid = "orcfhuwoomo6";
			}
		}
		return htmlid;
	}
}

