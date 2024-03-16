package l1j.server.server.templates;

import l1j.server.server.model.Instance.L1ItemInstance;

public class L1PrivateShopSellList {
	public L1PrivateShopSellList() {
	}

	private int _itemObjectId;
	public void setItemObjectId(int i) {
		_itemObjectId = i;
	}
	public int getItemObjectId() {
		return _itemObjectId;
	}

	private int _sellTotalCount;
	public void setSellTotalCount(int i) {
		_sellTotalCount = i;
	}
	public int getSellTotalCount() {
		return _sellTotalCount;
	}

	private int _sellPrice;
	public void setSellPrice(int i) {
		_sellPrice = i;
	}
	public int getSellPrice() {
		return _sellPrice;
	}

	private int _sellCount; 
	public void setSellCount(int i) {
		_sellCount = i;
	}
	public int getSellCount() {
		return _sellCount;
	}
	
	private L1ItemInstance _sellItem;
	public void setSellItem(L1ItemInstance i) {
		_sellItem = i;
	}
	public L1ItemInstance getSellItem() {
		return _sellItem;
	}
}

