package l1j.server.server.clientpackets;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javolution.util.FastTable;
import l1j.server.Config;
import l1j.server.GameSystem.shoplimit.ShopLimitLoader;
import l1j.server.GameSystem.shoplimit.ShopLimitType;
import l1j.server.GameSystem.shoplimit.ShopLimitUser;
import l1j.server.GameSystem.shoplimit.bean.ShopLimitInformation;
import l1j.server.GameSystem.shoplimit.bean.ShopLimitObject;
import l1j.server.GameSystem.tjcoupon.TJCoupon;
import l1j.server.GameSystem.tjcoupon.TJCouponLoader;
import l1j.server.GameSystem.tjcoupon.bean.TJCouponBean;
import l1j.server.common.data.eBloodPledgeRankType;
import l1j.server.server.GameClient;
import l1j.server.server.GameServerSetting;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.controller.DogFightController;
import l1j.server.server.controller.DollRaceController;
import l1j.server.server.datatables.ClanHistoryTable;
import l1j.server.server.datatables.HouseTable;
import l1j.server.server.datatables.ItemSelectorTable;
import l1j.server.server.datatables.ItemSelectorTable.SelectorWarehouseData;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.LogTable;
import l1j.server.server.datatables.NpcShopTable;
import l1j.server.server.datatables.ShopTable;
import l1j.server.server.datatables.ShopTable.L1ShopInfo;
import l1j.server.server.datatables.ShopTable.ShopType;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1HouseLocation;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.shop.L1PersonalShop;
import l1j.server.server.model.shop.L1Shop;
import l1j.server.server.model.shop.L1ShopBuyOrder;
import l1j.server.server.model.shop.L1ShopBuyOrderList;
import l1j.server.server.model.shop.L1ShopSellOrderList;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.warehouse.ClanWarehouse;
import l1j.server.server.model.warehouse.ElfWarehouse;
import l1j.server.server.model.warehouse.PackageWarehouse;
import l1j.server.server.model.warehouse.PrivateWarehouse;
import l1j.server.server.model.warehouse.SpecialWarehouse;
import l1j.server.server.model.warehouse.WarehouseManager;
import l1j.server.server.monitor.Logger.WarehouseType;
import l1j.server.server.monitor.LoggerInstance;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.serverpackets.warehouse.S_WareHouseItemListNoti;
import l1j.server.server.templates.L1House;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1PrivateShopBuyList;
import l1j.server.server.templates.L1PrivateShopSellList;
import l1j.server.server.templates.L1ShopItem;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.response.market.MarketLoader;
//import manager.Manager;  // MANAGER DISABLED

public class C_ShopAndWarehouse extends ClientBasePacket {
	private static final String C_SHOP_WAREHOUSE	= "[C] C_ShopAndWarehouse";
	
	private static final List<Integer> WEAPON_ARMOR_SHOP_IDS = Arrays.asList(new Integer[] {
			70073, 5157, 70032, 70024, 5156, 70016, 70044, 70061, 70083, 70058
	});
	
	private static final List<Integer> RACE_TICKET_SHOP_IDS = Arrays.asList(new Integer[] {
			70035, 70041, 70042
	});
	
	private static final String DWARF		= "L1Dwarf";
	private static final String NPC_SHOP	= "L1NpcShop";
	private static final String MERCHANT	= "L1Merchant";
	private static final String SELECTOR	= "L1Selector";
	private static final String TJCOUPON	= "L1TjCoupon";
	
	private L1PcInstance pc;
	private L1PcInventory inv;
	private int objectId;
	private int resultType;
	private int size;
	private int npcId;
	private String npcImpl;
	private L1Object findObject;
	
	private int bugCount;
	private boolean isPrivateShop, isPrivateNpcShop;
	
	public C_ShopAndWarehouse(byte abyte0[], GameClient client) throws Exception {
		super(abyte0);
		pc = client.getActiveChar();
		if (pc == null) {
			return;
		}
		inv = pc.getInventory();
 	    
		if (pc.getOnlineStatus() == 0) {
			pc.denals_disconnect(String.format("[C_ShopAndWarehouse] NOT_ONLINE_USER : NAME(%s)", pc.getName()));
			return;
		}
		for (L1PcInstance player : L1World.getInstance().getAllPlayers()) {
			if (player.getAccountName().equalsIgnoreCase(client.getAccountName()) && !player.isPrivateShop() && !player.isAutoClanjoin()) {
				bugCount++;
			}
		}
		if (bugCount > 1) {
			pc.denals_disconnect(String.format("[C_ShopAndWarehouse] BUG_COUNT_OVER : NAME(%s)", pc.getName()));
			return;
		}
		
		// 패킷을 읽어 들인다.
		objectId	= readD();
		resultType	= readC();
		size		= readC();
		readP(1);// unknown
		npcImpl		= StringUtil.EmptyString;		
	
		findObject	= L1World.getInstance().findObject(objectId);
		if (findObject != null) {
			if (findObject instanceof L1NpcInstance) {
				L1NpcInstance targetNpc = (L1NpcInstance) findObject;
				npcId	= targetNpc.getNpcTemplate().getNpcId();
				npcImpl	= targetNpc.getNpcTemplate().getImpl();

				// npcshop add
				if (npcImpl.equals(NPC_SHOP)) {
					isPrivateNpcShop = true;
				}
			} else if (findObject instanceof L1PcInstance) {
				isPrivateShop = true;
			}
		}
		if (objectId == 7626) {
			npcId	= Config.ALT.ADEN_SHOP_NPC_ID;// 아덴상점 엔피씨번호 
			npcImpl	= MERCHANT;
		} else if (objectId == Config.PLEDGE.PLEDGE_SHOP_NPC_ID) {
			npcId	= Config.PLEDGE.PLEDGE_SHOP_NPC_ID;// 혈맹상점 엔피씨번호
			npcImpl	= MERCHANT;
		} else if (ItemSelectorTable.isSelectorWareInfo(objectId)){
			npcImpl = SELECTOR;
		} else if (objectId == Config.TJ.TJ_COUPON_ITEMID) {
			npcId	= Config.TJ.TJ_COUPON_ITEMID;
			npcImpl = TJCOUPON;
		}
	
		// if we are trying to STORE an item, we don't need to control the actual invetory size
		if (
			!(resultType == S_WareHouseItemListNoti.eWarehouseType.TRADE_RETRIEVE.toInt() - 1 && npcImpl.equalsIgnoreCase(DWARF)) &&
			!(resultType == 1) &&
			!(resultType == S_WareHouseItemListNoti.eWarehouseType.TRADE_RETRIEVE_PLEDGE.toInt() - 1 && npcImpl.equalsIgnoreCase(DWARF)) &&
			!(resultType == S_WareHouseItemListNoti.eWarehouseType.TRADE_RETRIEVE_ELVEN.toInt() - 1 && npcImpl.equalsIgnoreCase(DWARF) && pc. isElf()) &&
			!(resultType == S_WareHouseItemListNoti.eWarehouseType.TRADE_RETRIEVE_CHAR.toInt() - 1 && npcImpl.equalsIgnoreCase(DWARF) && pc.getSpecialWareHouseSize() > 0)
		) 
		{		
			if (inv.getWeightPercent() >= 100 || size > L1PcInventory.MAX_SIZE) {
				pc.sendPackets(L1ServerMessage.sm82);// 무게 게이지가 부족하거나 인벤토리가 꽉차서 더 들 수 없습니다.
				return;
			}	
		}

		typeSwitch();
	}
	
