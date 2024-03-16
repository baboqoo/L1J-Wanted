package l1j.server.server.model.item.ablity;

import l1j.server.server.construct.item.L1ItemArmorType;
import l1j.server.server.construct.item.L1ItemType;
import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemMaxHp implements ItemAbility {// 최대 HP
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemMaxHp();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemMaxHp(){}
	
	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc){
		int hp = item.getItem().getAddHp();
		L1ItemType itemType = item.getItem().getItemType();
		int itemId = item.getItemId();
		int enchantlevel = item.getEnchantLevel();
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getMaxHp();
			if (value != 0) {
				hp += value;
			}
		}

		if (itemType == L1ItemType.ARMOR 
				&& (item.getItem().getGrade() >= 0 && item.getItem().getGrade() <= 2) 
				&& L1ItemArmorType.isAccessary(item.getItem().getType())
				&& item.getItem().getType() != L1ItemArmorType.BELT.getId() 
				&& enchantlevel > 0) {//반지 귀걸이 목걸이
			return hp += accessary_hp_without_belt(item.getItem().getType(), enchantlevel);
		}
		if (itemType == L1ItemType.ARMOR && item.getItem().getType() == L1ItemArmorType.BELT.getId() && enchantlevel > 5) {// 벨트
			return hp += belt_hp(enchantlevel);
		}
		
		if (pc != null) {
			int class_type = pc.getType();
			if (itemId == 222295 || itemId == 222296 || itemId == 222297 || itemId == 222298 || itemId == 222299) {// 55 엘릭서룬
				switch(class_type) {
				case 1:return hp += 50;
				case 7:return hp += 50;
				}
			}
			if (((itemId >= 222312 && itemId <= 222316) || (itemId >= 600051 && itemId <= 600075) || (itemId >= 600080 && itemId <= 600094) || (itemId >= 600700 && itemId <= 600720))
					&& (class_type == 1 || class_type == 6 || class_type == 7)) {// 70, 75, 80, 85, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 영광 엘릭서룬
				return hp += 50;
			}
		}
		
		return hp;
	}
	
	/**
	 * 악세사리 HP
	 * 벨트 제외
	 * @param acc_type
	 * @param enchantlevel
	 * @return HP
	 */
	int accessary_hp_without_belt(int acc_type, int enchantlevel) {
		switch (enchantlevel) {
		case 1:
			return 5;
		case 2:
			return 10;
		case 3:
			return 20;
		case 4:
			return 30;
		case 5:
		case 6:
			return 40;
		case 7:
		case 8:
			return 50;
		case 9:
			return 60;
		default:
			return acc_type == L1ItemArmorType.AMULET.getId() ? 100 : 70;
		}
	}
	
	/**
	 * 벨트 HP
	 * @param enchantlevel
	 * @return HP
	 */
	int belt_hp(int enchantlevel) {
		switch (enchantlevel) {
		case 6:
			return 20;
		case 7:
			return 30;
		case 8:
			return 40;
		case 9:
			return 50;
		default:
			return 100;
		}
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemMaxHp();
	}
	
}

