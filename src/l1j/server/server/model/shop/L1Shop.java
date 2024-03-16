package l1j.server.server.model.shop;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import l1j.server.Config;
import l1j.server.GameSystem.arca.L1Arca;
import l1j.server.GameSystem.shoplimit.ShopLimitLoader;
import l1j.server.GameSystem.shoplimit.bean.ShopLimitInformation;
import l1j.server.server.GameServerSetting;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.controller.DogFightController;
import l1j.server.server.controller.DollRaceController;
import l1j.server.server.datatables.CastleTable;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.LogTable;
import l1j.server.server.datatables.ShopTable;
import l1j.server.server.datatables.ShopTable.L1ShopInfo;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1TaxCalculator;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcShopInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.item.function.PledgeItem;
import l1j.server.server.monitor.Logger.ShopLogType;
import l1j.server.server.monitor.LoggerInstance;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_TamPointNoti;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.serverpackets.pledge.S_BloodPledgeContribution;
import l1j.server.server.templates.L1Castle;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1PrivateShopSellList;
import l1j.server.server.templates.L1ShopItem;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.IntRange;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.response.market.MarketLoader;
//import manager.Manager;  // MANAGER DISABLED
//import manager.ManagerInfoThread;  // MANAGER DISABLED

public class L1Shop {
	private final int _npcId;
	private final List<L1ShopItem> _sellingItems;
	private final List<L1ShopItem> _purchasingItems;

	public L1Shop(int npcId, List<L1ShopItem> sellingItems, List<L1ShopItem> purchasingItems) {
		if (sellingItems == null || purchasingItems == null)
			throw new NullPointerException();
		
		_npcId				= npcId;
		_sellingItems		= sellingItems;
		_purchasingItems	= purchasingItems;
	}

	public int getNpcId() {
		return _npcId;
	}
	public List<L1ShopItem> getSellingItems() {
		return _sellingItems;
	}
	public List<L1ShopItem> getBuyingItems() {
		return _purchasingItems;
	}

	/** 판매하기 아이템 리스트 제외 **/
	private boolean isPurchaseableItem(L1ItemInstance item) {
		if (item == null || item.isEquipped() || item.getBless() >= 128) {
			return false;
		}
		return true;
	}
	public L1ShopItem getSellItem(int itemid) {
		for (L1ShopItem a : _sellingItems) {
			if (a.getItemId() == itemid) {
				return a;
			}
		}
		return null;
	}

	public L1ShopItem getBuyItem(int itemid) {
		for (L1ShopItem a : _purchasingItems) {
			if (a.getItemId() == itemid) {
				return a;
			}
		}
		return null;
	}
	public boolean isSellingItem(int itemid) {
		for (L1ShopItem a : _sellingItems) {
			if (a.getItemId() == itemid) {
				return true;
			}
		}
		return false;
	}

	private L1ShopItem getPurchasingItem(int itemId, int enchant) {
		for (L1ShopItem shopItem : _purchasingItems) {
			if (shopItem.getItemId() == itemId && shopItem.getEnchant() == enchant) {
				return shopItem;
			}
		}
		return null;
	}

	public L1AssessedItem assessItem(L1ItemInstance item) {
		L1ShopItem shopItem = getPurchasingItem(item.getItemId(), item.getEnchantLevel());
		if (shopItem == null) {
			return null;
		}
		return new L1AssessedItem(item.getId(), getAssessedPrice(shopItem));
	}

	private int getAssessedPrice(L1ShopItem item) {
		return (int) (item.getPrice() * Config.RATE.RATE_SHOP_PURCHASING_PRICE / item.getPackCount());
	}

	public List<L1AssessedItem> assessItems(L1PcInventory inv) {
		List<L1AssessedItem> result = new ArrayList<L1AssessedItem>();
		for (L1ShopItem item : _purchasingItems) {
			for (L1ItemInstance targetItem : inv.findItemsId(item.getItemId())) {
				if (!isPurchaseableItem(targetItem)) {
					continue;
				}
				if (item.getEnchant() == targetItem.getEnchantLevel()) {// 인챈트가 같은 아이템만
					result.add(new L1AssessedItem(targetItem.getId(), getAssessedPrice(item)));
				}
			}
		}
		return result;
	}

