package l1j.server.server.model.npc.action.html;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcHtmlAction;

public class FixAction implements L1NpcHtmlAction {
	private static class newInstance {
		public static final L1NpcHtmlAction INSTANCE = new FixAction();
	}
	public static L1NpcHtmlAction getInstance(){
		return newInstance.INSTANCE;
	}
	private FixAction(){}

	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		return null;
	}
}

