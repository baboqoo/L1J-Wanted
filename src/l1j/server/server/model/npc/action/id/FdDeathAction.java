package l1j.server.server.model.npc.action.id;

import l1j.server.IndunSystem.valakasroom.ValakasRoomCreator;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;

public class FdDeathAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new FdDeathAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private FdDeathAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		return zinDeathKnight(pc, s);
	}
	
	private String zinDeathKnight(L1PcInstance pc, String s){
		if (s.equalsIgnoreCase("enter")) {
			if (pc.getLevel() < 60) {
				return "fd_death2";
			}
			if (!pc.getInventory().checkItem(203003, 1)) {// 데스나이트의 불검:진
				pc.getInventory().storeItem(203003, 1);// 데스나이트의 불검:진
				ValakasRoomCreator.getInstance().startRaid(pc);
			}
		}
		return null;
	}
}

