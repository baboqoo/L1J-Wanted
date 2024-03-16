package l1j.server.GameSystem.shoplimit.bean;

import java.sql.Timestamp;

import l1j.server.GameSystem.shoplimit.ShopLimitTerm;

public class ShopLimitObject {
	private int buyShopId;
	private int buyItemId;
	private int buyCount;
	private Timestamp buyTime;
	private ShopLimitTerm limitTerm;
	
	public ShopLimitObject(int buyShopId, int buyItemId, int buyCount, Timestamp buyTime, ShopLimitTerm limitTerm) {
		this.buyShopId		= buyShopId;
		this.buyItemId		= buyItemId;
		this.buyCount		= buyCount;
		this.buyTime		= buyTime;
		this.limitTerm		= limitTerm;
	}
	
	public int getBuyShopId() {
		return buyShopId;
	}
	public void setBuyShopId(int buyShopId) {
		this.buyShopId = buyShopId;
	}
	public int getBuyItemId() {
		return buyItemId;
	}
	public void setBuyItemId(int buyItemId) {
		this.buyItemId = buyItemId;
	}
	public int getBuyCount() {
		return buyCount;
	}
	public void setBuyCount(int buyCount) {
		this.buyCount = buyCount;
	}
	public Timestamp getBuyTime() {
		return buyTime;
	}
	public void setBuyTime(Timestamp buyTime) {
		this.buyTime = buyTime;
	}
	public ShopLimitTerm getLimitTerm() {
		return limitTerm;
	}
	public void setLimitTerm(ShopLimitTerm limitTerm) {
		this.limitTerm = limitTerm;
	}
}

