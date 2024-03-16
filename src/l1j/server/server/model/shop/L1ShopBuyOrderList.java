package l1j.server.server.model.shop;

import java.util.ArrayList;
import java.util.List;

import l1j.server.Config;
import l1j.server.server.model.L1TaxCalculator;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1ShopItem;

public class L1ShopBuyOrderList {
	private final L1Shop _shop;
	private final List<L1ShopBuyOrder> _list = new ArrayList<L1ShopBuyOrder>();
	private final L1TaxCalculator _taxCalc;

	private int _totalWeight = 0;
	private int _totalPrice = 0;
	private int _totalPriceTaxIncluded = 0;
	private int bugok = 0;

	L1ShopBuyOrderList(L1Shop shop) {
		_shop = shop;
		_taxCalc = new L1TaxCalculator(shop.getNpcId());
	}


	public void add(int orderNumber, int count, L1PcInstance pc) {
		if (_shop.getSellingItems().size() < orderNumber) {
			return;
		}
		if (count >= 10000) {
			bugok = 1;
			return;
		}

		// arraylist size over flow exception.
		if (_shop.getSellingItems().size() <= 0 || _shop.getSellingItems().size() < orderNumber) {
			return;
		}
		L1ShopItem shopItem = _shop.getSellingItems().get(orderNumber);
		if (shopItem == null) {
			return;
		}
		int price = (int) (shopItem.getPrice() * Config.RATE.RATE_SHOP_SELLING_PRICE);
		// 오버플로우 체크
		for (int j = 0; j < count; j++) {
			if (price * j < 0) {
				return;
			}
		}		
		//int itemPrice = 0; 

		_totalPrice += price * count;

		int npcId = _shop.getNpcId();

		if (npcId == 70035 || npcId == 70041 || npcId == 70042) {
			_totalPriceTaxIncluded += shopItem.getPrice() * count; // 버경표 열외
			//itemPrice = shopItem.getPrice() * count;
		} else {
			_totalPriceTaxIncluded += _taxCalc.layTax(price) * count;
			//itemPrice = _taxCalc.layTax(price) * count;
		}
		_totalWeight += shopItem.getItem().getWeight() * count * shopItem.getPackCount();

		long totalprice = _totalPrice;

		// ** 상점 구입 비셔스 방어 **//

		if (count <= 0 || count > 9999) {
			pc.denals_disconnect(String.format("[L1ShopBuyOrderList] COUNT_DENALS : NAME(%s)", pc.getName()));
			bugok = 1;
			return;
		}
		if (totalprice < 0 || price < 0) { // ##### 43억 버그방지 추가
			pc.denals_disconnect(String.format("[L1ShopBuyOrderList] PRICE_DENALS : NAME(%s)", pc.getName()));
			bugok = 1;
			return;
		}
		// ** 상점 구입 비셔스 방어 **//

		if (shopItem.getItem().isMerge()) {
			_list.add(new L1ShopBuyOrder(shopItem, count * shopItem.getPackCount(), orderNumber));
			return;
		}

		for (int i = 0; i < (count * shopItem.getPackCount()); i++) {
			_list.add(new L1ShopBuyOrder(shopItem, 1, orderNumber));
		}
	}

	public List<L1ShopBuyOrder> getList() {
		return _list;
	}

	// ** 상점 구입 비셔스 방어 **//
	public int BugOk() {
		return bugok;
	}

	// ** 상점 구입 비셔스 방어 **//

	public int getTotalWeight() {
		return _totalWeight;
	}

	public int getTotalPrice() {
		return _totalPrice;
	}

	public int getTotalPriceTaxIncluded() {
		return _totalPriceTaxIncluded;
	}

	L1TaxCalculator getTaxCalculator() {
		return _taxCalc;
	}

}

