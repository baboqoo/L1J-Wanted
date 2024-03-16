package l1j.server.GameSystem.shoplimit;

import javolution.util.FastTable;
import l1j.server.GameSystem.shoplimit.bean.ShopLimitObject;

public class ShopLimitUser {
	private final FastTable<ShopLimitObject> _list = new FastTable<ShopLimitObject>();
	
	public ShopLimitUser(){
	}
	
	public FastTable<ShopLimitObject> getLimitList(){
		return _list;
	}
	
	public ShopLimitObject getLimit(int shopId, int itemId){
		for (ShopLimitObject obj : _list) {
			if (obj.getBuyShopId() == shopId && obj.getBuyItemId() == itemId) {
				return obj;
			}
		}
		return null;
	}
	
	public void addLimit(ShopLimitObject obj){
		if (_list.contains(obj)) {
			return;
		}
		_list.add(obj);
	}
	
	public void removeLimit(ShopLimitObject obj){
		if (!_list.contains(obj)) {
			return;
		}
		_list.remove(obj);
	}
	
	public void clearLimit(){
		_list.clear();
	}
}

