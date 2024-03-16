package l1j.server.server.model.npc.action.id;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1TownLocation;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;
import l1j.server.server.utils.StringUtil;

public class GtwInterTelAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new GtwInterTelAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private GtwInterTelAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		if (s.equalsIgnoreCase("a")) {
			if (pc._occupyPenaltyTimer != null) {
				pc._occupyPenaltyTimer.cancel();
				pc._occupyPenaltyTimer = null;
				pc.sendPackets(S_SpellBuffNoti.PENALTY_OFF);
			}
			int[] loc = L1TownLocation.getGetBackLoc(pc.getMapId() == L1TownLocation.MAP_WOLRDWAR_HEINE_TOWER ? L1TownLocation.TOWNID_HEINE : L1TownLocation.TOWNID_WINDAWOOD);
            pc.getTeleport().start(loc[0], loc[1], (short) loc[2], pc.getMoveState().getHeading(), true, true);
            return StringUtil.EmptyString;								
		}
		return null;
	}
}

