package l1j.server.server.model.npc.action.html;

import l1j.server.server.datatables.DoorSpawnTable;
import l1j.server.server.datatables.HouseTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcHtmlAction;
import l1j.server.server.templates.L1House;
import l1j.server.server.utils.StringUtil;

public class OpenAction implements L1NpcHtmlAction {
	
	private static class newInstance {
		public static final L1NpcHtmlAction INSTANCE = new OpenAction();
	}
	public static L1NpcHtmlAction getInstance(){
		return newInstance.INSTANCE;
	}
	private OpenAction(){}

	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		houseDoorAction(pc, npcId, s);
		return StringUtil.EmptyString;
	}
	
	private void houseDoorAction(L1PcInstance pc, int npcId, String s) {
		L1Clan clan = pc.getClan();
		if (clan != null) {
			int houseId = clan.getHouseId();
			if (houseId != 0) {
				L1House house = HouseTable.getInstance().getHouseTable(houseId);
				int keeperId = house.getKeeperId();
				if (npcId == keeperId) {
					L1DoorInstance door1 = null;
					L1DoorInstance door2 = null;
					L1DoorInstance door3 = null;
					L1DoorInstance door4 = null;
					for (L1DoorInstance door : DoorSpawnTable.getInstance().getDoorList()) {
						if (door.getKeeperId() == keeperId) {
							if (door1 == null) {
								door1 = door;
								continue;
							}
							if (door2 == null) {
								door2 = door;
								continue;
							}
							if (door3 == null) {
								door3 = door;
								continue;
							}
							if (door4 == null) {
								door4 = door;
								break;
							}
						}
					}
					if (door1 != null) {
						action(door1, s);
					}
					if (door2 != null) {
						action(door2, s);
					}
					if (door3 != null) {
						action(door3, s);
					}
					if (door4 != null) {
						action(door4, s);
					}
				}
			}
		}
	}
	
	void action(L1DoorInstance door, String request) {
		if (request.equalsIgnoreCase("open")) {
			door.open();
		} else if (request.equalsIgnoreCase("close")) {
			door.close();
		}
	}
	
}

