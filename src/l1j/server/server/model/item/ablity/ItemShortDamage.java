package l1j.server.server.model.item.ablity;

import l1j.server.server.construct.item.L1ItemArmorType;
import l1j.server.server.construct.item.L1ItemType;
import l1j.server.server.construct.item.L1ItemWeaponType;
import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemShortDamage implements ItemAbility {// 근거리 대미지
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemShortDamage();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemShortDamage(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc){
		int dmg = item.getItem().getDmgRate();
		L1ItemType itemType = item.getItem().getItemType();
		int itemId = item.getItemId();
		int enchantlevel = item.getEnchantLevel();
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getShortDamage();
			if (value != 0) {
				dmg += value;
			}
		}

		if (itemType == L1ItemType.ARMOR && (item.getItem().getGrade() >= 0 && item.getItem().getGrade() <= 2) && L1ItemArmorType.isRing(item.getItem().getType()) && enchantlevel > 4) {// 반지 5이상
			return dmg += enchantlevel - 4;
		}
		
		if (pc != null) {
			int class_type = pc.getType();
			if (((itemId >= 222312 && itemId <= 222316) || (itemId >= 600051 && itemId <= 600075) || (itemId >= 600080 && itemId <= 600094) || (itemId >= 600700 && itemId <= 600720)) && (class_type == 1 || class_type == 2)) {// 70, 75, 80, 85, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 영광의 엘릭서룬
				return dmg += 1;
			}
			if (itemId == 22400 && !(class_type == 2 && pc.getWeapon() != null && L1ItemWeaponType.isBowWeapon(pc.getWeapon().getItem().getWeaponType())) && class_type != 3 && class_type != 6) {
				return dmg += 2;
			}
		}
		return dmg;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemShortDamage();
	}
	
}

