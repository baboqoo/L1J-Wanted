package l1j.server.GameSystem.shoplimit.bean;

import l1j.server.GameSystem.shoplimit.ShopLimitTerm;
import l1j.server.GameSystem.shoplimit.ShopLimitType;

public class ShopLimitInformation {
	private int shopId;
	private int itemId;
	private ShopLimitTerm limitTerm;
	private int limitCount;
	private ShopLimitType limitType;
	
	public ShopLimitInformation(int shopId, int itemId, ShopLimitTerm limitTerm, int limitCount, ShopLimitType limitType) {
		this.shopId		= shopId;
		this.itemId		= itemId;
		this.limitTerm	= limitTerm;
		this.limitCount	= limitCount;
		this.limitType	= limitType;
	}
	
	public int getShopId() {
		return shopId;
	}
	public int getItemId() {
		return itemId;
	}
	public ShopLimitTerm getLimitTerm() {
		return limitTerm;
	}
	public int getLimitCount() {
		return limitCount;
	}
	public ShopLimitType getLimitType() {
		return limitType;
	}
}