	private boolean ensureSell(L1PcInstance pc, L1ShopBuyOrderList orderList) {
		int price = orderList.getTotalPriceTaxIncluded();
		if (!IntRange.includes(price, 0, L1Inventory.MAX_AMOUNT)) {
			pc.sendPackets(new S_ServerMessage(904, StringUtil.EmptyString + L1Inventory.MAX_AMOUNT), true);
			return false;
		}
		if (!pc.getInventory().checkItem(L1ItemId.ADENA, price)) {
			pc.sendPackets(L1ServerMessage.sm189);
			return false;
		}
		return isAvailable(pc, orderList, price);
	}

	private void payCastleTax(L1ShopBuyOrderList orderList) {
		L1TaxCalculator calc = orderList.getTaxCalculator();
		int price = orderList.getTotalPrice();
		int castleId = L1CastleLocation.getCastleIdByNpcid(_npcId);
		int castleTax = calc.calcCastleTaxPrice(price);
		int nationalTax = calc.calcNationalTaxPrice(price);
		if (castleId == L1CastleLocation.ADEN_CASTLE_ID || castleId == L1CastleLocation.DIAD_CASTLE_ID) {
			castleTax += nationalTax;
			nationalTax = 0;
		}
		if (castleId != 0 && castleTax > 0) {
			L1Castle castle = CastleTable.getInstance().getCastleTable(castleId);
			synchronized (castle) {
				int money = castle.getPublicMoney();
				if (L1Inventory.MAX_AMOUNT > money + castleTax) {
					money = money + castleTax;
					castle.setPublicMoney(money);
					CastleTable.getInstance().updateCastle(castle);
				}
			}
			if (nationalTax > 0) {
				L1Castle aden = CastleTable.getInstance().getCastleTable(L1CastleLocation.ADEN_CASTLE_ID);
				synchronized (aden) {
					int money = aden.getPublicMoney();
					if (L1Inventory.MAX_AMOUNT > money + castleTax) {
						money = money + nationalTax;
						aden.setPublicMoney(money);
						CastleTable.getInstance().updateCastle(aden);
					}
				}
			}
		}
	}

	private void payDiadTax(L1ShopBuyOrderList orderList) {
		L1TaxCalculator calc = orderList.getTaxCalculator();
		int price = orderList.getTotalPrice();
		int diadTax = calc.calcDiadTaxPrice(price);
		if (diadTax <= 0) {
			return;
		}
		L1Castle castle = CastleTable.getInstance().getCastleTable(L1CastleLocation.DIAD_CASTLE_ID);
		synchronized (castle) {
			int money = castle.getPublicMoney();
			if (L1Inventory.MAX_AMOUNT > money + diadTax) {
				money = money + diadTax;
				castle.setPublicMoney(money);
				CastleTable.getInstance().updateCastle(castle);
			}
		}
	}

	private void payTax(L1ShopBuyOrderList orderList) {
		payCastleTax(orderList);
		payDiadTax(orderList);
	}
	
	/**
	 * 겜블 상점(롤코)
	 * -2 ~ +7 까지의 랜덤한 인첸트 수치를 획득한다.
	 * @return enchant_level
	 */
	private int get_random_enchant_level_from_shop_buy() {
		int chance = CommonUtil.random(150) + 1;
		if (chance <= 15) {
			return -2;
		}
		if (chance >= 16 && chance <= 30) {
			return -1;
		}
		if (chance >= 31 && chance <= 89) {
			return 0;
		}
		if (chance >= 90 && chance <= 141) {
			return CommonUtil.random(2) + 1;
		}
		if (chance >= 142 && chance <= 147) {
			return CommonUtil.random(3) + 3;
		}
		if (chance >= 148 && chance <= 149) {
			return 6;
		}
		return 7;
	}

