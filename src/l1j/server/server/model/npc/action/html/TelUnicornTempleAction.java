package l1j.server.server.model.npc.action.html;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcHtmlAction;

public class TelUnicornTempleAction implements L1NpcHtmlAction {
	private static class newInstance {
		public static final L1NpcHtmlAction INSTANCE = new TelUnicornTempleAction();
	}
	public static L1NpcHtmlAction getInstance(){
		return newInstance.INSTANCE;
	}
	private TelUnicornTempleAction(){}

	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		String htmlid = null;
		if (pc.getInventory().consumeItem(810000, 1)) {
			pc.getTeleport().start(32800, 32798, (short) 1935, 2, true);
		} else {
			htmlid = "edlen4";
		}
		return htmlid;
	}
}

