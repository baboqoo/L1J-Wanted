package l1j.server.web.dispatcher.response.market;

import java.util.ArrayList;
import java.util.HashMap;

import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1PrivateShopBuyList;
import l1j.server.server.templates.L1PrivateShopSellList;

/**
 * 시세
 * @author LinOffice
 */
public class MarketLoader{
	private static final Object _lock = new Object();
	private static MarketLoader _instance;
	private HashMap<Integer, ArrayList<MarketItemObject>> _purs;
	private HashMap<Integer, ArrayList<MarketItemObject>> _sells;
	
	public static MarketLoader getInstance(){
		if (_instance == null) {
			_instance = new MarketLoader();
		}
		return _instance;
	}

	private MarketLoader(){
		_purs	= new HashMap<>(1024);
		_sells	= new HashMap<>(1024);
	}
	
	public boolean contains(MPSEElement element) {
		return (this._sells.containsKey(element.normalId)) || (this._sells.containsKey(element.blessId)) || (this._sells.containsKey(element.curseId)) 
				|| (this._purs.containsKey(element.normalId)) || (this._purs.containsKey(element.blessId)) || (this._purs.containsKey(element.curseId));
	}

	public ArrayList<MarketItemObject> getPurchasings(int itemId) {
		synchronized (_lock) {
			return this._purs.get(itemId);
		}
	}

	public ArrayList<MarketItemObject> getSellings(int itemId) {
		synchronized (_lock) {
			return this._sells.get(itemId);
		}
	}
	
	/**
	 * 상점 개설시 아이템 등록
	 * @param type
	 * @param list
	 * @param cha
	 */
	public void addItem(int type, ArrayList<?> list, L1Character cha){
		synchronized (_lock) {
			L1ItemInstance item			= null;
			MarketItemObject obj		= null;
			L1PrivateShopSellList sell	= null;
			L1PrivateShopBuyList buy	= null;
			switch(type){
			case 0:// 판매
				for (int i=0; i<list.size(); i++) {
					sell	= (L1PrivateShopSellList) list.get(i);
					item	= sell.getSellItem();
					if (item == null) {
						item = cha.getInventory().getItem(sell.getItemObjectId());
					}
					if (item == null) {
						continue;
					}
					obj				= new MarketItemObject();
					obj.itemObjId	= item.getId();
					obj.itemId		= item.getItemId();
					obj.charName	= cha.getName();
					//obj.name		= item.getDescKr();
					obj.name		= item.getDescEn();
					obj.price		= sell.getSellPrice();
					// 0 = 미확 1 = 확인보통 2 = 축 3 = 저주
					if (!item.isIdentified()){
						obj.iden = 0;
					} else {
						switch (item.getBless()) {
						case 0:obj.iden = 2;break;// 축
						case 1:obj.iden = 1;break;// 보통
						case 2:obj.iden = 3;break;// 저주
						}
					}
					obj.type		= type;
					obj.invGfx		= item.getIconId();
					obj.attr		= item.getAttrEnchantLevel();
					obj.enchant		= item.getEnchantLevel();
					obj.count		= sell.getSellTotalCount();
					obj.itemType	= item.getItem().getItemType().getInteractionType();
					obj.loc			= getLoc(cha.getX(), cha.getY());
					getAndSet(this._sells, obj);
				}
				break;
			case 1:// 구매
				for (int i=0; i<list.size(); i++) {
					buy		= (L1PrivateShopBuyList) list.get(i);
					item	= buy.getBuyItem();
					if (item == null) {
						if (buy.isBySearching()) {// 검색으로 등록된 아이템
							ItemTable temp = ItemTable.getInstance();
							L1Item createItem = temp.findItemByNameIdAndBless(buy.getItemObjectId(), buy.getBless());// 아이템 정보취득
							item = temp.createItem(createItem);// 아이템 생성
							buy.setBuyItem(item);
							item.setEnchantLevel(buy.getEnchantLevel());
							int buyAttrType = buy.getAttyType();
							int buyAttrEnchant = buy.getAttyEnchantLevel();
							if (buyAttrType > 0 && buyAttrEnchant > 0) {
								item.setAttrEnchantLevel(L1ItemInstance.calculateElementalEnchant(buyAttrType, buyAttrEnchant));
							}
							item.setBless(buy.getBless());
							item.setIdentified(true);
						} else {
							item = cha.getInventory().getItem(buy.getItemObjectId());
						}
					}
					if (item == null) {
						continue;
					}
					obj				= new MarketItemObject();
					obj.itemObjId	= item.getId();
					obj.itemId		= item.getItemId();
					obj.charName	= cha.getName();
					//obj.name		= item.getDescKr();
					obj.name		= item.getDescEn();
					obj.price		= buy.getBuyPrice();
					// 0 = 미확 1 = 확인보통 2 = 축 3 = 저주
					if (!item.isIdentified()){
						obj.iden = 0;
					} else {
						switch (item.getBless()) {
						case 0:obj.iden = 2;break;// 축
						case 1:obj.iden = 1;break;// 보통
						case 2:obj.iden = 3;break;// 저주
						}
					}
					obj.type		= type;
					obj.invGfx		= item.getIconId();
					obj.attr		= item.getAttrEnchantLevel();
					obj.enchant		= item.getEnchantLevel();
					obj.count		= buy.getBuyTotalCount();
					obj.itemType	= item.getItem().getItemType().getInteractionType();
					obj.loc			= getLoc(cha.getX(), cha.getY());
					getAndSet(this._purs, obj);
				}
				break;
			}
		}
	}
	
