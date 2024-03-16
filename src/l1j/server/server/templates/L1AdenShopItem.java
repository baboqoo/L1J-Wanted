package l1j.server.server.templates;

import l1j.server.server.datatables.ItemTable;

public class L1AdenShopItem {
	private final int _itemId;
	private final L1Item _item;
	private int _price;
	private final int _packCount;
	private int _count;
	private String _html;
	private int _status;
	private int _type;
	private final int _enchant;

	public L1AdenShopItem(int itemId, int price, int packCount, String html, int status, int type, int enchant) {
		_itemId = itemId;
		_item = ItemTable.getInstance().getTemplate(itemId);
		_price = price;
		_packCount = packCount;
		_html = html;
		_count = 1;
		_status = status;
		_type = type;
		_enchant = enchant;
	}

	public int getItemId() {
		return _itemId;
	}
	
	public L1Item getItem() {
		return _item;
	}
	
	public void setPrice(int i) {
		_price = i;
	}
	
	public int getPrice() {
		return _price;
	}

	public int getPackCount() {
		return _packCount;
	}

	public int getCount() {
		return _count;
	}

	public void setCount(int i) {
		_count = i;
	}

	public String getHtml() {
		return _html;
	}

	public int getStatus() {
		return _status;
	}

	public int getType() {
		return _type;
	}
	public int getEnchant() {
		return _enchant;
	}
}

