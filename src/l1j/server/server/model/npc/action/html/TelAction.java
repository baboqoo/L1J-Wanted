package l1j.server.server.model.npc.action.html;

import l1j.server.server.datatables.HouseTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1HouseLocation;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcHtmlAction;
import l1j.server.server.templates.L1House;
import l1j.server.server.utils.StringUtil;

public class TelAction implements L1NpcHtmlAction {
	private static class newInstance {
		public static final L1NpcHtmlAction INSTANCE = new TelAction();
	}
	public static L1NpcHtmlAction getInstance(){
		return newInstance.INSTANCE;
	}
	private TelAction(){}

	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		L1Clan clan = pc.getClan();
		if (clan != null) {
			int houseId = clan.getHouseId();
			if (houseId != 0) {
				L1House house = HouseTable.getInstance().getHouseTable(houseId);
				int keeperId = house.getKeeperId();
				if (npcId == keeperId) {
					int[] loc = new int[3];
					if (s.equalsIgnoreCase("tel0")) {
						loc = L1HouseLocation.getHouseTeleportLoc(houseId, 0);
					} else if (s.equalsIgnoreCase("tel1")) {
						loc = L1HouseLocation.getHouseTeleportLoc(houseId, 1);
					} else if (s.equalsIgnoreCase("tel2")) {
						loc = L1HouseLocation.getHouseTeleportLoc(houseId, 2);
					} else if (s.equalsIgnoreCase("tel3")) {
						loc = L1HouseLocation.getHouseTeleportLoc(houseId, 3);
					}
					pc.getTeleport().start(loc[0], loc[1], (short) loc[2], 5, true);
				}
			}
		}
		return StringUtil.EmptyString;
	}
}

