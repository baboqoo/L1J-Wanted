package l1j.server.server.model.npc.action.html;

import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.datatables.HouseTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.npc.L1NpcHtmlAction;
import l1j.server.server.templates.L1House;
import l1j.server.server.utils.StringUtil;

public class UpgradeAction implements L1NpcHtmlAction {
	private static class newInstance {
		public static final L1NpcHtmlAction INSTANCE = new UpgradeAction();
	}
	public static L1NpcHtmlAction getInstance(){
		return newInstance.INSTANCE;
	}
	private UpgradeAction(){}

	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		L1Clan clan = pc.getClan();
		if (clan != null) {
			int houseId = clan.getHouseId();
			if (houseId != 0) {
				L1House house = HouseTable.getInstance().getHouseTable(houseId);
				int keeperId = house.getKeeperId();
				if (npcId == keeperId) {
					if (pc.isCrown() && pc.getId() == clan.getLeaderId()) {
						if (house.isPurchaseBasement()) {
							pc.sendPackets(L1ServerMessage.sm1135);
						} else {
							if (pc.getInventory().consumeItem(L1ItemId.ADENA, 5000000)) {
								house.setPurchaseBasement(true);
								HouseTable.getInstance().updateHouse(house);
								pc.sendPackets(L1ServerMessage.sm1099);
							} else {
								pc.sendPackets(L1ServerMessage.sm189);
							}
						}
					} else {
						pc.sendPackets(L1ServerMessage.sm518);
					}
				}
			}
		}
		return StringUtil.EmptyString;
	}
}