	private void sellItems(L1PcInventory inv, L1ShopBuyOrderList orderList,  L1PcInstance pc) {
		if (!inv.consumeItem(L1ItemId.ADENA, orderList.getTotalPriceTaxIncluded()))
			//throw new IllegalStateException("구입에 필요한 아데나를 소비 할 수 없습니다.");
			throw new IllegalStateException("You cannot spend the adena required for purchase.");
		
		L1ItemInstance item = null;
		if (_npcId == 70035 || _npcId == 70041 || _npcId == 70042) {
			/*if ((ManagerInfoThread.AdenConsume - orderList.getTotalPrice()) >= 0) {
				ManagerInfoThread.AdenConsume -= orderList.getTotalPrice();
			}*/  // MANAGER DISABLED
		}
		
		for (L1ShopBuyOrder order : orderList.getList()) {
			int itemId = order.getItem().getItemId();
			int amount = order.getCount();
			int enchant = order.getItem().getEnchant();
			item = ItemTable.getInstance().createItem(itemId);
			if (getSellingItems().contains(item)) {
				return;
			}
			item.setCount(amount);
			item.setIdentified(true);
			item.setEnchantLevel(enchant);
			if (_npcId == 70068 || _npcId == 70020 || _npcId == 70056) {// 프랑코, 롤코, 조드
				item.setIdentified(false);
				item.setEnchantLevel(get_random_enchant_level_from_shop_buy());
			} else if (_npcId == 81008 || _npcId == 70010 || _npcId == 80020 || _npcId == 200004 
					|| _npcId == 900171 || _npcId == 81007 || _npcId == 611156 || _npcId == 81110
					|| _npcId == 8111111 || _npcId == 5000003 || _npcId == 5000007 || _npcId == 795478 
					|| _npcId == 460000131 || _npcId == 999888) {// 인챈상점 추가
				item.setEnchantLevel(enchant);
			} else if (_npcId == 70035 || _npcId == 70041 || _npcId == 70042) {// 인형경주
				for (int row = 0; row < 5; row++) {
					if (DollRaceController.getInstance()._ticketId[row] == item.getItemId()) {
						DollRaceController.getInstance()._ticketCount[row] += amount;
					}
				}
			} else if (_npcId == 170041) {// 투견
				for (int row = 0; row < 2; row++) {
					if (DogFightController.getInstance()._ticketId[row] == item.getItemId()) {
						DogFightController.getInstance()._ticketCount[row] += amount;
					}
				}
			}
			
			ShopLimitInformation limit = ShopLimitLoader.getShopLimit(_npcId, item.getItemId());
			if (limit != null) {
				ShopLimitLoader.getInstance().buyLimitItem(pc, limit, _npcId, item);
			}
			
			item = inv.storeItem(item);
			LoggerInstance.getInstance().addShop(ShopLogType.BUY, pc, getNpcId(), item, amount, order.getItem().getPrice() * (amount / order.getItem().getPackCount()));
			if (Config.ALT.SAVE_DB_LOG) {
				LogTable.logNpcShop(pc, getNpcId(), item, order.getItem().getPrice(), amount, 0);
			}
		}
	}
	
	/**
	 * 아이템 구매
	 * @param pc
	 * @param orderList
	 */
	public void sellItems(L1PcInstance pc, L1ShopBuyOrderList orderList) {
		L1ShopInfo info = ShopTable.getInstance().getShopInfo(_npcId);
		if (info != null) {
			switch(info.getType()){
			case ITEM:
			case BERRY:
				if (!isEnsureSell(pc, orderList, info.getCurrencyId())) {
					return;
				}
				sellConsumeItem(pc.getInventory(), orderList, info.getCurrencyId());
				break;
			case TAM:
				if (!isEnsureTam(pc, orderList)) {
					return;
				}
				sellItemTam(pc, pc.getInventory(), orderList);
				break;
			case N_COIN:
				if (!ncoinCheck(pc, orderList)) {
					return;
				}
				ncoinBuy(pc, pc.getInventory(), orderList);
				break;
			case EIN_POINT:
				if (!isEinPoint(pc, orderList)) {
					return;
				}
				sellItemEinPoint(pc, pc.getInventory(), orderList);
				break;
			case CLAN:
				if (!isEnsurePledge(pc, orderList)) {
					return;
				}
				sellItemPledge(pc, pc.getInventory(), orderList);
				break;
			default:break;
			}
			return;
		} else if (_npcId >= 4000001 && _npcId <= 4000061) {// 영자 엔피씨무인상점
			if (!noTaxEnsureSell(pc, orderList)) {
				return;
			}
			npcShopSellItems(pc.getInventory(), orderList);
			return;
		} else if (_npcId >= 6100000 && _npcId <= 6100013 || _npcId >= 6100042 && _npcId <= 6100045 || _npcId >= 6100014 && _npcId <= 6100041) {// 코인 상인
			if (!ensureCashSell(pc, orderList, getNpcId())) {
				return;
			}
			sellCashItems(pc, pc.getInventory(), orderList, getNpcId());
			return;
		}
		if (!ensureSell(pc, orderList)) {
			return;
		}
		sellItems(pc.getInventory(), orderList, pc);
		payTax(orderList);
	}

