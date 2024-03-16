package l1j.server.server.model.npc.action.id;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;

public class JpAction implements L1NpcIdAction {// 제이프
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new JpAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private JpAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		return jaype(pc, s);
	}
	
	private String jaype(L1PcInstance pc, String s){
		int consumeCount = 0, createItemId = 0;
		if (s.equalsIgnoreCase("A")) { // 꿈꾸는 곰인형
			consumeCount = 1000;	createItemId = 41093;
		} else if (s.equalsIgnoreCase("B")) { // 향수
			consumeCount = 5000;	createItemId = 41094;
		} else if (s.equalsIgnoreCase("C")) { // 드레스
			consumeCount = 10000;	createItemId = 41095;
		} else if (s.equalsIgnoreCase("D")) { // 반지
			consumeCount = 100000;	createItemId = 41096;
		} else if (s.equalsIgnoreCase("E")) { // 위인전
			consumeCount = 1000;	createItemId = 41098;
		} else if (s.equalsIgnoreCase("F")) { // 세련된 모자
			consumeCount = 5000;	createItemId = 41099;
		} else if (s.equalsIgnoreCase("G")) { // 최고급 와인
			consumeCount = 10000;	createItemId = 41100;
		} else if (s.equalsIgnoreCase("H")) { // 알 수 없는 열쇠
			consumeCount = 100000;	createItemId = 41101;
		}
		if (!pc.getInventory().consumeItem(49026, consumeCount)) {
			return "jp5";
		}
		pc.getInventory().storeItem(createItemId, 1);
		return createItemId >= 41093 && createItemId <= 41096 ? "jp6" : "jp8";
	}
}

