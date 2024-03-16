package l1j.server.server.model.shop;

import l1j.server.server.templates.L1ShopItem;

public class L1ShopBuyOrder {
	private final L1ShopItem _item;
	private final int _count;
	private final int _orderNumber;

	public L1ShopBuyOrder(L1ShopItem item, int count, int orderNumber) {
		_item			= item;
		_count			= count;
		_orderNumber	= orderNumber;
	}

	public L1ShopItem getItem() {
		return _item;
	}

	public int getCount() {
		return _count;
	}

	public int getOrderNumber() {
		return _orderNumber;
	}
}

