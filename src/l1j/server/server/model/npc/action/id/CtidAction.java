package l1j.server.server.model.npc.action.id;

import l1j.server.IndunSystem.fantasyisland.FantasyIslandCreator;
import l1j.server.IndunSystem.fantasyisland.FantasylslandType;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;

public class CtidAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new CtidAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private CtidAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		return fantasyLand(pc, s, npcId);
	}
	
	private String fantasyLand(L1PcInstance pc, String s, int npcId){
		if (s.equalsIgnoreCase("enter")) {
			if (npcId == 7200001) {// 중앙사원 문지기
				return excute(pc, FantasylslandType.NORMAL);
			}
			if (npcId == 7200028) {// 유니콘 사원 차원의 문 boost
				return excute(pc, FantasylslandType.BOOST);
			}
		}
		return null;
	}
	
	String excute(L1PcInstance pc, FantasylslandType type) {
		FantasyIslandCreator manager = FantasyIslandCreator.getInstance();
		if (manager.getRaidCount(type) >= 49) {
			return "ctid1";
		}
		manager.create(pc, type);
		return null;
	}
}

