package l1j.server.server.model.npc.action.id;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.utils.StringUtil;

public class HyosueAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new HyosueAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private HyosueAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		return hoshu(pc, s);
	}
	
	private String hoshu(L1PcInstance pc, String s) {
		if (s.equalsIgnoreCase("a")) {// 관찰한다.
			if (pc.getInventory().consumeItem(L1ItemId.ADENA, 2000)) {
				ghostTeleport(pc, 32789, 33186, (short)4, 30);
				return StringUtil.EmptyString;
			}
			return "hyosue1";
		} else if (s.equalsIgnoreCase("1")) {// 샌드웜 영역 이동
			if (pc.getInventory().consumeItem(L1ItemId.ADENA, 10000)) {
				pc.getTeleport().start(32789, 33186, (short) 4, 5, true);
				return StringUtil.EmptyString;
			}
			return "hyosue1";
		}
		return null;
	}
	
	private void ghostTeleport(L1PcInstance pc, int x, int y, short m, int second) {
		try {
			pc.save();
			pc.beginGhost(x, y, (short) m, false, second); //30초
		} catch(Exception e) {}
	}
}

