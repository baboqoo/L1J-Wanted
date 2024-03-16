package l1j.server.server.model.npc.action.id;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;

public class TutorrwAction implements L1NpcIdAction {// 초보자 도우미
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new TutorrwAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private TutorrwAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		String htmlid = null;
		L1NpcInstance npc = (L1NpcInstance) obj;
		String desc = npc.getNpcTemplate().getDesc();
		if (s.equals("A")) { // 상아탑 방어구가 필요합니다.
			if (pc.isKnight() || pc.isCrown() || pc.isDarkelf() || pc.isDragonknight() || pc.isFencer() || pc.isLancer()) {
				int check = 0;
				int[] item = new int[] { 22300, 22301, 22302, 22303, 22304 };
				for (int i = 0; i < item.length; i++) {
					if (!pc.getInventory().checkItem(item[i], 1)) {
						pc.getInventory().createItem(desc, item[i], 1, 0);
						check = 1;
					}
				}
				htmlid = check == 1 ? "tutorrw5" : "tutorrw6";
			} else {
				int check = 0;
				int[] item = new int[] { 22306, 22307, 22308, 22309, 22310 };
				for (int i = 0; i < item.length; i++) {
					if (!pc.getInventory().checkItem(item[i], 1)) {
						pc.getInventory().createItem(desc, item[i], 1, 0);
						check = 1;
					}
				}
				htmlid = check == 1 ? "tutorrw5" : "tutorrw6";
			}
		}
		return htmlid;
	}
}

