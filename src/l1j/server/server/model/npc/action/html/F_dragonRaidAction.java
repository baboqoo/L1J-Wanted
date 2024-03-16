package l1j.server.server.model.npc.action.html;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1MerchantInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcHtmlAction;
import l1j.server.server.utils.StringUtil;

public class F_dragonRaidAction implements L1NpcHtmlAction {
	private static class newInstance {
		public static final L1NpcHtmlAction INSTANCE = new F_dragonRaidAction();
	}
	public static L1NpcHtmlAction getInstance(){
		return newInstance.INSTANCE;
	}
	private F_dragonRaidAction(){}

	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		return dragonRaidEnter(pc, obj);
	}
	
	private synchronized String dragonRaidEnter(L1PcInstance pc, L1Object obj) {
		L1MerchantInstance npc = (L1MerchantInstance) obj;
		switch (npc.getMoveMapId()) {
		case 1180:
			pc.getTeleport().start(32799, 32653, (short) npc.getMoveMapId(), pc.getMoveState().getHeading(), true);
			break;
		case 1181:
			pc.getTeleport().start(32986, 32843, (short) npc.getMoveMapId(), pc.getMoveState().getHeading(), true);
			break;
		case 1182:
			pc.getTeleport().start(32826, 32899, (short) npc.getMoveMapId(), pc.getMoveState().getHeading(), true);
			break;
		case 1183:
			pc.getTeleport().start(32796, 32885, (short) npc.getMoveMapId(), pc.getMoveState().getHeading(), true);
			break;
		}
		return StringUtil.EmptyString;
	}
}

