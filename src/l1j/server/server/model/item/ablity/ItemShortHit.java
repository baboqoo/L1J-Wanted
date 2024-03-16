package l1j.server.server.model.item.ablity;

import l1j.server.server.construct.item.L1ItemWeaponType;
import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemShortHit implements ItemAbility {// 근거리 명중
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemShortHit();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemShortHit(){}
	
	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc){
		int hit = item.getItem().getHitRate();
		int itemId = item.getItemId();
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getShortHit();
			if (value != 0) {
				hit += value;
			}
		}

		if (pc != null) {
			int class_type = pc.getType();
			if ((itemId >= 222295 && itemId <= 222299) && class_type == 5) {// 55엘릭서 룬
				return hit += 3;
			}
			if ((itemId >= 222312 && itemId <= 222316) || (itemId >= 600051 && itemId <= 600075) || (itemId >= 600080 && itemId <= 600094) || (itemId >= 600700 && itemId <= 600720)) {// 70, 75, 80, 85, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 영광의 엘릭서룬
				switch(class_type) {
				case 0:
					return hit += 2;
				case 5:case 8:case 9:
					return hit += 3;
				}
			}
			if ((itemId == 22400 || itemId == 200505) && !(class_type == 2 && pc.getWeapon() != null && L1ItemWeaponType.isBowWeapon(pc.getWeapon().getItem().getWeaponType())) && class_type != 3 && class_type != 6) {
				return hit += 2;
			}
		}
		return hit;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemShortHit();
	}
	
}

