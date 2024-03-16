package l1j.server.server.model.npc.action.id;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;

public class MyAction implements L1NpcIdAction {// 에마이
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new MyAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private MyAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		if (s.equalsIgnoreCase("A")) { // 곰인형
			if(!pc.getInventory().consumeItem(41093, 1))return "my4";
			pc.getInventory().storeItem(41097, 1);
			return "my5";
		} else if (s.equalsIgnoreCase("B")) { // 향수
			if(!pc.getInventory().consumeItem(41094, 1))return "my4";
			pc.getInventory().storeItem(41918, 1);
			return "my6";
		} else if (s.equalsIgnoreCase("C")) { // 드레스
			if(!pc.getInventory().consumeItem(41095, 1))return "my4";
			pc.getInventory().storeItem(41919, 1);
			return "my7";
		} else if (s.equalsIgnoreCase("D")) { // 반지
			if(!pc.getInventory().consumeItem(41096, 1))return "my4";
			pc.getInventory().storeItem(41920, 1);
			return "my8";
		}
		return null;
	}
}

