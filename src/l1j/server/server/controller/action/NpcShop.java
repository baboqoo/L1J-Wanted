package l1j.server.server.controller.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import l1j.server.Config;
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.IdFactory;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.NpcShopSpawnTable;
import l1j.server.server.datatables.NpcShopTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1NpcShopInstance;
import l1j.server.server.model.shop.L1Shop;
import l1j.server.server.serverpackets.action.S_DoActionShop;
import l1j.server.server.templates.L1NpcShop;
import l1j.server.server.templates.L1PrivateShopBuyList;
import l1j.server.server.templates.L1PrivateShopSellList;
import l1j.server.server.templates.L1ShopItem;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.response.market.MarketLoader;

/**
 * 영자 상점 시장 스폰 컨트롤러
 * @author LinOffice
 */
public class NpcShop implements ControllerInterface {
	
	private static final String RELOAD_HOUR_REGEX = "0000|0200|0400|0600|0800|1000|1200|1400|1600|1800|2000|2200";// 2시간 주기
	
	private static class newInstance {
		public static final NpcShop INSTANCE = new NpcShop();
	}
	public static NpcShop getInstance() {
		return newInstance.INSTANCE;
	}
	private NpcShop(){}
	
	private boolean _power = false;
	private boolean isReload = false;
	SimpleDateFormat sdf = new SimpleDateFormat("HHmm", Locale.KOREA);

	@Override
	public void execute() {
		if(!_power)return;
		try {
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(System.currentTimeMillis());
			String currentTime = sdf.format(c.getTime());
			if (!isReload) {
				if (isReloadHour(currentTime)) {
					// 리로드
					isReload = true;
					deletePrivateShop();
					shopItemSetting();
				} else {
					isReload = false;
				}
			} else {
				isReload = false;
			}
		} catch (Exception e) {
		}
	}
	
	@Override
	public void execute(l1j.server.server.model.Instance.L1PcInstance pc) {
	}
	
	/**
	 * 리로드가 되는 시간을 조사한다.
	 * @param time
	 * @return boolean
	 */
	private boolean isReloadHour(String time) {
		if (StringUtil.isNullOrEmpty(time)){
			return false;
		}
		return time.matches(RELOAD_HOUR_REGEX);
	}
	
	static class NpcShopSpwan implements Runnable {

		public NpcShopSpwan() {
		}

		public void run() {
			try {
				ArrayList<L1NpcShop> list = NpcShopSpawnTable.getInstance().getList();
				for (int i = 0; i < list.size(); i++) {
					L1NpcShop shop = list.get(i);
					L1NpcInstance npc = NpcTable.getInstance().newNpcInstance(shop.getNpcId());
					npc.setId(IdFactory.getInstance().nextId());
					npc.setMap(shop.getMapId());
					npc.getLocation().set(shop.getX(), shop.getY(), shop.getMapId());
					npc.getLocation().forward(5);
					npc.setHomeX(npc.getX());
					npc.setHomeY(npc.getY());
					npc.getMoveState().setHeading(shop.getHeading());
					npc.setName(shop.getName());
					npc.setTitle(shop.getTitle());
					L1NpcShopInstance obj = (L1NpcShopInstance) npc;
					obj.setShopChat(shop.getShopChat());
					L1World.getInstance().storeObject(npc);
					L1World.getInstance().addVisibleObject(npc);
					npc.getLight().turnOnOffLight();
					Thread.sleep(30L);
					obj.setState(1);
					npc.broadcastPacket(new S_DoActionShop(npc.getId(), ActionCodes.ACTION_Shop, shop.getShopChat()), true);
					Thread.sleep(10L);
				}
				deletePrivateShop();
				shopItemSetting();
			} catch (Exception exception) {
				return;
			}
		}
	}

	public void npcShopStart() {
		NpcShopSpwan ns = new NpcShopSpwan();
		GeneralThreadPool.getInstance().execute(ns);
		_power = true;
	}

	public void npcShopStop() {
		_power = false;
		ArrayList<L1NpcShop> list = NpcShopSpawnTable.getInstance().getList();
		if (list != null && !list.isEmpty()) {
			deletePrivateShop();
			L1NpcInstance npc = null;
			for (L1NpcShop shop : list) {
				if (shop == null) {
					continue;
				}
				npc = L1World.getInstance().findNpc(shop.getNpcId());
				if (npc == null) {
					continue;
				}
				npc.deleteMe();
			}
		}
	}

	public boolean isPower() {
		return _power;
	}
	
	/**
	 * 영자 상점의 아이템을  등록한다.
	 */
	private static void shopItemSetting(){
		MarketLoader market = MarketLoader.getInstance();
		L1PrivateShopSellList pss	= null;
		L1PrivateShopBuyList psb	= null;
		L1Shop shop					= null;
		ArrayList<L1PrivateShopSellList> sellList	= null;
		ArrayList<L1PrivateShopBuyList> buyList		= null;
		L1ItemInstance item = null;
		for (L1NpcShopInstance npcShop : L1World.getInstance().getAllNpcShop()) {
			shop = NpcShopTable.getInstance().get(npcShop.getNpcId());
			
			sellList	= npcShop.get_sellList();
			buyList		= npcShop.get_buyList();
			
			// 판매 리스트 세팅
			for (L1ShopItem shopItem : shop.getSellingItems()) {
				pss = new L1PrivateShopSellList();
				item = ItemTable.getInstance().createItem(shopItem.getItemId());
				item.setEnchantLevel(shopItem.getEnchant());
				pss.setSellItem(item);
				pss.setItemObjectId(item.getId());
				pss.setSellPrice(shopItem.getPrice());
				pss.setSellTotalCount(shopItem.getCount());
				sellList.add(pss);
			}
			
			// 구매 리스트 세팅
			for (L1ShopItem shopItem : shop.getBuyingItems()) {
				psb = new L1PrivateShopBuyList();
				item = ItemTable.getInstance().createItem(shopItem.getItemId());
				item.setEnchantLevel(shopItem.getEnchant());
				psb.setBuyItem(item);
				psb.setItemObjectId(item.getId());
				psb.setBuyPrice(shopItem.getPrice());
				psb.setBuyTotalCount(shopItem.getCount());
				buyList.add(psb);
			}
			
			if (Config.WEB.WEB_SERVER_ENABLE) {
				market.addItem(0, sellList, npcShop);
				market.addItem(1, buyList, npcShop);
			}
		}
	}
	
	/**
	 * 영자 상점의 아이템을 제거한다.
	 */
	private static void deletePrivateShop(){
		MarketLoader market = MarketLoader.getInstance();
		for (L1NpcShopInstance npcShop : L1World.getInstance().getAllNpcShop()) {
			if (npcShop.get_sellList() != null) {
				npcShop.get_sellList().clear();
			}
			if (npcShop.get_buyList() != null) {
				npcShop.get_buyList().clear();
			}
			if (Config.WEB.WEB_SERVER_ENABLE) {
				market.deleteShop(npcShop);
			}
		}
	}

}

