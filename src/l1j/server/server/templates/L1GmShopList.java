package l1j.server.server.templates;

import l1j.server.server.model.Instance.L1ItemInstance;

public class L1GmShopList {
	private L1ItemInstance _item;
	private int _count;
	private int _price;

	public L1GmShopList(L1ItemInstance item, int count, int price) {
		_item	= item;
		_count	= count;
		_price	= price;
	}

	public L1ItemInstance getItem() {
		return _item;
	}
	
	public void setCount(int i) {
		_count = i;
	}

	public int getCount() {
		return _count;
	}
	
	public int getPrice() {
		return _price;
	}
	
	
}

