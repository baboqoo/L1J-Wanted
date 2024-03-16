package l1j.server.server.model.npc.action.id;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.utils.StringUtil;

public class IndunLosusGerengAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new IndunLosusGerengAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private IndunLosusGerengAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		return crocodileGerang(pc, s, obj);
	}
	
	private String crocodileGerang(L1PcInstance pc, String s, L1Object obj){
		if (!pc.getInventory().consumeItem(420113)) {
			return "indun_losus_gerengf-k";
		}
		int itemid = 0;
		if (s.equalsIgnoreCase("a")) {
			itemid = 420100;
		} else if (s.equalsIgnoreCase("b")) {
			itemid = 420101;
		} else if (s.equalsIgnoreCase("c")) {
			itemid = 420102;
		}
		L1ItemInstance item = pc.getInventory().storeItem(itemid, 1);
		pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getDesc(), item.getItem().getDesc()), true);
		return StringUtil.EmptyString;
	}
}

