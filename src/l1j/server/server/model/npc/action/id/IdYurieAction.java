package l1j.server.server.model.npc.action.id;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.serverpackets.message.S_ServerMessage;

public class IdYurieAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new IdYurieAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private IdYurieAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		return yurie(pc, s, obj);
	}
	
	private String yurie(L1PcInstance pc, String s, L1Object obj){
		L1ItemInstance item = null;
		L1NpcInstance npc = (L1NpcInstance) obj;
		String desc = npc.getDesc();
		if (s.equalsIgnoreCase("b")) {// 열쇠 구입
			if (pc.getInventory().checkItem(420093, 1)) {
				return "id_yurie04";
			}
			if (pc.getLevel() < 80) {
				return "id_yurie07";
			}
			if (!pc.getInventory().consumeItem(L1ItemId.ADENA, 4000000)) {
				return "id_yurie05";
			}
			item = pc.getInventory().storeItem(420093, 1);
			pc.sendPackets(new S_ServerMessage(143, desc, item.getItem().getDesc()), true);
			return "id_yurie06";
		}
		return null;
	}
}