	private void typeSwitch(){
		if (size <= 0 && resultType != S_WareHouseItemListNoti.eWarehouseType.TRADE_RETRIEVE_PLEDGE.toInt()) {
			return;
		}
		if (resultType == 0) {// 아이템 구매
			if (npcImpl.equalsIgnoreCase(MERCHANT)) {// 아이템 구매
				nomalShopBuy();
			} else if (isPrivateNpcShop) {// npc 개인 상점 구매
				npcPrivateShopBuy();
			} else if (isPrivateShop) {// 개인 상점 아이템 구매
				privateShopBuy();
			}
		} else if (resultType == 1) {// 아이템 판매
			if (npcImpl.equalsIgnoreCase(MERCHANT)) {// 아이템 판매
				nomalShopSell();
			} else if (isPrivateNpcShop) {// Npc 개인 상점 판매
				npcPrivateShopSell();
			} else if (isPrivateShop) {// 개인 상점 판매
				privateShopSell();
			}
		} else if (resultType == S_WareHouseItemListNoti.eWarehouseType.TRADE_RETRIEVE.toInt() - 1 && npcImpl.equalsIgnoreCase(DWARF)) {// 개인 창고 맡기기
			nomalWarehouseIn();
		} else if (resultType == S_WareHouseItemListNoti.eWarehouseType.TRADE_RETRIEVE.toInt()) {// 개인 창고 찾기
			if (npcImpl.equalsIgnoreCase(DWARF)) {
				nomalWarehouseOut();
			} else if (npcImpl.equalsIgnoreCase(SELECTOR)) {
				itemSelector();
			} else if (npcImpl.equalsIgnoreCase(TJCOUPON)) {
				tjCouponSelect();
			}
		} else if (resultType == S_WareHouseItemListNoti.eWarehouseType.TRADE_RETRIEVE_PLEDGE.toInt() - 1 && npcImpl.equalsIgnoreCase(DWARF)) {// 혈맹 창고 맡기기
			clanWarehouseIn();
		} else if (resultType == S_WareHouseItemListNoti.eWarehouseType.TRADE_RETRIEVE_PLEDGE.toInt() && npcImpl.equalsIgnoreCase(DWARF)) {// 혈맹 창고 찾기
			if (size != 0) {
				clanWarehouseOut();// 혈맹 창고 찾기
			} else {
				clanWarehouseCancel();// 혈맹 창고로 꺼낸다 Cancel, 또는, ESC 키
			}
		} else if (resultType == S_WareHouseItemListNoti.eWarehouseType.TRADE_RETRIEVE_ELVEN.toInt() - 1 && npcImpl.equalsIgnoreCase(DWARF) && pc. isElf()) {// 요정 창고 맡기기
			elfWarehouseIn();
		} else if (resultType == S_WareHouseItemListNoti.eWarehouseType.TRADE_RETRIEVE_ELVEN.toInt() && npcImpl.equalsIgnoreCase(DWARF) && pc.isElf()) {// 요정 창고 찾기
			elfWarehouseOut();
		} else if (resultType == S_WareHouseItemListNoti.eWarehouseType.TRADE_REQUEST_COUNT.toInt()) {// 패키지 창고 찾기
			packageWarehousOut();
		} else if (resultType == S_WareHouseItemListNoti.eWarehouseType.TRADE_RETRIEVE_CHAR.toInt() - 1 && npcImpl.equalsIgnoreCase(DWARF) && pc.getSpecialWareHouseSize() > 0) {// 특수 창고 맡기기
			specialWarehouseIn();
		} else if (resultType == S_WareHouseItemListNoti.eWarehouseType.TRADE_RETRIEVE_CHAR.toInt() && npcImpl.equalsIgnoreCase(DWARF)) {// 특수 창고 찾기
			specialWarehouseOut();
		}
	}
	
