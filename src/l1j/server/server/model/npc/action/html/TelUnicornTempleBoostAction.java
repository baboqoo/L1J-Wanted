package l1j.server.server.model.npc.action.html;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcHtmlAction;

public class TelUnicornTempleBoostAction implements L1NpcHtmlAction {
	private static class newInstance {
		public static final L1NpcHtmlAction INSTANCE = new TelUnicornTempleBoostAction();
	}
	public static L1NpcHtmlAction getInstance(){
		return newInstance.INSTANCE;
	}
	private TelUnicornTempleBoostAction(){}

	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		String htmlid = null;
		if (pc.getInventory().consumeItem(810000, 3)) {
			pc.getTeleport().start(32800, 32798, (short) 2935, 2, true);
		} else {
			htmlid = "edlen4";
		}
		return htmlid;
	}
}

