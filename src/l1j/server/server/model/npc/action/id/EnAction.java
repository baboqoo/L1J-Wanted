package l1j.server.server.model.npc.action.id;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.utils.StringUtil;

public class EnAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new EnAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private EnAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		if (s.equalsIgnoreCase("1")) {
			L1PolyMorph.poly(pc, 4002);
			return StringUtil.EmptyString;
		} else if (s.equalsIgnoreCase("2")) {
			L1PolyMorph.poly(pc, 4004);
			return StringUtil.EmptyString;
		} else if (s.equalsIgnoreCase("3")) {
			L1PolyMorph.poly(pc, 4950);
			return StringUtil.EmptyString;
		}
		return null;
	}
}