	/**
	 * 아이템 구매하기
	 */
	private void nomalShopBuy(){
		if (GameServerSetting.SHUTDOWN_SERVER) {
			pc.sendPackets(L1SystemMessage.SERVER_DOWN_SHOP_FAIL);
			return;
		}
		
		if (npcId == 100800 || npcId == 81011) {
			npcId = pc.getType() + 10;// 인센스 마법 10:군주 11:기사 12:요정 13:법사 14:다엘 15:용기사 16:환술사 17:전사
		} else if (npcId == 100801) {
			npcId = pc.getType() + 20;// 사이먼 마법
		} else if (npcId == 100556) {
			npcId = pc.getType() + 30;// 군터의 인장 에반스
		} else if (npcId == 202054) {
			npcId = pc.getType() + 60;// 케슨 수련자 장비
		} else if (WEAPON_ARMOR_SHOP_IDS.contains(npcId)) {
			npcId = pc.getType() + 70;// 무기 방어구 상인
		} else if (npcId == 100600 && pc.isLancer()) {// 신규 클래스 스킬북 상인
			npcId = 100601;
		}
		
		L1Shop shop = ShopTable.getInstance().get(npcId);
		L1ShopBuyOrderList orderList = shop.newBuyOrderList();
		int itemNumber;
		long itemcount;
		if (RACE_TICKET_SHOP_IDS.contains(npcId) && DollRaceController.getInstance().getDollState() != 0) {
			return;
		}
		if (npcId == 170041 && DogFightController.getInstance().getdogState() != 0) {
			return;
		}
		
		if (pc.getClan() == null && pc.getLevel() >= Config.PLEDGE.NOT_PLEDGE_SHOP_ENABLE_LEVEL && !pc.isGm()) {
			//pc.sendPackets(new S_SystemMessage(String.format("%d레벨 이상은 혈맹이 없으면 상점을 이용할 수 없습니다.", Config.PLEDGE.NOT_PLEDGE_SHOP_ENABLE_LEVEL)), true);
			pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(30), String.valueOf(Config.PLEDGE.NOT_PLEDGE_SHOP_ENABLE_LEVEL)), true);
			return;
		}
		
		if (npcId == Config.PLEDGE.PLEDGE_SHOP_NPC_ID && size > 1) {
			pc.sendPackets(L1SystemMessage.ONE_BUY_MSG);
			return;
		}
		
		if (shop.getSellingItems().size() < size) {
			//System.out.println(String.format("상점이 판매하는 아이템 수(%d)보다 더 많이 사려고 함.(%d)개", shop.getSellingItems().size(), size));
			System.out.println(String.format("You want to buy more (%d) items than the store sells (%d)", shop.getSellingItems().size(), size));
			pc.denals_disconnect(String.format("[C_ShopAndWarehouse] SELL_SIZE_OVER_REQ : NAME(%s)", pc.getName()));
			return;
		}
		
		L1ShopInfo info = ShopTable.getInstance().getShopInfo(npcId);
		for (int i = 0; i < size; i++) {
			itemNumber	= readD();
			itemcount	= readD();
			if (itemcount <= 0) {
				return;
			}
			if (info != null && info.getType() == ShopType.N_COIN && itemNumber == 0) {// NCOIN 보유 수량 표기 순번
				pc.sendPackets(L1SystemMessage.ITEM_EMPTY);
				return;
			}
			if (((npcId >= 6100000 && npcId <= 6100045) || npcId == 81026 || npcId == Config.PLEDGE.PLEDGE_SHOP_NPC_ID) && itemcount > 1) {
				pc.sendPackets(L1SystemMessage.ONE_BUY_MSG);
				return;
			}
			
			if (npcId >= 800501 && npcId <= 800503) {// 공성전 참가 패키지
				if (itemcount > 1) {
					pc.sendPackets(L1SystemMessage.ONE_BUY_MSG);
					return;
				}
				if (inv.checkItem(130000) || inv.checkItem(130001)) {
					pc.sendPackets(L1SystemMessage.BUY_COUNT_MAX);
					continue;
				}
			}

			if (itemcount <= 0 || itemcount >= 10000) {
				return;
			}
			if (orderList.getTotalPrice() > L1Inventory.MAX_AMOUNT) {
				pc.sendPackets(L1SystemMessage.BUY_ADENA_MAX_COUNT_FAIL);
				return;
			}
			orderList.add(itemNumber, (int) itemcount, pc);
			if (orderList.BugOk() != 0) {
				//S_SystemMessage message = new S_SystemMessage(String.format("%s님 상점 최대구매 수량초과 (%d)", pc.getName(), itemcount));
				S_ServerMessage message = new S_ServerMessage(S_ServerMessage.getStringIdx(31), pc.getName(), String.valueOf(itemcount));
				for (L1PcInstance gm : L1World.getInstance().getAllGms()) {
					gm.sendPackets(message, true);
				}
				pc.sendPackets(message, true);
			}
		}
		int bugok = orderList.BugOk();
		if (bugok == 0){
			for (L1ShopBuyOrder sbo : orderList.getList()) {
				L1ShopItem shop_item = sbo.getItem();
				if (npcId == Config.PLEDGE.PLEDGE_SHOP_NPC_ID && !isValidationPledgeShop(pc.getClan(), shop_item.getItemId(), shop_item)) {
					return;
				}
				
				ShopLimitInformation limit = ShopLimitLoader.getShopLimit(npcId, shop_item.getItemId());
				if (limit != null) {
					if (!isValidationShopBuyLimit(limit, sbo)) {
						return;
					}
				} else if (shop_item.getItemId() == 6022 && inv.checkItem(6020)) {
					pc.sendPackets(L1SystemMessage.BUY_COUNT_MAX);
					return;
				}
			}
			shop.sellItems(pc, orderList);
			pc.saveInventory();// 아이템저장시킴
		}
	}
	
	/**
	 * 아이템 판매하기
	 */
	private void nomalShopSell(){
		L1Shop shop = ShopTable.getInstance().get(npcId);
		L1ShopSellOrderList orderList = shop.newSellOrderList(pc);
		int itemNumber;
		long itemcount;
		L1ItemInstance item	= null;
		for (int i = 0; i < size; i++) {
			itemNumber	= readD();
			itemcount	= readD();
			item		= inv.getItem(itemNumber);
			if (item == null) {
				continue;
			}
			if (itemcount <= 0) {
				return;
			}
			if (npcId >= 6100000 && npcId <= 6100045 && !inv.getItem(itemNumber).isPackage()) {
				pc.sendPackets(L1SystemMessage.PACKAGE_SHOP_OTHER_ITEM);
				return;
			}
			if (!item.getItem().isCantSell() || item.isSlot()) {
				continue;
			}
			orderList.add(itemNumber, (int) itemcount, pc);
		}
		int bugok = orderList.BugOk();
		if (bugok == 0) {
			shop.buyItems(orderList);
			pc.saveInventory();// 백섭복사 방지 수량성버그방지
		}
	}
	
	/**
	 * 개인창고 맡기기
	 */
	private void nomalWarehouseIn(){
		if (pc.getLevel() < 20) {
			pc.sendPackets(L1SystemMessage.WAREHOUSE_20LEVEL);
			return;
		}
		if (pc.getSkill().hasSkillEffect(L1SkillId.SET_BUFF)) {
            pc.sendPackets(L1SystemMessage.NOTI_BUFF_DELAY_CHECK);
            return;
        }
		if (pc.isTwoLogin()) {
			return;
		}
		int objectId, count;
		L1ItemInstance item = null;
		long nowtime = System.currentTimeMillis();
		for (int i = 0; i < size; i++) {
			objectId	= readD();
			count		= readD();
			item		= inv.getItem(objectId);
			if (item == null) {
				break;
			}
			if (item.getItemDelay() >= nowtime) {
				break;
			}
			if (count > item.getCount()) {
				count = item.getCount();
			}
			// we are trying to deposit an item, not necesary to check if our weight is ok...
			if (!isAvailableWarehouse(objectId, item, count) /*|| !isAvailablePcWeight(item, count)*/) {
				break;
			}
			if (!isRetriveCheck(item)) {
				continue;
			}

			PrivateWarehouse warehouse = WarehouseManager.getInstance().getPrivateWarehouse(pc.getAccountName());
			if (warehouse == null) {
				break;
			}
			if (warehouse.checkAddItemToWarehouse(item, count) == L1Inventory.SIZE_OVER) {
				pc.sendPackets(L1ServerMessage.sm75); // \f1상대가 물건을 너무 가지고 있어 거래할 수 없습니다.
				break;
			}

			inv.tradeItem(objectId, count, warehouse);
			pc.getLight().turnOnOffLight();
				
			if (Config.ALT.SAVE_DB_LOG) {
				LogTable.logWarehouse(pc, item, count, 0);
			}
			//Manager.getInstance().WarehouseAppend(item.getLogName(), count, pc.getName(), 0); // MANAGER DISABLED
			LoggerInstance.getInstance().addWarehouse(WarehouseType.Private, true, pc, item, count);
		}
	}
	
	/**
	 * 개인창고 찾기
	 */
	private void nomalWarehouseOut(){
		if (pc.getLevel() < 20) {
			pc.sendPackets(L1SystemMessage.WAREHOUSE_20LEVEL);
			return;
		}
		if (pc.getSkill().hasSkillEffect(L1SkillId.SET_BUFF)) {
			pc.sendPackets(L1SystemMessage.NOTI_BUFF_DELAY_CHECK);
			return;
		}
		if (pc.isTwoLogin()) {
			return;				
		}
		if (!inv.checkItem(L1ItemId.ADENA, 100 * size)) {
			pc.sendPackets(L1ServerMessage.sm189);
			return;
		}
		
		PrivateWarehouse warehouse			= WarehouseManager.getInstance().getPrivateWarehouse(pc.getAccountName());
		if (warehouse == null) {
			return;
		}
		ArrayList<L1ItemInstance> itemList	= new ArrayList<L1ItemInstance>();
		ArrayList<Integer> list_count		= new ArrayList<Integer>();
		L1ItemInstance item = null;
		int count, itemnum;
		long nowtime = System.currentTimeMillis();
		for (int i = 0; i < size; i++) {
			itemnum	= readD();// 인덱스
			count	= readD();
			item	= warehouse.getItems().get(itemnum);
			/**   창고 찾기 부분 버그 방지 **/
			if (item == null) {
				return;
			}
			if (item.getItemDelay() >= nowtime) {
				break;
			}
			if (count > item.getCount()) {
				count = item.getCount();
			}
			if (!isAvailableTrade(item, count) || !isAvailablePcWeight(item, count)) {
				return;
			}
			if (!item.getItem().isToBeSavedAtOnce()) {
				inv.saveItem(item, L1PcInventory.COL_COUNT);
			}
			itemList.add(item);
			list_count.add(count);
		}
		
		L1ItemInstance item1 = null;
		for (int i = 0; i < itemList.size(); i++) {
			item1			= itemList.get(i);
			int item_count	= list_count.get(i);
			if (inv.checkAddItem(item1, item_count) == L1Inventory.OK) {// 용량 중량 확인 및 메세지 송신
				if (inv.consumeItem(L1ItemId.ADENA, 100)) {
					warehouse.tradeItem(null, item1, item_count, inv);	
					/*if (((item_count >= 100) && ((item1.getItemId() == L1ItemId.PIXIE_FEATHER) 
							|| (item1.getItemId() == 40087) || (item1.getItemId() == 40074)))
							|| (item1.getEnchantLevel() > 0)
							|| ((item_count >= 1000000) && (item1.getItemId() == L1ItemId.ADENA))
							|| ((item_count >= 1000) && (item1.getItemId() != L1ItemId.ADENA))) {
						//Manager.getInstance().WarehouseAppend(item1.getLogName(), item_count, pc.getName(), 1); // MANAGER DISABLED
					}*/  // MANAGER DISABLED
					if (Config.ALT.SAVE_DB_LOG) {
						LogTable.logWarehouse(pc, item1, item_count, 1);
					}
					LoggerInstance.getInstance().addWarehouse(WarehouseType.Private, false, pc, item1, item_count);	
				} else {
					pc.sendPackets(L1ServerMessage.sm189); // \f1아데나가 부족합니다.
					break;
				}
			} else {
				pc.sendPackets(L1ServerMessage.sm270); // \f1 가지고 있는 것이 무거워서 거래할 수 없습니다.
				break;
			}
		}
		itemList.clear();
		list_count.clear();
	}
	
	/**
	 * 혈맹 창고 맡기기
	 */
	private void clanWarehouseIn(){
		if (pc.getClanid() <= 0) {
			pc.sendPackets(L1ServerMessage.sm208);// 창고: 혈맹 창고 이용 불가(혈맹 미가입)
			return;
		}
		if (pc.getLevel() < 20) {
			pc.sendPackets(L1SystemMessage.WAREHOUSE_20LEVEL);
			return;
		}
		if (pc.getSkill().hasSkillEffect(L1SkillId.SET_BUFF)) {
			pc.sendPackets(L1SystemMessage.NOTI_BUFF_DELAY_CHECK);
			return;
		}
		if (pc.isTwoLogin()) {
			return;
		}
		
		int objectId, count;
		L1ItemInstance item = null;
		L1Clan clan = null;
		long nowtime = System.currentTimeMillis();
		for (int i = 0; i < size; i++) {
			objectId	= readD();
			count		= readD();
			item		= inv.getItem(objectId);
			if (item == null) {
				return;
			}
			
			if (item.getItemDelay() >= nowtime) {
				break;
			}
			if (count > item.getCount()) {
				count = item.getCount();
			}
			if (!isAvailableWarehouse(objectId, item, count)) {
				return;
			}
			clan = pc.getClan();
			if (clan != null) {
				if (!item.getItem().isTradable() || item.getBless() >= 128) {
					pc.sendPackets(new S_ServerMessage(210, item.getItem().getDesc()), true); // \f1%0은 버리거나 또는 타인에게 양일을 할 수 없습니다.
					continue;
				}
				
				if (!isRetriveCheck(item)) {
					continue;
				}
				
				ClanWarehouse clanWarehouse = WarehouseManager.getInstance().getClanWarehouse(clan.getClanName());
				if (clanWarehouse.checkAddItemToWarehouse(item, count) == L1Inventory.SIZE_OVER) {
					pc.sendPackets(L1ServerMessage.sm75);// \f1상대가 물건을 너무 가지고 있어 거래할 수 없습니다.
					break;
				}
				inv.tradeItem(objectId, count, clanWarehouse);
				pc.getLight().turnOnOffLight();
				
				//ClanHistoryTable.getInstance().add(pc.getClan(), 0, pc.getName(), item.getDescKr(), count);
				ClanHistoryTable.getInstance().add(pc.getClan(), 0, pc.getName(), item.getDescEn(), count);
				if (Config.ALT.SAVE_DB_LOG) {
					LogTable.logClanWarehouse(pc, item, count, 0);
				}
				//Manager.getInstance().EPWarehouseAppend(item.getLogName(), count, pc.getName(), 2); // MANAGER DISABLED
				LoggerInstance.getInstance().addWarehouse(WarehouseType.Clan, true, pc, item, count);
			}
		}
	}
	
	/**
	 * 혈맹 창고 찾기
	 */
	private void clanWarehouseOut(){
		if (pc.getLevel() < 20) {
			pc.sendPackets(L1SystemMessage.WAREHOUSE_20LEVEL);
			return;
		}
		
		if (pc.getSkill().hasSkillEffect(L1SkillId.SET_BUFF)) {
			pc.sendPackets(L1SystemMessage.NOTI_BUFF_DELAY_CHECK);
			return;
		}
		
		if (!inv.checkItem(L1ItemId.ADENA, 100 * size)) {
			pc.sendPackets(L1ServerMessage.sm189);
			return;
		}

		if (pc.isTwoLogin()) {
			return;
		}
		L1Clan clan = pc.getClan();
		if (clan == null) {
			return;
		}
		ArrayList<L1ItemInstance> itemList	= new ArrayList<L1ItemInstance>();
		ArrayList<Integer> list_count		= new ArrayList<Integer>();
		ClanWarehouse clanWarehouse = WarehouseManager.getInstance().getClanWarehouse(clan.getClanName());
		for (int i = 0; i < size; i++) {
			int itemnum, count;
			itemnum	= readD();
			count	= readD();
			L1ItemInstance item = clanWarehouse.getItems().get(itemnum);   				

			//** 클랜 창고 찾기 부분 방어 **//		
			if (item == null) {
				return;
			}
			if (count >= item.getCount()) {
				count = item.getCount();
			}
			if (!isAvailableTrade(item, count) || !isAvailablePcWeight(item, count)) {
				return;
			}
			itemList.add(item);
			list_count.add(count);
		}
		
		for (int i = 0; i < itemList.size(); i++) {
			L1ItemInstance item = itemList.get(i);
			int item_count = list_count.get(i);	 

			if (inv.checkAddItem(item, item_count) == L1Inventory.OK) { // 용량 중량 확인 및 메세지 송신
				if (inv.consumeItem(L1ItemId.ADENA, 100 * size)) {
					clanWarehouse.tradeItem(null, item, item_count, inv);
					//ClanHistoryTable.getInstance().add(pc.getClan(), 1, pc.getName(), item.getDescKr(), item_count);
					ClanHistoryTable.getInstance().add(pc.getClan(), 1, pc.getName(), item.getDescEn(), item_count);
					/*if ((item_count >= 100 
							&& (item.getItemId() == L1ItemId.PIXIE_FEATHER || item.getItemId() == 40087 || item.getItemId() == 40074))
							|| item.getEnchantLevel() > 0
							|| (item_count >= 1000000 && item.getItemId() == L1ItemId.ADENA)
							|| (item_count >= 1000 && item.getItemId() != L1ItemId.ADENA)) {
						//Manager.getInstance().EPWarehouseAppend(item.getLogName(), item_count, pc.getName(), 3);
					} */ // MANAGER DISABLED
					if (Config.ALT.SAVE_DB_LOG) {
						LogTable.logClanWarehouse(pc, item, item_count, 1);
					}
					LoggerInstance.getInstance().addWarehouse(WarehouseType.Clan, false, pc, item, item_count);
				} else {
					pc.sendPackets(L1ServerMessage.sm189);// \f1아데나가 부족합니다.
					break;
				}
			} else {
				pc.sendPackets(L1ServerMessage.sm270);// \f1 가지고 있는 것이 무거워서 거래할 수 없습니다.
				break;
			}
		}
		itemList.clear();
		list_count.clear();
		clanWarehouse.setWarehouseUsingChar(0, 0);
	}
	
	/**
	 * 혈맹 창고 이용 취소
	 */
	private void clanWarehouseCancel(){
		L1Clan clan = pc.getClan();
		if (clan == null) {
			return;
		}
		if (pc.getSkill().hasSkillEffect(L1SkillId.SET_BUFF)){
			pc.sendPackets(L1SystemMessage.NOTI_BUFF_DELAY_CHECK);
			return;
		}
		ClanWarehouse clanWarehouse = WarehouseManager.getInstance().getClanWarehouse(clan.getClanName());
		clanWarehouse.setWarehouseUsingChar(0, 0);
	}
	
	/**
	 * 요정족 창고 맡기기
	 */
	private void elfWarehouseIn(){
		if (pc.getLevel() < 20) {
			pc.sendPackets(L1SystemMessage.WAREHOUSE_20LEVEL);
			return;
		}
		if (pc.getSkill().hasSkillEffect(L1SkillId.SET_BUFF)) {
			pc.sendPackets(L1SystemMessage.NOTI_BUFF_DELAY_CHECK);
			return;
		}
		if (pc.isTwoLogin()) {
			return;
		}
		L1ItemInstance item = null;
		int objectId, count;
		for (int i = 0; i < size; i++) {
			objectId	= readD();
			count		= readD();
			item		= inv.getItem(objectId);
			if (item == null) {
				return;
			}
			if (count > item.getCount()) {
				count = item.getCount();
			}
			if (!isAvailableWarehouse(objectId, item, count)) {
				return;
			}
			
			if (!isRetriveCheck(item)) {
				continue;
			}

			ElfWarehouse elfwarehouse = WarehouseManager.getInstance().getElfWarehouse(pc.getAccountName());
			if (elfwarehouse.checkAddItemToWarehouse(item, count) == L1Inventory.SIZE_OVER) {
				pc.sendPackets(L1ServerMessage.sm75); // \f1상대가 물건을 너무 가지고 있어 거래할 수 없습니다.
				break;
			}
			inv.tradeItem(objectId, count, elfwarehouse);
			pc.getLight().turnOnOffLight();
			
			if ((item.getCount() >= 100 
					&& (item.getItemId() == L1ItemId.PIXIE_FEATHER || item.getItemId() == 40087 || item.getItemId() == 40074))
					|| item.getEnchantLevel() > 0
					|| (item.getCount() >= 1000000 && item.getItemId() == L1ItemId.ADENA)
					|| (item.getCount() >= 1000 && item.getItemId() != L1ItemId.ADENA)) {
				//Manager.getInstance().EPWarehouseAppend(item.getLogName(), count, item.getDescKr(), 0); // MANAGER DISABLED
			}
			/** 로그파일저장**/
			LoggerInstance.getInstance().addWarehouse(WarehouseType.Elf, true, pc, item, count);
		}
	}
	
	/**
	 * 요정족 창고 찾기
	 */
	private void elfWarehouseOut(){
		if (pc.getLevel() < 20) {
			pc.sendPackets(L1SystemMessage.WAREHOUSE_20LEVEL);
			return;
		}
		if (pc.getSkill().hasSkillEffect(L1SkillId.SET_BUFF)) {
			pc.sendPackets(L1SystemMessage.NOTI_BUFF_DELAY_CHECK);
			return;
		}
		if (pc.isTwoLogin()) {
			return;					
		}

		if (!inv.checkItem(40494, 4 * size)) {
			pc.sendPackets(L1ServerMessage.sm337_MISLIL);
			return;
		}
		
		ElfWarehouse elfwarehouse = WarehouseManager.getInstance().getElfWarehouse(pc.getAccountName());
		if (elfwarehouse == null) {
			return;
		}
		ArrayList<L1ItemInstance> itemList = new ArrayList<L1ItemInstance>();
		ArrayList<Integer> list_count = new ArrayList<Integer>();
		int count, itemnum;
		L1ItemInstance item = null;
		L1ItemInstance item1 = null;
		long nowtime = System.currentTimeMillis();
		for (int i = 0; i < size; i++) {
			itemnum	= readD();
			count	= readD();
			item	= elfwarehouse.getItems().get(itemnum);
			if (item == null) {
				return;
			}
			if (item.getItemDelay() >= nowtime) {
				break;
			}
			if (count > item.getCount()) {
				count = item.getCount();
			}
			if (!isAvailableTrade(item, count) || !isAvailablePcWeight(item, count)) {
				return;
			}
			if (!item.getItem().isToBeSavedAtOnce()) {
				inv.saveItem(item, L1PcInventory.COL_COUNT);
			}
			itemList.add(item);
			list_count.add(count);
		}
		
		for (int i = 0; i < itemList.size(); i++) {
			item1 = itemList.get(i);
			int item_count = list_count.get(i);
			if (inv.checkAddItem(item1, item_count) == L1Inventory.OK) {// 용량 중량 확인 및 메세지 송신
				if (inv.consumeItem(40494, 4)) {
					elfwarehouse.tradeItem(null, item1, item_count, inv);	
					/*if ((item_count >= 100 
							&& (item1.getItemId() == L1ItemId.PIXIE_FEATHER || item1.getItemId() == 40087 || item1.getItemId() == 40074))
							|| item1.getEnchantLevel() > 0
							|| (item_count >= 1000000 && item1.getItemId() == L1ItemId.ADENA)
							|| (item_count >= 1000 && item1.getItemId() != L1ItemId.ADENA)) {
						//Manager.getInstance().WarehouseAppend(item1.getLogName(), item_count, pc.getName(), 1);
					}*/ // MANAGER DISABLED
					if (Config.ALT.SAVE_DB_LOG) {
						LogTable.logWarehouse(pc, item1, item_count, 1);
					}
					LoggerInstance.getInstance().addWarehouse(WarehouseType.Elf, false, pc, item1, item_count);	
				} else {
					pc.sendPackets(L1ServerMessage.sm189); // \f1아데나가 부족합니다.
					break;
				}
			} else {
				pc.sendPackets(L1ServerMessage.sm270); // \f1 가지고 있는 것이 무거워서 거래할 수 없습니다.
				break;
			}
		}
		itemList.clear();
		list_count.clear();
	}
	
	/**
	 * 패키지 창고 찾기
	 */
	private void packageWarehousOut(){
		if (!inv.checkItem(L1ItemId.ADENA, 30 * size)) {
			pc.sendPackets(L1ServerMessage.sm189);
			return;
		}
		PackageWarehouse warehouse = WarehouseManager.getInstance().getPackageWarehouse(pc.getAccountName());
		if (warehouse == null) {
			return;
		}
		ArrayList<L1ItemInstance> itemList	= new ArrayList<L1ItemInstance>();
		ArrayList<Integer> list_count		= new ArrayList<Integer>();
		int itemnum, count;
		L1ItemInstance item = null;
		for (int i = 0; i < size; i++) {
			itemnum = readD();// 아이템 오브젝트
			readD();
			count = readD();
			
			for (L1ItemInstance obj : warehouse.getItems()) {
				if (obj.getId() == itemnum) {
					item = obj;
					break;
				}
			}
			
			if (item == null) {
				return;
			}
			if (count > 0) {
				count = item.getCount();// 부가 아이테 창고는 수량 전체 선택 되도록
			}
			if (!isAvailableTrade(item, count) || !isAvailablePcWeight(item, count)) {
				break;
			}
			
			itemList.add(item);
			list_count.add(count);
		}
		
		L1ItemInstance item1 = null;
		for (int i = 0; i < itemList.size(); i++) {
			item1 = itemList.get(i);
			int item_count = list_count.get(i);
			warehouse.tradeItem(null, item1, item_count, inv);
			
			/** 자동사냥 인증 아이템 **/
			if (item1.getItem().getItemId() == 3000211) {
				setDeleteTime(item1, 8640);
			} else if (item1.getItem().getItemId() == 3000210) {
				setDeleteTime(item1, 4320);
			} else if (item1.getItem().getItemId() == 3000209) {
				setDeleteTime(item1, 1440);
			}
			/** 로그파일저장 **/
			LoggerInstance.getInstance().addWarehouse(WarehouseType.Package, false, pc, item1, item_count);
		}
	}
	
	/**
	 * 특수 창고 맡기기
	 */
	private void specialWarehouseIn(){
		if (pc.getLevel() < 5) {
			pc.sendPackets(L1SystemMessage.WAREHOUSE_5LEVEL);
			return;
		}
		if (pc.isTwoLogin()) {
			return;
		}
		SpecialWarehouse warehouse = WarehouseManager.getInstance().getSpecialWarehouse(pc.getName());
		if (warehouse == null) {
			return;
		}
		boolean retrieve = false;
		L1ItemInstance item = null;
		for (int i = 0, objectId, count; i < size; i++) {
			retrieve	= true;
			objectId	= readD();
			count		= readD();
			item		= inv.getItem(objectId);
			if (item == null) {
				return;
			}
			if (count > item.getCount()) {
				count = item.getCount();
			}
 			// we are trying to deposit an item, not necesary to check if our weight is ok...
			if (!isAvailableWarehouse(objectId, item, count) /*|| !isAvailablePcWeight(item, count)*/) {
				return;
			}
			
			if (!item.getItem().isSpecialRetrieve()) {
				pc.sendPackets(new S_ServerMessage(210, item.getItem().getDesc()), true);// \f1%0은 버리거나 또는 타인에게 양일을 할 수 없습니다.
				continue;
			}
			/** 시간제아이템 창고불가 */
			if (item.getEndTime() != null){
				pc.sendPackets(L1ServerMessage.sm125);
				continue;
			}
			
			if (warehouse.checkAddItemToSpecialWarehouse(pc, item, count) == L1Inventory.SIZE_OVER) {
				pc.sendPackets(L1ServerMessage.sm75);
				break;
			}
			
			if (retrieve) {
				inv.tradeItem(objectId, count, warehouse);
				pc.getLight().turnOnOffLight();

				/** 로그파일저장 **/
				LoggerInstance.getInstance().addWarehouse(WarehouseType.Char, true, pc, item, count);
			}
		}
	}
	
	/**
	 * 특수 창고 찾기
	 */
	private void specialWarehouseOut(){
		if (pc.getLevel() < 5) {
			pc.sendPackets(L1SystemMessage.WAREHOUSE_5LEVEL);
			return;
		}
		if (pc.getSkill().hasSkillEffect(L1SkillId.SET_BUFF)) {
			pc.sendPackets(L1SystemMessage.NOTI_BUFF_DELAY_CHECK);
			return;
		}
		if (pc.isTwoLogin()) {
			return;
		}
		if (!inv.checkItem(L1ItemId.ADENA, 100 * size)) {
			pc.sendPackets(L1ServerMessage.sm189);
			return;
		}
		
		SpecialWarehouse warehouse = WarehouseManager.getInstance().getSpecialWarehouse(pc.getName());
		if (warehouse == null) {
			return;
		}
		ArrayList<L1ItemInstance> itemList	= new ArrayList<L1ItemInstance>();
		ArrayList<Integer> list_count		= new ArrayList<Integer>();
		int count, itemnum;
		L1ItemInstance item = null;
		long nowtime = System.currentTimeMillis();
		for (int i = 0; i < size; i++) {
			itemnum	= readD();
			count	= readD();
			item	= warehouse.getItems().get(itemnum);
			if (item == null) {
				return;
			}
			if (item.getItemDelay() >= nowtime) {
				break;
			}
			if (count > item.getCount()) {
				count = item.getCount();
			}
			if (!isAvailableTrade(item, count) || !isAvailablePcWeight(item, count)) {
				return;
			}
			if (!item.getItem().isToBeSavedAtOnce()) {
				inv.saveItem(item, L1PcInventory.COL_COUNT);
			}
			itemList.add(item);
			list_count.add(count);
		}
		
		L1ItemInstance item1 = null;
		for (int i = 0; i < itemList.size(); i++) {
			item1 = itemList.get(i);
			int item_count = list_count.get(i);
			if (inv.checkAddItem(item1, item_count) == L1Inventory.OK) {// 용량 중량 확인 및 메세지 송신
				if (inv.consumeItem(L1ItemId.ADENA, 100)) {
					warehouse.tradeItem(null, item1, item_count, inv);
					if ((item_count >= 100 && (item1.getItemId() == L1ItemId.PIXIE_FEATHER || item1.getItemId() == 40087 || item1.getItemId() == 40074))
							|| item1.getEnchantLevel() > 0
							|| (item_count >= 1000000 && item1.getItemId() == L1ItemId.ADENA)
							|| (item_count >= 1000 && item1.getItemId() != L1ItemId.ADENA)) {
					LoggerInstance.getInstance().addWarehouse(WarehouseType.Char, false, pc, item1, item_count);
					}
				} else {
					pc.sendPackets(L1ServerMessage.sm189); // \f1아데나가 부족합니다.
					break;
				}
			} else {
				pc.sendPackets(L1ServerMessage.sm270); // \f1 가지고 있는 것이 무거워서 거래할 수 없습니다.
				break;
			}
		}
		itemList.clear();
		list_count.clear();
	}
	
	/**
	 * 개인상점(NPC) 구매
	 */
	private void npcPrivateShopBuy(){
		L1Shop shop = NpcShopTable.getInstance().get(npcId);
		L1ShopBuyOrderList orderList = shop.newBuyOrderList();
		int index; long itemcount;
		for (int i = 0; i < size; i++) {
			index		= readD();
			itemcount	= readD();
			if (itemcount <= 0) {
				return;
			}
			if (size >= 2) { // 동시에 다른물건을 살수없게 2개가 선택된다면,
				pc.sendPackets(L1SystemMessage.TWO_ITEM_BUY_FAIL);
				return;
			}
			if (pc.getMapId() == 800 && itemcount > 15) {// 15개 제한
				pc.sendPackets(L1SystemMessage.BUY_ITEM_MAX_COUNT_MSG);
				return;
			}
			orderList.add(index, (int)itemcount, pc); 
		}
		int bugok = orderList.BugOk();
		if (bugok == 0) {
			shop.sellItems(pc, orderList);
			pc.saveInventory();// 백섭복사 방지 수량성버그방지
		}
	}
	
	/**
	 * 개인상점(NPC) 판매
	 */
	private void npcPrivateShopSell(){
		L1Shop shop = NpcShopTable.getInstance().get(npcId);
		L1ShopSellOrderList orderList = shop.newSellOrderList(pc);
		
		L1ItemInstance item = null;
		int itemNumber; long itemcount;
		for (int i = 0; i < size; i++) {
			itemNumber	= readD();
			itemcount	= readBit();// 3자리로 호출됨
			item		= inv.getItem(itemNumber);
			
			if (item == null || !item.getItem().isCantSell()) {
				continue;
			}
			if (item.getBless() >= 128) {
				pc.sendPackets(new S_ServerMessage(210, item.getItem().getDesc()), true); // \f1%0은 버리거나 또는 타인에게 양일을 할 수 없습니다.
				return;
			}
			
			if (!item.getItem().isTradable() || item.isSlot()) {
				pc.sendPackets(L1ServerMessage.sm941);// 거래 불가 아이템입니다.
				return;
			}
			
			if (itemcount <= 0 || itemcount > 9999 || itemcount > item.getCount()) {
				return;
			}
			orderList.add(itemNumber, (int)itemcount , pc); 
		}
		int bugok = orderList.BugOk();
		if (bugok == 0) {
			shop.buyItems(orderList);
			pc.saveInventory();// 백섭복사 방지 수량성버그방지
		}
	}
	
	/**
	 * 개인상점 구매
	 */
	private void privateShopBuy(){
		L1PcInstance targetPc = null;
		if (findObject instanceof L1PcInstance) {
			targetPc = (L1PcInstance) findObject;
		}
		if (targetPc == null) {
			return;
		}
		int order, count, price;
		ArrayList<L1PrivateShopSellList> sellList = targetPc.getSellList();
		L1PrivateShopSellList pssl;
		int itemObjectId, sellPrice, sellTotalCount, sellCount;
		synchronized (sellList) {
			// 품절이 발생해, 열람중의 아이템수와 리스트수가 다르다
			if (pc.getPartnersPrivateShopItemCount() != sellList.size()) {
				return;
			}

			L1ItemInstance item;
			boolean[] isRemoveFromList = new boolean[8];
			long nowtime = System.currentTimeMillis();
			for (int i = 0; i < size; i++) { // 구입 예정의 상품
				order = readD();
				count = readD();
				
				pssl			= (L1PrivateShopSellList) sellList.get(order);
				itemObjectId	= pssl.getItemObjectId();
				sellPrice		= pssl.getSellPrice();
				sellTotalCount	= pssl.getSellTotalCount(); // 팔 예정의 개수
				sellCount		= pssl.getSellCount(); // 판 누계
				item			= targetPc.getInventory().getItem(itemObjectId);
				
				if (item == null || item.getItemDelay() >= nowtime) {
					break;
				}
				if (count > sellTotalCount - sellCount) {
					count = sellTotalCount - sellCount;
				}
				if (count <= 0) {
					break;
				}
				if (item.isEquipped()) {
					pc.sendPackets(L1ServerMessage.sm905); // 장비 하고 있는 아이템 구매못하게.
					continue;
				}
				
				if (inv.checkAddItem(item, count) == L1Inventory.OK) { // 용량 중량 확인 및 메세지 송신					
					for (int j = 0; j < count; j++) { // 오버플로우를 체크
						if (sellPrice * j > L1Inventory.MAX_AMOUNT || sellPrice * j < 0) {
							pc.sendPackets(new S_ServerMessage(904, "2000000000"), true); // 총판 매가격은%d아데나를 초과할 수 없습니다.
							return;
						}
					} 

					/** 개인상점 버그방지 **/  					
					if (itemObjectId != item.getId()) {
						pc.denals_disconnect(String.format("[C_ShopAndWarehouse] PACKET_DENALS : NAME(%s)", pc.getName()));
						targetPc.denals_disconnect(String.format("[C_ShopAndWarehouse] PACKET_DENALS : NAME(%s)", pc.getName()));
						return;
					}
					if (!item.isMerge() && count != 1) {
						pc.denals_disconnect(String.format("[C_ShopAndWarehouse] NOT_MERGE_ITEM_COUNT_NOT_ONE : NAME(%s)", pc.getName()));
						targetPc.denals_disconnect(String.format("[C_ShopAndWarehouse] NOT_MERGE_ITEM_COUNT_NOT_ONE : NAME(%s)", pc.getName()));
						return;
					}
					if (count <= 0 || item.getCount() <= 0 || item.getCount() < count) {
						return;
					}
					
					price = count * sellPrice;	
					if (price <= 0 || price > L1Inventory.MAX_AMOUNT) {
						return;
					}
					if (!isAvailablePrivateShop(targetPc, itemObjectId, item, count)) {
						break;
					}
					if (count >= item.getCount()) {
						count = item.getCount();
					}
					if (count > 9999) {
						break;
					}
					/** 개인상점 버그방지 **/  

					if (inv.checkItem(L1ItemId.ADENA, price)) {
						L1ItemInstance adena = inv.findItemId(L1ItemId.ADENA);
						if (targetPc != null && adena != null) {
							if (targetPc.getInventory().tradeItem(item, count, inv) == null) {
								return;
							}
							inv.tradeItem(adena, price, targetPc.getInventory());			
							targetPc.sendPackets(new S_ServerMessage(877, pc.getName(), String.format("%s (%d)", item.getItem().getDesc(), count)), true);// %1%o에 판매했습니다.
							
							pssl.setSellCount(count + sellCount);
							sellList.set(order, pssl);
							//Manager.getInstance().ShopAppend(item.getLogName(), count, price, targetPc.getName(), pc.getName()); // MANAGER DISABLED
							LoggerInstance.getInstance().addPrivateShop(true, pc, targetPc, item, item.getCount());
							if (Config.ALT.SAVE_DB_LOG) {
								LogTable.logShop(pc, targetPc, item, sellPrice, count, 0);
							}
							if (pssl.getSellCount() == pssl.getSellTotalCount()) { // 팔 예정의 개수를 팔았다
								isRemoveFromList[order] = true;
								try {
									L1PersonalShop.delete(targetPc, item.getId(), 0);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							try {
								pc.saveInventory();
								targetPc.saveInventory();
							} catch (Exception e) {
								e.printStackTrace();
							}
							if (Config.WEB.WEB_SERVER_ENABLE) {
								MarketLoader.getInstance().updateItem(targetPc.getName(), item.getId(), pssl.getSellTotalCount() - pssl.getSellCount(), 0);
							}
						}
					} else {
						pc.sendPackets(L1ServerMessage.sm189); // \f1아데나가 부족합니다.
						break;
					}
				} else {
					pc.sendPackets(L1ServerMessage.sm270); // \f1 가지고 있는 것이 무거워서 거래할 수 없습니다.
					break;
				}
			}
			// 품절된 아이템을 리스트의 말미로부터 삭제
			for (int i = 7; i >= 0; i--) {
				if (isRemoveFromList[i]) {
					sellList.remove(i);
				}
			}
		}
	}
	
	/**
	 * 개인상점 판매
	 */
	private void privateShopSell(){
		L1PcInstance targetPc = null;
		if (findObject instanceof L1PcInstance) {
			targetPc = (L1PcInstance) findObject;
		}
		if (targetPc == null) {
			return;
		}
		int count, order;
		ArrayList<L1PrivateShopBuyList> buyList = targetPc.getBuyList();
		L1PrivateShopBuyList psbl;
		L1ItemInstance item = null;
		int itemObjectId, buyPrice, buyTotalCount, buyCount;
		synchronized (buyList) {
			boolean[] isRemoveFromList = new boolean[8];
			for (int i = 0; i < size; i++) {
				itemObjectId	= readD();
				count			= readCH();
				order			= readC();
				item			= inv.getItem(itemObjectId);
				if (item == null) {
					continue;
				}
				if (item.getBless() >= 128) {
					pc.sendPackets(new S_ServerMessage(210, item.getItem().getDesc()), true); // \f1%0은 버리거나 또는 타인에게 양일을 할 수 없습니다.
					return;
				}
				
				if (!item.getItem().isTradable() || item.isSlot()) {
					pc.sendPackets(L1ServerMessage.sm941); //거래 불가 아이템입니다.
					return;
				}

				psbl			= (L1PrivateShopBuyList) buyList.get(order);
				buyPrice		= psbl.getBuyPrice();
				buyTotalCount	= psbl.getBuyTotalCount(); // 살 예정의 개수
				buyCount		= psbl.getBuyCount(); // 산 누계
				
				if (buyTotalCount - buyCount == 0) {
					break;
				}
				if (count > buyTotalCount - buyCount) {
					count = buyTotalCount - buyCount;
				}
				int buyItemObjectId = psbl.getItemObjectId();
				L1ItemInstance buyItem = targetPc.getInventory().getItem(buyItemObjectId);

				if (buyItem == null) {
					return;
				}
				if (item.isEquipped()) {
					pc.sendPackets(L1ServerMessage.sm905); // 장비 하고 있는 아이템은 판매할 수 없습니다.
					continue;
				}

				if (targetPc.getInventory().checkAddItem(item, count) == L1Inventory.OK) { // 용량 중량 확인 및 메세지 송신
					for (int j = 0; j < count; j++) { // 오버플로우를 체크
						if (buyPrice * j > L1Inventory.MAX_AMOUNT || buyPrice * j < 0) {
							targetPc.sendPackets(new S_ServerMessage(904, "2000000000"), true);// 총판 매가격은%d아데나를 초과할 수 없습니다.
							return;
						}
					}	  
					/** 버그 방지 **/
					if (itemObjectId != item.getId()) {
						pc.denals_disconnect(String.format("[C_ShopAndWarehouse] PACKET_DENALS : NAME(%s)", pc.getName()));
						targetPc.denals_disconnect(String.format("[C_ShopAndWarehouse] PACKET_DENALS : NAME(%s)", pc.getName()));
						return;
					}

					if (count >= item.getCount()) {
						count = item.getCount();
					}

					if (item.getItemId() != buyItem.getItemId()) {
						return;
					}
					if (!item.isMerge() && count != 1) {
						return;					
					}
					if (item.getCount() <= 0 || count <= 0) {
						return;
					}
					if (buyPrice * count <= 0 || buyPrice * count > L1Inventory.MAX_AMOUNT) {
						return;
					}
					//** 개인상점 부분 비셔스 방어 **//	

					if (targetPc.getInventory().checkItem(L1ItemId.ADENA, count * buyPrice)) {
						L1ItemInstance adena = targetPc.getInventory().findItemId(L1ItemId.ADENA);
						if (adena == null) {
							break;
						}
						targetPc.getInventory().tradeItem(adena, count * buyPrice, inv);
						inv.tradeItem(item, count, targetPc.getInventory());
									
						targetPc.sendPackets(new S_ServerMessage(877, pc.getName(), String.format("%s (%d)", item.getItem().getDesc(), count)), true);// %1%o에 판매했습니다.
						LoggerInstance.getInstance().addPrivateShop(false, pc, targetPc, item, item.getCount());
						if (Config.ALT.SAVE_DB_LOG) {
							LogTable.logShop(pc, targetPc, item, buyPrice, count, 1);
						}
						psbl.setBuyCount(count + buyCount);
						buyList.set(order, psbl);
						if (psbl.getBuyCount() == psbl.getBuyTotalCount()) {// 살 예정의 개수를 샀다
							isRemoveFromList[order] = true;
							try {
								L1PersonalShop.delete(targetPc, item.getId(), 1);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						try {
							pc.saveInventory();
							targetPc.saveInventory();
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (Config.WEB.WEB_SERVER_ENABLE) {
							MarketLoader.getInstance().updateItem(targetPc.getName(), item.getId(), psbl.getBuyTotalCount() - psbl.getBuyCount(), 1);
						}
					} else {
						targetPc.sendPackets(L1ServerMessage.sm189); // \f1아데나가 부족합니다.
						break;
					}
				} else {
					pc.sendPackets(L1ServerMessage.sm271); // \f1상대가 물건을 너무 가지고 있어 거래할 수 없습니다.
					break;
				}
			}
			// 매점한 아이템을 리스트의 말미로부터 삭제
			for (int i = 7; i >= 0; i--) {
				if (isRemoveFromList[i]) {
					buyList.remove(i);
				}
			}
		}
	}
	
	/**
	 * 아이템 선택(창고형태)
	 */
	private void itemSelector(){
		if (size <= 0) {
			return;
		}
		if (pc.getLevel() < 80) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("80레벨 이하는 선택할 수 없습니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(115), true), true);
			return;
		}
		if (!inv.checkItem(objectId)) {
			L1Item item = ItemTable.getInstance().getTemplate(objectId);
			pc.sendPackets(new S_ServerMessage(337, item.getDesc()), true);
			return;
		}
		if (pc.getSkill().hasSkillEffect(L1SkillId.SET_BUFF)) {
			pc.sendPackets(L1SystemMessage.NOTI_BUFF_DELAY_CHECK);
			return;
		}
		if (pc.isTwoLogin()) {
			return;
		}
		if (size > 1) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("1개만 선택할 수 있습니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(116), true), true);
			return;
		}
		FastTable<SelectorWarehouseData> list = ItemSelectorTable.getSelectorWareInfo(objectId);
		if (list == null || list.isEmpty()) {
			return;
		}
		L1ItemInstance item	= null;
		int index			= readD();// 인덱스
		int count			= readD();
		for (SelectorWarehouseData data : list) {
			if (data._index == index) {
				item = data._item;
				break;
			}
		}
		if (item == null) {
			return;
		}
		if (count > item.getCount()) {
			count = item.getCount();
		}
		if (!isAvailableTrade(item, count) || !isAvailablePcWeight(item, count)) {
			return;
		}
		if (inv.checkAddItem(item, count) == L1Inventory.OK) {// 용량 중량 확인 및 메세지 송신
			L1ItemInstance create = inv.storeItem(pc, item.getItemId(), item.getCount(), item.getEnchantLevel(), item.getBless(), item.getAttrEnchantLevel());
			if (create != null) {
				pc.sendPackets(new S_ServerMessage(403, String.format("%s (%d)", create.getItem().getDesc(), item.getCount())), true);
			}
			inv.consumeItem(objectId, 1);
		} else {
			pc.sendPackets(L1ServerMessage.sm270); // \f1 가지고 있는 것이 무거워서 거래할 수 없습니다.
		}
	}
	
	/**
	 * TJ쿠폰 복구 아이템 선택
	 */
	private void tjCouponSelect(){
		if (size != 1) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("1개만 선택할 수 있습니다."), true);
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(116), true), true);
			return;
		}
		if (pc.getLevel() < Config.TJ.TJ_COUPON_USE_LEVEL) {
			//pc.sendPackets(new S_SystemMessage(String.format("%d레벨 미만은 사용할 수 없습니다.", Config.TJ.TJ_COUPON_USE_LEVEL)), true);
			pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(32), String.valueOf(Config.TJ.TJ_COUPON_USE_LEVEL)), true);
			return;
		}
		if (pc.isTwoLogin()) {
			return;
		}
		ArrayList<TJCouponBean> list = TJCouponLoader.getUser(pc.getId()).getCoupons();
		if (list == null || list.isEmpty()) {
			return;
		}
		int index			= readD();// 인덱스
		TJCouponBean bean	= list.get(index);
		if (bean == null) {
			return;
		}
		TJCoupon.useCoupon(pc, bean);
	}
	
	/**
	 * 상점 구매 제한 유효성 검증
	 * @param limit
	 * @param sbo
	 * @return boolean
	 */
	private boolean isValidationShopBuyLimit(ShopLimitInformation limit, L1ShopBuyOrder sbo) {
		if (sbo.getCount() > 1) {
			pc.sendPackets(L1SystemMessage.ONE_BUY_MSG);
			return false;
		}
		ShopLimitUser limitUser = limit.getLimitType() == ShopLimitType.ACCOUNT ? pc.getAccount().getShopLimit() : pc.getShopLimit();
		ShopLimitObject limitObj = null;
		int limitCount = 0;
		if (limitUser != null) {
			limitObj	= limitUser.getLimit(npcId, limit.getItemId());
			if (limitObj != null) {
				limitCount	= limitObj.getBuyCount();
			}
		}
		if (limitCount >= limit.getLimitCount()) {// 구매 수량 제한
			pc.sendPackets(L1SystemMessage.BUY_COUNT_MAX);
			return false;
		}
		return true;
	}
	
	/**
	 * 혈맹 상점 유효성 검증
	 * @param clan
	 * @param itemId
	 * @return boolean
	 */
	private boolean isValidationPledgeShop(L1Clan clan, int itemId, L1ShopItem shop_item){
		// 혈맹 상점은 구매 즉시 사용되므로 구매전 유효성 검사를 진행한다.
		if (clan == null) {
			pc.sendPackets(L1ServerMessage.sm1064);// 혈맹에 소속되어 있지 않습니다.(/파워북 혈맹매칭)
			return false;
		}
		if (pc.getMap().getInter() != null) {
			pc.sendPackets(L1ServerMessage.sm8322);// 인터서버에서는 이용할 수 없습니다.
			return false;
		}
		
		eBloodPledgeRankType rank = shop_item.getPledgeRank();
		// 군주 제한
		if (rank == eBloodPledgeRankType.RANK_NORMAL_KING && pc.getBloodPledgeRank() != rank) {
			pc.sendPackets(L1SystemMessage.BUY_RANKING_FAIL);
			return false;
		}
		// 부군주 제한
		if (rank == eBloodPledgeRankType.RANK_NORMAL_PRINCE && !eBloodPledgeRankType.isAuthRankAtPrince(pc.getBloodPledgeRank())) {
			pc.sendPackets(L1SystemMessage.BUY_RANKING_FAIL);
			return false;
		}
		// 수호 제한
		if (rank == eBloodPledgeRankType.RANK_NORMAL_KNIGHT && !eBloodPledgeRankType.isAuthRankAtKnight(pc.getBloodPledgeRank())) {
			pc.sendPackets(L1SystemMessage.BUY_RANKING_FAIL);
			return false;
		}
		// 정예 제한
		if (rank == eBloodPledgeRankType.RANK_NORMAL_ELITE_KNIGHT && !eBloodPledgeRankType.isAuthRankAtEliteKnight(pc.getBloodPledgeRank())) {
			pc.sendPackets(L1SystemMessage.BUY_RANKING_FAIL);
			return false;
		}
		
		switch(itemId){
		case 5994:case 5995:case 5997:// 멩세의 열쇠
			if (itemId == 5994 && !CommonUtil.isDayResetTimeCheck(clan.getClanDayDungeonTime())) {// 멩세의 열쇠
				pc.sendPackets(L1SystemMessage.BUY_COUNT_MAX);
				return false;
			}
			if ((itemId == 5995 || itemId == 5997) && !CommonUtil.isWeekResetCheck(clan.getClanWeekDungeonTime())) {// 멩세의 열쇠
				pc.sendPackets(L1SystemMessage.BUY_COUNT_MAX);
				return false;
			}
			int houseId = clan.getHouseId();
			int catleId = clan.getCastleId();
			if (houseId == 0 && catleId == 0) {
				return false;
			}
			int[] loc = null;
			if (catleId > 0) {
				loc = L1CastleLocation.getCastleLoc(clan.getCastleId());
			} else if (houseId > 0) {
				L1House house = HouseTable.getInstance().getHouseTable(houseId); 
				if (house == null || !house.isPurchaseBasement()) {
					pc.sendPackets(L1ServerMessage.sm1098);// 아지트: 지하 아지트 없음
					return false;
				}
				loc = L1HouseLocation.getBasementLoc(houseId);
			}
			if (loc == null || pc.getMapId() != loc[2]) {
				pc.sendPackets(L1SystemMessage.USE_NOT_CASTEL_FAIL);
				return false;
			}
			break;
		case 5996:// 맹세의 영약
			if (clan.getClanVowPotionCount() >= 3) {
				pc.sendPackets(L1SystemMessage.BUY_COUNT_MAX);
				return false;
			}
			long systime = System.currentTimeMillis();
			long nexttime = pc.getSurvivalTime().getTime() + (180 * 60000);
			if (pc.getLevel() >= 90) {
				nexttime -= (pc.getLevel() - 89) * (600000);
			}
			if (nexttime <= systime) {
				pc.sendPackets(L1SystemMessage.SURVIVE_CHARGE);
				return false;
			}
			break;
		}
		return true;
	}
	
	private boolean isRetriveCheck(L1ItemInstance item){
		if (!item.getItem().isRetrieve() || item.isSlot()) {
			pc.sendPackets(new S_ServerMessage(210, item.getItem().getDesc()), true);// \f1%0은 버리거나 또는 타인에게 양도 할 수 없습니다.
			return false;
		}
		
		if (item.getEndTime() != null) {
			pc.sendPackets(L1ServerMessage.sm125);
			return false;
		}
		
		if (item.getItem().getRetrieveEnchantLevel() > 0 && item.getEnchantLevel() < item.getItem().getRetrieveEnchantLevel()) {
			pc.sendPackets(new S_ServerMessage(210, item.getItem().getDesc()), true);// \f1%0은 버리거나 또는 타인에게 양도 할 수 없습니다.
			return false;
		}
		
		if (pc.getPet() != null && pc.getPet().getAmuletId() == item.getId()) {
			pc.sendPackets(new S_ServerMessage(210, item.getItem().getDesc()), true);// \f1%0은 버리거나 또는 타인에게 양일을 할 수 없습니다.
			return false;
		}

		if (pc.getDoll() != null && item.getId() == pc.getDoll().getItemObjId()) {
			pc.sendPackets(new S_ServerMessage(210, item.getItem().getDesc()), true);// \f1%0은 버리거나 또는 타인에게 양일을 할 수 없습니다.
			return false;
		}
		return true;
	}

	// 창고
	private boolean isAvailableWarehouse(int objectId, L1ItemInstance item, int count) {
		boolean result = true;
		if (item == null 
				|| objectId != item.getId()
				|| (!item.isMerge() && count != 1)
				|| item.getCount() <= 0 || item.getCount() > L1Inventory.MAX_AMOUNT
				|| count <= 0 || count > L1Inventory.MAX_AMOUNT) {
			result = false;
		}
		if (item.getItemId() == L1ItemId.INN_ROOM_KEY) {
			return false;
		}
		if (!result) {
			pc.denals_disconnect(String.format("[C_ShopAndWarehouse] WAREHOUSE_VALID_FAILURE : NAME(%s)", pc.getName()));
			return false;
		}
		return result;
	}
	
	private boolean isAvailableTrade(L1ItemInstance item, int count) {
		boolean result = true;
		if (item == null
				|| (!item.isMerge() && count != 1)
				|| item.getCount() <= 0 || item.getCount() > L1Inventory.MAX_AMOUNT
				|| count <= 0 || count > L1Inventory.MAX_AMOUNT) {
			result = false;
		}
		if (item.getItemId() == L1ItemId.INN_ROOM_KEY) {
			return false;
		}
		if (!result) {
			pc.denals_disconnect(String.format("[C_ShopAndWarehouse] TRADE_VALID_FAILURE : NAME(%s)", pc.getName()));
			return false;
		}
		return result;
	}

	private boolean isAvailablePrivateShop(L1PcInstance targetPc, int itemObjectId, L1ItemInstance item, int count) {
		boolean result = true;
		if (item == null 
				|| itemObjectId != item.getId()
				|| (!item.isMerge() && count != 1)
				|| count <= 0 || item.getCount() <= 0 || item.getCount() < count
				|| count > L1Inventory.MAX_AMOUNT || item.getCount() > L1Inventory.MAX_AMOUNT) {
			result = false;
		}
		if (!result) {
			pc.denals_disconnect(String.format("[C_ShopAndWarehouse] PERSONAL_SHOP_VALID_FAILURE : NAME(%s)", pc.getName()));
			targetPc.denals_disconnect(String.format("[C_ShopAndWarehouse] PERSONAL_SHOP_VALID_FAILURE : NAME(%s)", pc.getName()));
			return false;
		}
		return result;
	}

	private boolean isAvailablePcWeight(L1ItemInstance item, int count) {
		if (inv.checkAddItem(item, count) != L1Inventory.OK) {
			// in checkAddItem we are already sending messages to the client, not necesary to send another one
			//pc.sendPackets(L1ServerMessage.sm270);// \f1 가지고 있는 것이 무거워서 거래할 수 없습니다.
			return false;
		}
		return true;
	}
	
	private void setDeleteTime(L1ItemInstance item, int minute) {
	    item.setEndTime(new Timestamp(System.currentTimeMillis() + 60000 * minute));
	}
	
	@Override
	public String getType() {
		return C_SHOP_WAREHOUSE;
	}

}

