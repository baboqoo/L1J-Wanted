package l1j.server.server.model.item.ablity;

import l1j.server.server.construct.item.L1ItemWeaponType;
import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemLongHit implements ItemAbility {// 원거리 명중
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemLongHit();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemLongHit(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc){
		int hit = item.getItem().getBowHitRate();
		int itemId = item.getItemId();
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getLongHit();
			if (value != 0) {
				hit += value;
			}
		}
		if (pc != null && pc.getType() == 2) {
			if (itemId == 222295 || itemId == 222296 || itemId == 222297 || itemId == 222298 || itemId == 222299) {//55 엘릭서룬
				return hit += 3;
			}
			if ((itemId == 22400 || itemId == 200505) && pc.getWeapon() != null && L1ItemWeaponType.isBowWeapon(pc.getWeapon().getItem().getWeaponType())) {
				return hit += 2;
			}
		}
		return hit;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemLongHit();
	}
	
}

