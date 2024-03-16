package l1j.server.server.model.npc.action.id;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;

public class FeaenaAction implements L1NpcIdAction {// 용기사 피에나
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new FeaenaAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private FeaenaAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		if (s.equalsIgnoreCase("teleportURL") && pc.isDragonknight()) {
			return "feaena3";
		}
		return null;
	}
}

