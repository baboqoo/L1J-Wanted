package l1j.server.server.model.npc.action.id;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;

public class AshaAction implements L1NpcIdAction {// 환술사 아샤
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new AshaAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private AshaAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		if (s.equalsIgnoreCase("teleportURL") && pc.isIllusionist()) {
			return "asha3";
		}
		return null;
	}
}

