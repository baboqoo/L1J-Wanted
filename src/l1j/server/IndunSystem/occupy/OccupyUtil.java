package l1j.server.IndunSystem.occupy;

import java.sql.Timestamp;

import l1j.server.IndunSystem.occupy.action.Heine;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1TownLocation;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.message.S_ServerMessage;

public class OccupyUtil {
	public static int getBadgeId(OccupyTeamType teamType){
		if (teamType == null) {
			return 0;
		}
		switch(teamType){
		case RED_KNIGHT:
			return L1ItemId.RED_KNIGHT_BADGE;
		case BLACK_KNIGHT:
			return L1ItemId.BLACK_KNIGHT_BADGE;
		default:
			return L1ItemId.GOLD_KNIGHT_BADGE;
		}
	}
	
	public static int[] getTeamLoc(OccupyHandler handler, OccupyTeamType teamType){
		if (handler == null) {
			System.out.println(String.format("[OccupyUtil] TEAM_LOC_HANDLER_NULL : TEAM_TYPE(%s)", teamType.getDesc()));
			return null;
		}
		boolean handlerType = handler instanceof Heine;
		switch(teamType){
		case RED_KNIGHT:
			return L1TownLocation.getGetBackLoc(handlerType ? L1TownLocation.WOLRDWAR_HEINE_RED : L1TownLocation.WOLRDWAR_WINDAWOOD_RED);
		case BLACK_KNIGHT:
			return L1TownLocation.getGetBackLoc(handlerType ? L1TownLocation.WOLRDWAR_HEINE_BLACK : L1TownLocation.WOLRDWAR_WINDAWOOD_BLACK);
		default:
			return L1TownLocation.getGetBackLoc(handlerType ? L1TownLocation.WOLRDWAR_HEINE_GOLD : L1TownLocation.WOLRDWAR_WINDAWOOD_GOLD);
		}
	}
	
	public static L1ItemInstance reward(L1PcInstance pc, int itemId, int count, int deleteTimer){
		L1PcInventory inv = pc.getInventory();
		if (pc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(itemId), count) != L1Inventory.OK) return null;
		L1ItemInstance item = inv.storeItem(itemId, count);
		if (item == null) {
			System.out.println(String.format("[OccupyUtil] REWARD_ITEM_TEMPLATE_EMPTY : ITEM_ID(%d)", itemId));
			return null;
		}
		pc.sendPackets(new S_ServerMessage(403, String.format("%s (%d)", item.getItem().getDesc(), count)), true);
		if (deleteTimer > 0 && item.getEndTime() == null) {
			item.setEndTime(new Timestamp(System.currentTimeMillis() + (1000L * (long)deleteTimer)));
			item.setIdentified(true);
			inv.updateItem(item, L1PcInventory.COL_REMAINING_TIME);
			inv.saveItem(item, L1PcInventory.COL_REMAINING_TIME);
		}
		return item;
	}
}

