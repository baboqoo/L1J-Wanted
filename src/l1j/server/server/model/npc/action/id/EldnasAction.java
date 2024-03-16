package l1j.server.server.model.npc.action.id;

import l1j.server.IndunSystem.valakasroom.ValakasReadyCreator;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;

public class EldnasAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new EldnasAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private EldnasAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		return eldnas(pc, s);
	}
	
	private String eldnas(L1PcInstance pc, String s){
		if (s.equalsIgnoreCase("a")) {
			if (pc.getLevel() < 60) {
				return "eldnas3";
			}
			if (!pc.getInventory().consumeItem(820001, 1)) {
				return "eldnas1";
			}
			ValakasReadyCreator.getInstance().startReady(pc);
		}
		return null;
	}
}