	/** 물품판매시 획득 아이템 처리 **/
	public void buyItems(L1ShopSellOrderList orderList) {
		L1PcInventory inv = orderList.getPc().getInventory();
		int totalPrice = 0;
		L1Object object = null;
		L1ItemInstance item = null;
		for (L1ShopSellOrder order : orderList.getList()) {
			object = inv.getItem(order.getItem().getTargetId());
			item = (L1ItemInstance) object;
			if (item != null && item.getItem().getBless() < 128) {
				int count = inv.removeItem(order.getItem().getTargetId(), order.getCount());
				totalPrice += order.getItem().getAssessedPrice() * count;
				LoggerInstance.getInstance().addShop(ShopLogType.SELL, inv.getOwner(), getNpcId(), item, count, order.getItem().getAssessedPrice() * count);
				if (Config.ALT.SAVE_DB_LOG) {
					LogTable.logNpcShop(inv.getOwner(), getNpcId(), item, (int) order.getItem().getAssessedPrice(), count, 1);
				}
				//Manager.getInstance().ShopAppend(item.getLogName(), count, (int) order.getItem().getAssessedPrice(), null, inv.getOwner().getName()); // MANAGER DISABLED
			}
		}
		totalPrice = IntRange.ensure(totalPrice, 0, L1Inventory.MAX_AMOUNT);// 20억 체크
		if (totalPrice > 0) {
			if (getNpcId() >= 6100000 && getNpcId() <= 6100041  || getNpcId() >= 6100042 && getNpcId() <= 6100045) {
				inv.storeItem(getNpcId() - 5299999, totalPrice);
			} else if (_npcId == 7000077) {
				inv.storeItem(41302, totalPrice);// 행베리
			} else if (_npcId == 6000002) {
				inv.storeItem(L1ItemId.PIXIE_GOLD_FEATHER, totalPrice);// 픽시의 금빛 깃털
			} else if (_npcId == 7311108) {
				inv.storeItem(41922, totalPrice);// 픽시의 코인
			} else if (_npcId == 7220072) {
				inv.storeItem(31151, totalPrice);// 오림의 가넷
			} else if (_npcId == 7230010) {
				inv.storeItem(31177, totalPrice);// 하딘의 가넷
			} else if (_npcId == 100801 || _npcId == 100556 || _npcId == 100555) {
				inv.storeItem(L1ItemId.KNIGHT_COIN, totalPrice);// 기사단의 주화
			} else if (_npcId >= 7210067 && _npcId <= 7210069) {
				inv.storeItem(L1ItemId.CHEQUE, totalPrice);
			} else if (_npcId == 100703) {
				inv.storeItem(3200012, totalPrice);// 야성 강화 촉매제
			} else {
				inv.storeItem(L1ItemId.ADENA, totalPrice);
				/*if (getNpcId() != 70041 && getNpcId() != 70042 && getNpcId() != 70035 && getNpcId() != 170041) {// 버경 엔피씨 제외
					ManagerInfoThread.AdenMake += totalPrice;
				}*/   // MANAGER DISABLED
				if (getNpcId() != 70035 && getNpcId() != 70041 && getNpcId() != 70042 && getNpcId() != 170041 && Config.ALT.SAVE_DB_LOG) {
					LogTable.shopAden(orderList.getPc(), totalPrice);
				}
			}
		}
	}

	public L1ShopBuyOrderList newBuyOrderList() {
		return new L1ShopBuyOrderList(this);
	}
	public L1ShopSellOrderList newSellOrderList(L1PcInstance pc) {
		return new L1ShopSellOrderList(this, pc);
	}
	
