package l1j.server.server.model.npc.action.html;

import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.npc.L1NpcHtmlAction;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.utils.StringUtil;

public class PkAction implements L1NpcHtmlAction {
	private static class newInstance {
		public static final L1NpcHtmlAction INSTANCE = new PkAction();
	}
	public static L1NpcHtmlAction getInstance(){
		return newInstance.INSTANCE;
	}
	private PkAction(){}

	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		if (pc.getAlignment() < 30000) {
			pc.sendPackets(L1ServerMessage.sm559);
		} else if (pc.getPKcount() < 5) {
			pc.sendPackets(L1ServerMessage.sm560);
		} else {
			if (pc.getInventory().consumeItem(L1ItemId.ADENA, 700000)) {
				pc.setPKcount(pc.getPKcount() - 5);
				pc.sendPackets(new S_ServerMessage(561, String.valueOf(pc.getPKcount())), true);
			} else {
				pc.sendPackets(L1ServerMessage.sm189);
			}
		}
		return StringUtil.EmptyString;
	}
}

