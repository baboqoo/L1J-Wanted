package l1j.server.server.model.npc.action.id;

import l1j.server.Config;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.utils.StringUtil;

public class WcdEnterAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new WcdEnterAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private WcdEnterAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		if(s.equalsIgnoreCase("99")){
			L1Location loc = new L1Location(32844, 32824, pc.getMapId() + (Config.DUNGEON.ANT_QUEEN_INCLUDE ? 10 : (pc.isLancer() ? 1 : 10))).randomLocation(5, true);
			pc.getTeleport().start(loc, pc.getMoveState().getHeading(), true);
			loc = null;
		}
		return StringUtil.EmptyString;
	}
	
}