	private boolean isEnsureSell(L1PcInstance pc, L1ShopBuyOrderList orderList, int itemId){
		L1Item item		= ItemTable.getInstance().getTemplate(itemId);
		if (item == null) {
			System.out.println("L1Shop ensureCheckSell method item empty - itemId : " + itemId);
			return false;
		}
		int price		= orderList.getTotalPrice();
		if (!IntRange.includes(price, 0, L1Inventory.MAX_AMOUNT)) {
			//pc.sendPackets(new S_SystemMessage(String.format("%s은(는) 한번에 %d개 이상 사용할 수 없습니다.", item.getDescKr(), L1Inventory.MAX_AMOUNT)), true);
			//pc.sendPackets(new S_SystemMessage(String.format("%s은(는) 한번에 %d개 이상 사용할 수 없습니다.", item.getDesc(), L1Inventory.MAX_AMOUNT), true), true);
			pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(101), item.getDesc(), String.valueOf(L1Inventory.MAX_AMOUNT)), true);
			return false;
		}
		// 구입할 수 있을까 체크
		if (!pc.getInventory().checkItem(itemId, price)) {
			//pc.sendPackets(new S_ServerMessage(337, item.getDescKr()), true);
			pc.sendPackets(new S_ServerMessage(337, item.getDesc()), true);
			return false;
		}
		return isAvailable(pc, orderList, price);
	}
	
	private void sellConsumeItem(L1PcInventory inv, L1ShopBuyOrderList orderList, int itemId){
		L1Item item = ItemTable.getInstance().getTemplate(itemId);
		if (item == null) {
			System.out.println("L1Shop sellValidate method item empty - itemId : " + itemId);
			return;
		}
		if (!inv.consumeItem(itemId, orderList.getTotalPrice()))
			//throw new IllegalStateException(String.format("구입에 필요한 %s을(를) 소비할 수 없었습니다.", item.getDescKr()));
			throw new IllegalStateException(String.format("Failed to consume the required %s for purchase.", item.getDesc()));
		buyOrderListExcute(inv, orderList);
	}

	private boolean noTaxEnsureSell(L1PcInstance pc, L1ShopBuyOrderList orderList) {
		int price = orderList.getTotalPrice();
		if (!IntRange.includes(price, 0, L1Inventory.MAX_AMOUNT)) {
			pc.sendPackets(new S_ServerMessage(904, StringUtil.EmptyString + L1Inventory.MAX_AMOUNT), true);
			return false;
		}
		if (!pc.getInventory().checkItem(L1ItemId.ADENA, price)) {
			pc.sendPackets(L1ServerMessage.sm189);
			return false;
		}
		return isAvailable(pc, orderList, price);
	}

	private void npcShopSellItems(L1PcInventory inv, L1ShopBuyOrderList orderList) {
		if (!inv.consumeItem(L1ItemId.ADENA, orderList.getTotalPrice()))
			//throw new IllegalStateException("구입에 필요한 아데나를 소비 할 수 없습니다.");
			throw new IllegalStateException("You cannot spend the adena required for purchase.");
		L1NpcShopInstance npcShopIns = (L1NpcShopInstance) L1World.getInstance().findNpc(this._npcId);
		L1ItemInstance item		= null;
		ItemTable it			= ItemTable.getInstance();
		LoggerInstance logger	= LoggerInstance.getInstance();
		boolean[] isRemoveFromList = new boolean[8];
		for (L1ShopBuyOrder order : orderList.getList()) {
			int orderid			= order.getOrderNumber();
			int itemId			= order.getItem().getItemId();
			int amount			= order.getCount();
			int enchant			= order.getItem().getEnchant();
			int remaindcount	= _sellingItems.get(orderid).getCount();
	
			if (remaindcount < amount)
				return;
			item = it.createItem(itemId);
			if (_sellingItems.contains(item))
				return;
			item.setCount(amount);
			item.setIdentified(true);
			item.setEnchantLevel(enchant);
			if (remaindcount == amount){
				isRemoveFromList[orderid] = true;
			} else {
				_sellingItems.get(orderid).setCount(remaindcount - amount);
			}
			inv.storeItem(item);
			
			logger.addShop(ShopLogType.BUY, inv.getOwner(), getNpcId(), item, amount, order.getItem().getPrice() * ((amount / order.getItem().getPackCount())));
			if (Config.ALT.SAVE_DB_LOG) {
				LogTable.logNpcShop(inv.getOwner(), getNpcId(), item, order.getItem().getPrice(), amount, 0);
			}
			
			if (Config.WEB.WEB_SERVER_ENABLE) {
				updateAppcenterNpcShop(npcShopIns, orderid, itemId, enchant, remaindcount == amount);
			}
			
			for (int i = 7; i >= 0; i--) {
				if (isRemoveFromList[i]){
					_sellingItems.remove(i);
				}
			}
		}
	}
	
	private void updateAppcenterNpcShop(L1NpcShopInstance npcShopIns, int orderid, int itemId, int enchant, boolean soldout){
		L1PrivateShopSellList sell = null;
		if (npcShopIns.get_sellList() != null && npcShopIns.get_sellList().size() > orderid) {
			sell = npcShopIns.get_sellList().get(orderid);
		}
		
		int afterCount = soldout ? 0 : _sellingItems.get(orderid).getCount();
		if (sell != null) {
			sell.setSellTotalCount(afterCount);
			MarketLoader.getInstance().updateItem(npcShopIns.getName(), sell.getItemObjectId(), sell.getSellTotalCount(), 0);
			if (sell.getSellTotalCount() <= 0) {
				npcShopIns.get_sellList().remove(sell);
			}
		} else {
			MarketLoader.getInstance().updateNpcItem(npcShopIns.getName(), itemId, afterCount, enchant, 0);
		}
	}
	
	private boolean ncoinCheck(L1PcInstance pc, L1ShopBuyOrderList orderList) {
		int price = orderList.getTotalPrice();
		if (!IntRange.includes(price, 0, L1Inventory.MAX_AMOUNT)) {
			return false;
		}
		if (pc.getNcoin() < price){
//AUTO SRM: 			pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"시스템: Ncoin이 부족합니다."), true);	 // CHECKED OK
			pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,S_SystemMessage.getRefText(1149)), true);
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("시스템: Ncoin이 부족합니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1149), true), true);
			return false;
		}
		return isAvailable(pc, orderList, price);
	}

	private void ncoinBuy(L1PcInstance pc, L1PcInventory inv, L1ShopBuyOrderList orderList) {
		if (inv.getOwner().getNcoin() < orderList.getTotalPrice())
			//throw new IllegalStateException("구입에 필요한 Ncoin이 부족합니다.");
			throw new IllegalStateException("There are not enough Ncoins to purchase.");
		if (orderList.getTotalPrice() <= pc.getNcoin()) {
			pc.addNcoin(-(orderList.getTotalPrice()));
			try {
				pc.getAccount().updateNcoin();
			} catch (Exception e) {
			}
		}
		buyOrderListExcute(inv, orderList);
	}

	private boolean isEnsureTam(L1PcInstance pc, L1ShopBuyOrderList orderList) {
		int price = orderList.getTotalPrice();
		if (!IntRange.includes(price, 0, L1Inventory.MAX_AMOUNT)) {
			return false;
		}
		if (pc.getAccount().getArca().getPoint() < price){
//AUTO SRM: 			pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"시스템: TAM이 부족합니다."), true); // CHECKED OK
			pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,S_SystemMessage.getRefText(1150)), true);
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("시스템: TAM이 부족합니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1150), true), true);
			return false;
		}
		return isAvailable(pc, orderList, price);
	}

	private void sellItemTam(L1PcInstance pc, L1PcInventory inv, L1ShopBuyOrderList orderList) {
		L1Arca arca = pc.getAccount().getArca();
		if (arca.getPoint() < orderList.getTotalPrice())
			//throw new IllegalStateException("구입에 필요한 탐이 부족합니다.");
			throw new IllegalStateException("You do not have enough toms to purchase.");
		if (orderList.getTotalPrice() <= arca.getPoint()) {
			arca.addPoint(-orderList.getTotalPrice());
			pc.sendPackets(new S_TamPointNoti(arca.getPoint()), true);
		}
		buyOrderListExcute(inv, orderList);
	}
	
	private boolean isEinPoint(L1PcInstance pc, L1ShopBuyOrderList orderList) {
		int price = orderList.getTotalPrice();
		if (!IntRange.includes(price, 0, L1Inventory.MAX_AMOUNT)) {
//			pc.sendPackets(new S_SystemMessage("시스템: TAM은 한번에 900만개 이상 사용할 수 없습니다."), true);
			return false;
		}
		if (pc.getEinPoint() < price){
//AUTO SRM: 			pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"시스템: 아인하사드 포인트가 부족합니다."), true);	 // CHECKED OK
			pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,S_SystemMessage.getRefText(1151)), true);
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("시스템: 아인하사드 포인트가 부족합니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1151), true), true);
			return false;
		}
		return isAvailable(pc, orderList, price);
	}

	private void sellItemEinPoint(L1PcInstance pc, L1PcInventory inv, L1ShopBuyOrderList orderList) {
		if (inv.getOwner().getEinPoint() < orderList.getTotalPrice())
			//throw new IllegalStateException("구입에 필요한 아인하사드 포인트가 부족합니다.");
			throw new IllegalStateException("There are not enough Einhasad points required for purchase.");
		if (orderList.getTotalPrice() <= pc.getEinPoint()) {
			pc.addEinPoint(-(orderList.getTotalPrice()));
		}
		L1ItemInstance item		= null;
		ItemTable it			= ItemTable.getInstance();
		LoggerInstance logger	= LoggerInstance.getInstance();
		for (L1ShopBuyOrder order : orderList.getList()) {
			int itemId = order.getItem().getItemId();
			int amount = order.getCount();
			item = it.createItem(itemId);
			item.setCount(amount);
			item.setIdentified(true);
			inv.storeItem(item);
			
			ShopLimitInformation limit = ShopLimitLoader.getShopLimit(_npcId, item.getItemId());
			if (limit != null) {
				ShopLimitLoader.getInstance().buyLimitItem(pc, limit, _npcId, item);
			}
			
			logger.addShop(ShopLogType.BUY, inv.getOwner(), getNpcId(), item, amount, order.getItem().getPrice() * ((amount / order.getItem().getPackCount())));
			if (Config.ALT.SAVE_DB_LOG) {
				LogTable.logNpcShop(pc, getNpcId(), item, order.getItem().getPrice(), amount, 0);
			}
		}
	}
	
	private boolean isEnsurePledge(L1PcInstance pc, L1ShopBuyOrderList orderList) {
		int price = orderList.getTotalPrice();
		if (!IntRange.includes(price, 0, L1Inventory.MAX_AMOUNT)) {
			return false;
		}
		if (pc.getClan() == null) {
			pc.sendPackets(L1ServerMessage.sm1064);// 혈맹에 소속되어 있지 않습니다.(/파워북 혈맹매칭)
			return false;
		}
		if (pc.getClan().getContribution() < price) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("구입에 필요한 공헌도가 부족합니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1152), true), true);
			return false;
		}
		return isAvailable(pc, orderList, price);
	}

	private void sellItemPledge(L1PcInstance pc, L1PcInventory inv, L1ShopBuyOrderList orderList) {
		L1Clan clan = pc.getClan();
		if (clan.getContribution() < orderList.getTotalPrice()) {
			//throw new IllegalStateException("구입에 필요한 공헌도가 부족합니다.");
			throw new IllegalStateException("You do not have enough contributions to purchase.");
		}
		clan.addContribution(-orderList.getTotalPrice());
		
		ClanTable ct			= ClanTable.getInstance();
		try {
			ct.updateContribution(clan);
			pc.sendPackets(new S_BloodPledgeContribution(clan.getContribution()), true);
		} catch (Exception e) {
		}
		
		L1ItemInstance item		= null;
		ItemTable it			= ItemTable.getInstance();
		LoggerInstance logger	= LoggerInstance.getInstance();
		for (L1ShopBuyOrder order : orderList.getList()) {
			int itemId = order.getItem().getItemId();
			int amount = order.getCount();
			boolean isStore = true;// 구매 즉시 사용 여부
			switch(itemId){
			case 5994:// 맹세의 열쇠
				isStore = false;
				if (PledgeItem.pledgeDungeonKey(pc, null, itemId)) {
					clan.setClanDayDungeonTime(new Timestamp(System.currentTimeMillis()));
					ct.updateClanDungeonTime(String.format("UPDATE clan_data SET dayDungeonTime=NOW() WHERE clan_name='%s'", clan.getClanName()));
				}
				break;
			case 5995:case 5997:// 맹세의 열쇠(주간)
				isStore = false;
				if (PledgeItem.pledgeDungeonKey(pc, null, itemId)) {
					clan.setClanWeekDungeonTime(new Timestamp(System.currentTimeMillis()));
					ct.updateClanDungeonTime(String.format("UPDATE clan_data SET weekDungeonTime=NOW() WHERE clan_name='%s'", clan.getClanName()));
				}
				break;
			case 5996:// 맹세의 영약
				isStore = false;
				clan.setClanVowPotionCount(clan.getClanVowPotionCount() + 1);
				ct.updateClanVowCount(clan.getClanVowPotionCount(), clan.getClanId());
				if (pc.getFood() < GameServerSetting.MAX_FOOD_VALUE) {
					pc.setFood(GameServerSetting.MAX_FOOD_VALUE);
				}
				long nextTime = System.currentTimeMillis() - 10800000;
				if (pc.getLevel() >= 90) {
					nextTime += (pc.getLevel() - 89) * 600000;
				}
				pc.setSurvivalTime(new Timestamp(nextTime));
				pc.sendPackets(new S_PacketBox(S_PacketBox.FOOD, pc.getFood()), true);
				pc.sendPackets(new S_ServerMessage(8010), true);// 생존의 외침 게이지 100% 즉시 충전
				break;
			case 5990:case 5991:// 혈맹 성장 버프 I (1일), 혈맹 성장 버프 II (1일)
			case 5992:case 5993:case 5998:case 5999:// 혈맹 전투 버프 I (2시간), 혈맹 전투 버프 II (2시간), 혈맹 방어 버프 I (2시간), 혈맹 방어 버프 II (2시간)
				isStore = false;
				PledgeItem.contributionBuff(pc, null, itemId);
				break;
			}
			
			if (isStore) {
				item = it.createItem(itemId);
				item.setCount(amount);
				item.setIdentified(true);
				inv.storeItem(item);
			}
			
			logger.addShop(ShopLogType.PLEDGE_BUY, pc, getNpcId(), item, amount, order.getItem().getPrice() * ((amount / order.getItem().getPackCount())));
			if (Config.ALT.SAVE_DB_LOG) {
				if (isStore) {
					LogTable.logNpcShop(pc, getNpcId(), item, order.getItem().getPrice(), amount, 0);
				} else {
					//LogTable.logNpcShopClan(pc, getNpcId(), 0, order.getItem().getItem().getDescKr(), order.getItem().getEnchant(), order.getItem().getPrice(), amount, 0);
					LogTable.logNpcShopClan(pc, getNpcId(), 0, order.getItem().getItem().getDesc(), order.getItem().getEnchant(), order.getItem().getPrice(), amount, 0);
				}
			}
		}
	}

	private void sellCashItems(L1PcInstance pc, L1PcInventory inv, L1ShopBuyOrderList orderList, int npcId) {
		if (!inv.consumeItem(npcId - 5299999, orderList.getTotalPrice()))
			//throw new IllegalStateException("구입에 필요한 코인을 소비할 수 없었습니다.");
			throw new IllegalStateException("Failed to consume the required coins for purchase.");
		buyOrderListExcute(inv, orderList);
	}

	private boolean ensureCashSell(L1PcInstance pc, L1ShopBuyOrderList orderList, int npcId) {
		int price = orderList.getTotalPrice();
		if (!pc.getInventory().checkItem(npcId - 5299999, price)) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("\\aA시스템: \\aG코인이 부족합니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1153), true), true);
			return false;
		}
		return isAvailable(pc, orderList, price);
	}
	
	private void buyOrderListExcute(L1PcInventory inv, L1ShopBuyOrderList orderList){
		L1ItemInstance item		= null;
		ItemTable it			= ItemTable.getInstance();
		LoggerInstance logger	= LoggerInstance.getInstance();
		for (L1ShopBuyOrder order : orderList.getList()) {
			int itemId = order.getItem().getItemId();
			int amount = order.getCount();
			int enchant = order.getItem().getEnchant();
			item = it.createItem(itemId);
			item.setCount(amount);
			item.setIdentified(true);
			item.setEnchantLevel(enchant);
			item.setPackage(true);
			inv.storeItem(item);
			
			ShopLimitInformation limit = ShopLimitLoader.getShopLimit(_npcId, item.getItemId());
			if (limit != null) {
				ShopLimitLoader.getInstance().buyLimitItem(inv.getOwner(), limit, _npcId, item);
			}
			
			logger.addShop(ShopLogType.BUY, inv.getOwner(), getNpcId(), item, amount, order.getItem().getPrice() * ((amount / order.getItem().getPackCount())));
			if (Config.ALT.SAVE_DB_LOG) {
				LogTable.logNpcShop(inv.getOwner(), getNpcId(), item, order.getItem().getPrice(), amount, 0);
			}
		}
	}
	
	private boolean isAvailable(L1PcInstance pc, L1ShopBuyOrderList orderList, int price){
		int currentWeight = pc.getInventory().getWeight() * 1000;
		if (currentWeight + orderList.getTotalWeight() > pc.getMaxWeight() * 1000) {
			pc.sendPackets(L1ServerMessage.sm82);
			return false;
		}
		int totalCount = pc.getInventory().getSize();
		L1Item temp = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			temp = order.getItem().getItem();
			if (temp.isMerge()) {
				if (!pc.getInventory().checkItem(temp.getItemId())) {
					totalCount++;
				}
			} else {
				totalCount++;
			}
		}
		if (totalCount > L1PcInventory.MAX_SIZE) {
			pc.sendPackets(L1ServerMessage.sm263);// \f1한사람의 캐릭터가 가지고 걸을 수 있는 아이템은 최대 180개까지입니다.
			return false;
		}
		if (price <= 0 || price > L1Inventory.MAX_AMOUNT) {
			pc.denals_disconnect(String.format("[L1Shop] PRICE_DENALS : NAME(%s)", pc.getName()));
			return false;
		}
		return true;
	}

}

