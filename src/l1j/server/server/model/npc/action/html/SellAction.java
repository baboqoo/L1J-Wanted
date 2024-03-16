package l1j.server.server.model.npc.action.html;

import java.util.List;

import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.datatables.HouseTable;
import l1j.server.server.datatables.ShopTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcHtmlAction;
import l1j.server.server.model.shop.L1AssessedItem;
import l1j.server.server.model.shop.L1Shop;
import l1j.server.server.serverpackets.S_NoSell;
import l1j.server.server.serverpackets.S_SellHouse;
import l1j.server.server.serverpackets.S_ShopBuyList;
import l1j.server.server.templates.L1House;
import l1j.server.server.utils.StringUtil;

public class SellAction implements L1NpcHtmlAction {
	
	private static class newInstance {
		public static final L1NpcHtmlAction INSTANCE = new SellAction();
	}
	public static L1NpcHtmlAction getInstance(){
		return newInstance.INSTANCE;
	}
	private SellAction(){}

	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		if (npcId == 70523 || npcId == 70805) {
			return "ladar2";
		}
		if (npcId == 70537 || npcId == 70807) {
			return "farlin2";
		}
		if (npcId == 70525 || npcId == 70804) {
			return "lien2";
		}
		if (npcId == 50527 || npcId == 50505 || npcId == 50519 || npcId == 50545 || npcId == 50531 || npcId == 50529 || npcId == 50516 || npcId == 50538
				|| npcId == 50518 || npcId == 50509 || npcId == 50536 || npcId == 50520 || npcId == 50543 || npcId == 50526 || npcId == 50512 || npcId == 50510
				|| npcId == 50504 || npcId == 50525 || npcId == 50534 || npcId == 50540 || npcId == 50515 || npcId == 50513 || npcId == 50528 || npcId == 50533
				|| npcId == 50542 || npcId == 50511 || npcId == 50501 || npcId == 50503 || npcId == 50508 || npcId == 50514 || npcId == 50532 || npcId == 50544
				|| npcId == 50524 || npcId == 50535 || npcId == 50521 || npcId == 50517 || npcId == 50537 || npcId == 50539 || npcId == 50507 || npcId == 50530
				|| npcId == 50502 || npcId == 50506 || npcId == 50522 || npcId == 50541 || npcId == 50523 || npcId == 50620 || npcId == 50623 || npcId == 50619
				|| npcId == 50621 || npcId == 50622 || npcId == 50624 || npcId == 50617 || npcId == 50614 || npcId == 50618 || npcId == 50616 || npcId == 50615
				|| npcId == 50626 || npcId == 50627 || npcId == 50628 || npcId == 50629 || npcId == 50630 || npcId == 50631) {
			String sellHouseMessage = houseSell(pc, obj.getId(), npcId);
			if (sellHouseMessage != null) {
				return sellHouseMessage;
			}
		} else {
			L1Object object = L1World.getInstance().findObject(obj.getId());
			if (!(object instanceof L1NpcInstance)) {
				return null;
			}
			L1NpcInstance npc = (L1NpcInstance) object;
			L1Shop shop = ShopTable.getInstance().get(npcId);
			if (shop == null) {
				pc.sendPackets(new S_NoSell(npc), true);
				return null;
			}
			List<L1AssessedItem> assessedItems = shop.assessItems(pc.getInventory());
			if (assessedItems.isEmpty()) {
				pc.sendPackets(new S_NoSell(npc), true);
				return null;
			}
			pc.sendPackets(new S_ShopBuyList(obj.getId(), pc), true);
		}
		return null;
	}
	
	String houseSell(L1PcInstance pc, int objectId, int npcId) {
		L1Clan clan = pc.getClan();
		if (clan == null) {
			return StringUtil.EmptyString;
		}
		int houseId = clan.getHouseId();
		if (houseId == 0) {
			return StringUtil.EmptyString;
		}
		L1House house = HouseTable.getInstance().getHouseTable(houseId);
		if (npcId != house.getKeeperId()) {
			return StringUtil.EmptyString;
		}
		if (!pc.isCrown()) {
			pc.sendPackets(L1ServerMessage.sm518);
			return StringUtil.EmptyString;
		}
		if (pc.getId() != clan.getLeaderId()) {
			pc.sendPackets(L1ServerMessage.sm518);
			return StringUtil.EmptyString;
		}
		if (house.isOnSale()) {
			return "agonsale";
		}
		pc.sendPackets(new S_SellHouse(objectId, String.valueOf(houseId)));
		return null;
	}

}

