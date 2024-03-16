package l1j.server.server.model.shop;

import java.util.ArrayList;

import l1j.server.server.datatables.AdenShopTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.warehouse.PackageWarehouse;
import l1j.server.server.model.warehouse.WarehouseManager;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1AdenShopItem;
import l1j.server.server.templates.L1Item;

public class L1AdenShop {

	private ArrayList<Item> buylist = new ArrayList<Item>();
	private boolean bugok = false;

	public L1AdenShop() {
	}

	private int _totalPrice = 0;

	public void add(int id, int count) {
		try {
			if (_totalPrice < 0)
				return;
			L1AdenShopItem item = AdenShopTable.getInstance().get(id);
			if (item == null)
				return;
			int price = item.getPrice();
			if (price == 0)
				return;
			_totalPrice += price * count;
			if (_totalPrice < 0 || _totalPrice >= 1000000000) {
				bugok = true;
				return;
			}
			if (count <= 0 || count > 50) {
				bugok = true;
				return;
			}
			Item listitem = new Item();
			listitem.itemid = id;
			listitem.count = count * (item.getPackCount() > 0 ? item.getPackCount() : 1);
			buylist.add(listitem);
		} catch (Exception e) {

		}
	}

	public boolean BugOk() {
		// TODO 자동 생성된 메소드 스텁
		return bugok;
	}

	public boolean commit(L1PcInstance pc) {
		if (pc.getNcoin() < _totalPrice) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("N코인이 부족합니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1146), true), true);
			return false;
		}

		try {
			pc.addNcoin(-_totalPrice);
			pc.getAccount().updateNcoin();
		} catch (Exception e) {}
		
		PackageWarehouse pwh = WarehouseManager.getInstance().getPackageWarehouse(pc.getAccountName());
		if (pwh == null)
			return false;
		for (Item listitem : buylist) {
			if (listitem.itemid == 0 || listitem.count == 0)
				continue;
			L1Item tempItem = ItemTable.getInstance().getTemplate(listitem.itemid);
			if (tempItem.isMerge()) {
				L1ItemInstance item = ItemTable.getInstance().createItem(listitem.itemid);
				item.setIdentified(true);
				item.setEnchantLevel(0);
				item.setCount(listitem.count);

				pwh.storeTradeItem(item);
			} else {
				L1ItemInstance item = null;
				int createCount;
				for (createCount = 0; createCount < listitem.count; createCount++) {
					item = ItemTable.getInstance().createItem(listitem.itemid);
					item.setIdentified(true);
					item.setEnchantLevel(0);
					pwh.storeTradeItem(item);
				}
			}
		}
		return true;
	}

	class Item {
		public int itemid = 0;
		public int count = 0;
	}

}


