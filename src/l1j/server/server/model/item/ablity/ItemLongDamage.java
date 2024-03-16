package l1j.server.server.model.item.ablity;

import l1j.server.server.construct.item.L1ItemArmorType;
import l1j.server.server.construct.item.L1ItemType;
import l1j.server.server.construct.item.L1ItemWeaponType;
import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemLongDamage implements ItemAbility {// 원거리 대미지
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemLongDamage();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemLongDamage(){}
	
	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc){
		int dmg = item.getItem().getBowDmgRate();
		L1ItemType itemType = item.getItem().getItemType();
		int itemId = item.getItemId();
		int enchantlevel = item.getEnchantLevel();
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getLongDamage();
			if (value != 0) {
				dmg += value;
			}
		}

		if (itemType == L1ItemType.ARMOR && (item.getItem().getGrade() >= 0 && item.getItem().getGrade() <= 2) && L1ItemArmorType.isRing(item.getItem().getType()) && enchantlevel > 4) {//반지 5이상
			return dmg += (enchantlevel - 4);
		}
		if (pc != null && pc.getType() == 2) {
			if ((itemId >= 222312 && itemId <= 222316) || (itemId >= 600051 && itemId <= 600075) || (itemId >= 600080 && itemId <= 600094) || (itemId >= 600700 && itemId <= 600720)) {//70, 75, 80, 85, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 영광 엘릭서룬
				return dmg += 1;
			}
			if (itemId == 22400 && pc.getWeapon() != null && L1ItemWeaponType.isBowWeapon(pc.getWeapon().getItem().getWeaponType())) {
				return dmg += 2;
			}
		}

		return dmg;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemLongDamage();
	}
	
}

