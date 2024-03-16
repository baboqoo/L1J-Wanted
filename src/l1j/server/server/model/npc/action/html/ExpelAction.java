package l1j.server.server.model.npc.action.html;

import l1j.server.server.datatables.HouseTable;
import l1j.server.server.model.L1HouseLocation;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1TownLocation;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcHtmlAction;
import l1j.server.server.templates.L1House;
import l1j.server.server.utils.StringUtil;

public class ExpelAction implements L1NpcHtmlAction {
	
	private static class newInstance {
		public static final L1NpcHtmlAction INSTANCE = new ExpelAction();
	}
	public static L1NpcHtmlAction getInstance(){
		return newInstance.INSTANCE;
	}
	private ExpelAction(){}

	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		houseOtherPcOut(pc, npcId);
		return StringUtil.EmptyString;
	}
	
	void houseOtherPcOut(L1PcInstance clanPc, int keeperId) {
		if (keeperId == 11090) {
			expelSoloAgit(clanPc);
			return;
		}
		int houseId = 0;
		for (L1House house : HouseTable.getInstance().getHouseTableList()) {
			if (house.getKeeperId() == keeperId) {
				houseId = house.getHouseId();
			}
		}
		if (houseId == 0) {
			return;
		}
		int[] loc = new int[3];
		L1PcInstance pc = null;
		for (L1Object object : L1World.getInstance().getObject()) {
			if (object instanceof L1PcInstance) {
				pc = (L1PcInstance) object;
				if (L1HouseLocation.isInHouseLoc(houseId, pc.getX(), pc.getY(), pc.getMapId()) && clanPc.getClanid() != pc.getClanid()) {
					loc = L1HouseLocation.getHouseTeleportLoc(houseId, 0);
					if (pc != null) {
						pc.getTeleport().start(loc[0], loc[1], (short) loc[2], 5, true);
					}
				}
			}
		}
	}
	
	/**
	 * 영광의 아지트 외부인 추방
	 * @param pc
	 */
	void expelSoloAgit(L1PcInstance pc) {
		for (L1PcInstance user : L1World.getInstance().getMapPlayer(2237)) {
			if (user == null || user == pc) {
				continue;
			}
			int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_GIRAN);
			user.getTeleport().start(loc[0], loc[1], (short)loc[2], user.getMoveState().getHeading(), true);
		}
	}
}

