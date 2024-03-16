package l1j.server.server.model.npc.action.html;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1TownLocation;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcHtmlAction;

public class TeleportMobjteleAction implements L1NpcHtmlAction {
	private static class newInstance {
		public static final L1NpcHtmlAction INSTANCE = new TeleportMobjteleAction();
	}
	public static L1NpcHtmlAction getInstance(){
		return newInstance.INSTANCE;
	}
	private TeleportMobjteleAction(){}

	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_GIRAN);
		pc.getTeleport().start(loc[0], loc[1], (short) loc[2], pc.getMoveState().getHeading(), true);
		return null;
	}
}

