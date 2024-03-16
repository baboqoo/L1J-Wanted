package l1j.server.server.model.npc.action.html;

import java.util.Calendar;
import java.util.TimeZone;

import l1j.server.Config;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.datatables.HouseTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.npc.L1NpcHtmlAction;
import l1j.server.server.templates.L1House;
import l1j.server.server.utils.StringUtil;

public class PayfeeAction implements L1NpcHtmlAction {
	private static class newInstance {
		public static final L1NpcHtmlAction INSTANCE = new PayfeeAction();
	}
	public static L1NpcHtmlAction getInstance(){
		return newInstance.INSTANCE;
	}
	private PayfeeAction(){}

	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		houseTaxPayment(pc, npcId);
		return StringUtil.EmptyString;
	}
	
	private void houseTaxPayment(L1PcInstance pc, int npcId) {
		L1Clan clan = pc.getClan();
		if (clan != null) {
			int houseId = clan.getHouseId();
			if (houseId != 0) {
				L1House house = HouseTable.getInstance().getHouseTable(houseId);
				if (npcId == house.getKeeperId()) {
					TimeZone tz = TimeZone.getTimeZone(Config.SERVER.TIME_ZONE);
					Calendar checkCal = Calendar.getInstance(tz);
					checkCal.add(Calendar.DATE, 3);
					checkCal.set(Calendar.MINUTE, 0);
					checkCal.set(Calendar.SECOND, 0);

					if (house.getTaxDeadline().after(checkCal)) {
						pc.sendPackets(L1SystemMessage.HOUSE_TAX_EMPTY);
					} else if (pc.getInventory().consumeItem(L1ItemId.ADENA, 2000)) {
						Calendar cal = Calendar.getInstance(tz);
						cal.add(Calendar.DATE, Config.PLEDGE.HOUSE_TAX_INTERVAL_DAY);
						cal.set(Calendar.MINUTE, 0);
						cal.set(Calendar.SECOND, 0);
						house.setTaxDeadline(cal);
						HouseTable.getInstance().updateHouse(house);
					} else {
						pc.sendPackets(L1ServerMessage.sm189);
					}
				}
			}
		}
	}
}

