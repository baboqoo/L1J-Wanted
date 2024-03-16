package l1j.server.server.templates;

import l1j.server.common.data.eBloodPledgeRankType;
import l1j.server.server.datatables.ItemTable;

public class L1ShopItem {
	private final int _itemId;
	private final L1Item _item;
	private final int _price;
	private final int _packCount;
	private final int _enchant;
	private final eBloodPledgeRankType _pledge_rank;
	private int _count;
	
	public L1ShopItem(int itemId, int price, int packCount) {
		this(itemId, price, packCount, 0, null);
	}
	
	public L1ShopItem(int itemId, int price, int packCount, int enchant) {
		this(itemId, price, packCount, enchant, null);
	}
	
	public L1ShopItem(int itemId, int price, int packCount, int enchant, eBloodPledgeRankType pledge_rank) {
		_itemId			= itemId;
		_item			= ItemTable.getInstance().getTemplate(itemId);
		if (_item == null) {
			System.out.println(String.format("[L1ShopItem] ITEM_TEMPLATE_EMPTY : ITEMID(%d)", itemId));
		}
		_price			= price;
		_packCount		= packCount;
		_enchant		= enchant;
		_pledge_rank	= pledge_rank;
		_count			= 1;
	}

	public int getItemId() {
		return _itemId;
	}

	public L1Item getItem() {
		return _item;
	}

	public int getPrice() {
		return _price;
	}

	public int getPackCount() {
		return _packCount;
	}
	
	public int getEnchant() {
		return _enchant;
	}
	
	public eBloodPledgeRankType getPledgeRank() {
		return _pledge_rank;
	}

	public int getCount() {
		return _count;
	}

	public void setCount(int val) {
		_count = val;
	}
}


