package l1j.server.GameSystem.tjcoupon;

import java.sql.Timestamp;

import l1j.server.Config;
import l1j.server.GameSystem.tjcoupon.bean.TJCouponBean;
import l1j.server.server.construct.item.L1ItemArmorType;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.monitor.LoggerInstance;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.templates.L1Item;

/**
 * TJ 쿠폰 담당 핸들러
 * @author LinOffice
 */
public class TJCoupon {
	private static L1Item COUPON;
	
	/**
	 * 유효성 체크
	 * @param item
	 * @return boolean
	 */
	static boolean validation(L1ItemInstance item){
		switch (item.getItem().getItemType()) {
		case WEAPON:
			switch (item.getItem().getItemGrade()) {
			case LEGEND:case MYTH:
				return true;
			default:
				return item.getEnchantLevel() >= Config.TJ.TJ_COUPON_WEAPON_ENCHANT;
			}
		case ARMOR:
			if (L1ItemArmorType.isAccessary(item.getItem().getType())) {
				return item.getEnchantLevel() >= Config.TJ.TJ_COUPON_ACCESSARY_ENCHANT;
			}
			switch (item.getItem().getItemGrade()) {
			case LEGEND:case MYTH:
				return true;
			default:
				return item.getEnchantLevel() >= Config.TJ.TJ_COUPON_ARMOR_ENCHANT;
			}
		default:
			return false;
		}
	}
	
	/**
	 * 복구 로그를 생성한다.
	 * 로그 인스턴스에서 주기적 flush시 호출된다.
	 * @param charId
	 * @param item
	 * @param time
	 */
	public static void createCoupon(int charId, L1ItemInstance item, String time){
		if (!validation(item)) {
			return;
		}
		TJCouponLoader.getUser(charId).put(new TJCouponBean(item.getId(), charId, item.getItemId(), item.getCount(), item.getEnchantLevel(), item.getAttrEnchantLevel(), item.getBless(), Timestamp.valueOf(time)));
	}
	
	/**
	 * 쿠폰을 사용한다.
	 * C_ShopAndWarehouse 창고패킷 사용
	 * @param L1PcInstance
	 * @param TJCouponBean
	 */
	public static void useCoupon(L1PcInstance pc, TJCouponBean bean){
		if (COUPON == null) {
			COUPON = ItemTable.getInstance().getTemplate(Config.TJ.TJ_COUPON_ITEMID);
		}
		if (COUPON == null) {
			System.out.println(String.format("[TJCoupon] COUPON EMPTY TEMPLATE ITEMID(%d)", Config.TJ.TJ_COUPON_ITEMID));
			return;
		}
		if (!pc.getInventory().checkItem(COUPON.getItemId(), 1)) {
			pc.sendPackets(new S_ServerMessage(337, COUPON.getDesc()), true);
			return;
		}
		
		L1ItemInstance create = bean.getItem();
		if (pc.getInventory().checkAddItem(create, create.getCount()) == L1Inventory.OK) {// 용량 중량 확인 및 메세지 송신
			pc.getInventory().consumeItem(COUPON.getItemId(), 1);
			pc.getInventory().storeItem(create);
			pc.sendPackets(new S_ServerMessage(403, create.getLogNameRef()), true);
			LoggerInstance.getInstance().addTjCoupon(pc, create);
			TJCouponLoader.getUser(bean.getCharId()).remove(bean);
		} else {
			pc.sendPackets(L1ServerMessage.sm270); // \f1 가지고 있는 것이 무거워서 거래할 수 없습니다.
		}
	}
}

