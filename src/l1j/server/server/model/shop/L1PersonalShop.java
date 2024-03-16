package l1j.server.server.model.shop;

import java.util.ArrayList;

import l1j.server.Config;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1PrivateShopBuyList;
import l1j.server.web.dispatcher.response.market.MarketLoader;

public class L1PersonalShop {
	
	/**
	 * 검색으로 등록된 아이템 제거
	 * @param buyList
	 */
	private static void searchDummyRemove(ArrayList<L1PrivateShopBuyList> buyList){
		if (buyList != null && !buyList.isEmpty()) {
			for (L1PrivateShopBuyList psb : buyList) {
				if (psb.isBySearching() && psb.getBuyItem() != null) {
					L1World.getInstance().removeObject(psb.getBuyItem());// 검색으로 등록된 아이템 제거
				}
			}
		}
	}
	
	/**
	 * 검색으로 등록된 아이템 제거
	 * @param buyList
	 * @param itemObjId
	 */
	private static void searchDummyItemRemove(ArrayList<L1PrivateShopBuyList> buyList, int itemObjId){
		if (buyList != null && !buyList.isEmpty()) {
			for (L1PrivateShopBuyList psb : buyList) {
				if (psb.isBySearching() && psb.getBuyItem() != null && psb.getBuyItem().getId() == itemObjId) {
					L1World.getInstance().removeObject(psb.getBuyItem());// 검색으로 등록된 아이템 제거
				}
			}
		}
	}
	
	/**
	 * 상점 종료
	 * @param pc
	 */
	public static void delete(L1PcInstance pc) {
		searchDummyRemove(pc.getBuyList());
		if (Config.WEB.WEB_SERVER_ENABLE) {
			MarketLoader.getInstance().deleteShop(pc);
		}
	}
	
	/**
	 * 상점 목록 제거
	 * @param pc
	 * @param itemObjid
	 * @param type
	 */
	public static void delete(L1PcInstance pc, int itemObjid, int type) {
		if (type == 1) {
			searchDummyItemRemove(pc.getBuyList(), itemObjid);
		}
    }
	
	/**
	 * 상점 목록 추가
	 * @param cha
	 * @param list
	 * @param type
	 */
	public static void regist(L1Character cha, ArrayList<?> list, int type){
		if (list == null || list.isEmpty()) {
			return;
		}
		if (Config.WEB.WEB_SERVER_ENABLE) {
			MarketLoader.getInstance().addItem(type, list, cha);
		}
	}
}