	/**
	 * 상점이 종료되면 등록한 모든 아이템을 제거한다.
	 * @param cha
	 */
	public void deleteShop(L1Character cha){
		synchronized (_lock) {
			MarketItemObject obj = null;
			if (!this._sells.isEmpty()) {
				for (ArrayList<MarketItemObject> list : this._sells.values()) {
					if (list == null || list.isEmpty()) {
						continue;
					}
					for (int j=0; j<list.size(); j++) {
						obj = list.get(j);
						if (obj == null || !obj.charName.equals(cha.getName())) {
							continue;
						}
						list.remove(obj);
					}
				}
			}
			
			if (!this._purs.isEmpty()) {
				for (ArrayList<MarketItemObject> list : this._purs.values()) {
					if (list == null || list.isEmpty()) {
						continue;
					}
					for (int j=0; j<list.size(); j++) {
						obj = list.get(j);
						if (obj == null || !obj.charName.equals(cha.getName())) {
							continue;
						}
						list.remove(obj);
					}
				}
			}
		}
	}
	
	/**
	 * 수량 변화가 발생시 갱신한다.
	 * @param charName
	 * @param itemObjId
	 * @param count
	 * @param type
	 */
	public void updateItem(String charName, int itemObjId, int count, int type) {
		synchronized (_lock) {
			for (ArrayList<MarketItemObject> list : type == 0 ? this._sells.values() : this._purs.values()) {
				for (MarketItemObject obj : list) {
					if (obj.charName.equals(charName) && obj.itemObjId == itemObjId) {
						if (count <= 0) {
							list.remove(obj);
						} else {
							obj.count = count;
						}
						return;
					}
				}
			}
		}
	}
	
	/**
	 * 수량 변화가 발생시 갱신한다.
	 * @param charName
	 * @param itemId
	 * @param count
	 * @param enchant
	 * @param type
	 */
	public void updateNpcItem(String charName, int itemId, int count, int enchant, int type) {
		synchronized (_lock) {
			for (ArrayList<MarketItemObject> list : type == 0 ? this._sells.values() : this._purs.values()) {
				for (MarketItemObject obj : list) {
					if (obj.charName.equals(charName) && obj.itemId == itemId && obj.enchant == enchant) {
						if (count <= 0) {
							list.remove(obj);
						} else {
							obj.count = count;
						}
						return;
					}
				}
			}
		}
	}
	
	private static final String[] LOC_NAME = {
		//"중앙구역", "1구역", "2구역", "3구역", "4구역", "5구역", "6구역", "7구역", "8구역"
		"Central Area", "Zone 1", "Zone 2", "Zone 3", "Zone 4", "Zone 5", "Zone 6", "Zone 7", "Zone 8"

	};
	
	public String getLoc(int x, int y) {
		if ((x >= 32816 && x <= 32846) && (y >= 32881 && y <= 32911)) {
			return LOC_NAME[1];
		}
		if ((x >= 32785 && x <= 32815) && (y >= 32881 && y <= 32910)) {
			return LOC_NAME[2];
		}
		if ((x >= 32755 && x <= 32783) && (y >= 32881 && y <= 32909)) {
			return LOC_NAME[3];
		}
		if ((x >= 32755 && x <= 32782) && (y >= 32912 && y <= 32943)) {
			return LOC_NAME[4];
		}
		if ((x >= 32755 && x <= 32783) && (y >= 32944 && y <= 32972)) {
			return LOC_NAME[5];
		}
		if ((x >= 32785 && x <= 32815) && (y >= 32945 && y <= 32972)) {
			return LOC_NAME[6];
		}
		if ((x >= 32818 && x <= 32846) && (y >= 32945 && y <= 32972)) {
			return LOC_NAME[7];
		}
		if ((x >= 32818 && x <= 32846) && (y >= 32911 && y <= 32942)) {
			return LOC_NAME[8];
		}
		return LOC_NAME[0];
	}

	private void getAndSet(HashMap<Integer, ArrayList<MarketItemObject>> map, MarketItemObject obj) {
		ArrayList<MarketItemObject> list = map.get(obj.itemId);
		if (list == null) {
			list = new ArrayList<>(64);
			map.put(obj.itemId, list);
		}
		list.add(obj);
	}
	
	public static void release() {
		if (_instance != null) {
			_instance.clear();
			_instance = null;
		}
	}

	public void clear() {
		synchronized (_lock) {
			if (this._sells != null) {
				for (ArrayList<MarketItemObject> list : this._sells.values()) {
					if (list == null) {
						continue;
					}
					list.clear();
				}
				this._sells.clear();
				this._sells = null;
			}

			if (this._purs != null) {
				for (ArrayList<MarketItemObject> list : this._purs.values()) {
					if (list == null) {
						continue;
					}
					list.clear();
				}
				this._purs.clear();
				this._purs = null;
			}
		}
	}
}
