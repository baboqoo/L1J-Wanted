package l1j.server.server.model.npc.action.html;

import l1j.server.server.datatables.HouseTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcHtmlAction;
import l1j.server.server.serverpackets.message.S_MessageYN;
import l1j.server.server.templates.L1House;
import l1j.server.server.utils.StringUtil;

public class NameAction implements L1NpcHtmlAction {
	private static class newInstance {
		public static final L1NpcHtmlAction INSTANCE = new NameAction();
	}
	public static L1NpcHtmlAction getInstance(){
		return newInstance.INSTANCE;
	}
	private NameAction(){}

	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		if (npcId == 11090) {
			return changeShelterOwnerYn(pc);
		}
		L1Clan clan = pc.getClan();
		if (clan != null) {
			int houseId = clan.getHouseId();
			if (houseId != 0) {
				L1House house = HouseTable.getInstance().getHouseTable(houseId);
				int keeperId = house.getKeeperId();
				if (npcId == keeperId) {
					pc.setTempID(houseId);
					pc.sendPackets(S_MessageYN.HOUSE_NAME_CHANGE_YN);
				}
			}
		}
		return StringUtil.EmptyString;
	}
	
	/**
	 * 영광의 아지트 양도
	 * @param pc
	 * @return html
	 */
	String changeShelterOwnerYn(L1PcInstance pc) {
		pc.sendPackets(S_MessageYN.SHELTER_OWNER_CHANGE_YN);
		return StringUtil.EmptyString;
	}
}

