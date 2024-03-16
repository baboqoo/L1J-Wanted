package l1j.server.server.model.npc.action.id;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.serverpackets.S_Alignment;

public class YurisAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new YurisAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private YurisAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		int count = 0;
		if (s.equalsIgnoreCase("0")) {
			count = 1;
		} else if (s.equalsIgnoreCase("1")) {
			count = 3;
		} else if (s.equalsIgnoreCase("2")) {
			count = 5;
		} else if (s.equalsIgnoreCase("3")) {
			count = 10;
		}
		if (count > 0 && pc.getInventory().consumeItem(437008,count)) {
			pc.addAlignment(3000 * count);
			pc.sendPackets(new S_Alignment(pc.getId(), pc.getAlignment()), true);
			pc.send_effect(9009);
			return "yuris2";
		}
		return "yuris3";
	}
}

