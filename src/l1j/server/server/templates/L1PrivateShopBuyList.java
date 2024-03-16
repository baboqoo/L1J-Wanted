package l1j.server.server.templates;

import l1j.server.server.model.Instance.L1ItemInstance;

public class L1PrivateShopBuyList {
	public L1PrivateShopBuyList() {
	}

	private int _itemObjectId;
	public void setItemObjectId(int i) {
		_itemObjectId = i;
	}
	public int getItemObjectId() {
		return _itemObjectId;
	}

	private int _buyTotalCount;
	public void setBuyTotalCount(int i) {
		_buyTotalCount = i;
	}
	public int getBuyTotalCount() {
		return _buyTotalCount;
	}

	private int _buyPrice;
	public void setBuyPrice(int i) {
		_buyPrice = i;
	}
	public int getBuyPrice() {
		return _buyPrice;
	}

	private int _buyCount;
	public void setBuyCount(int i) {
		_buyCount = i;
	}
	public int getBuyCount() {
		return _buyCount;
	}
	
	private int _enchantLevel;
	public void setEnchantLevel(int i) {
		_enchantLevel = i;
	}
	public int getEnchantLevel() {
		return _enchantLevel;
	}
	
	private int _attrType;
	public void setAttyType(int i) {
		_attrType = i;
	}
	public int getAttyType() {
		return _attrType;
	}
	
	private int _attrEnchantLevel;
	public void setAttyEnchantLevel(int i) {
		_attrEnchantLevel = i;
	}
	public int getAttyEnchantLevel() {
		return _attrEnchantLevel;
	}
	
	private int _bless;
	public void setBless(int i) {
		_bless = i;
	}
	public int getBless() {
		return _bless;
	}
	
	private boolean _isBySearching;
	public void setBySearching(boolean val) {
		_isBySearching = val;
	}
	public boolean isBySearching() {
		return _isBySearching;
	}
	
	private L1ItemInstance _buyItem;
	public void setBuyItem(L1ItemInstance i) {
		_buyItem = i;
	}
	public L1ItemInstance getBuyItem() {
		return _buyItem;
	}
}

