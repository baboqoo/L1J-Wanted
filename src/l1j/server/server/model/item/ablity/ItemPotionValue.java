package l1j.server.server.model.item.ablity;

import l1j.server.server.construct.item.L1ItemArmorType;
import l1j.server.server.construct.item.L1ItemType;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemPotionValue implements ItemAbility {
	private static class newInstance {
		public static final ItemPotionValue INSTANCE = new ItemPotionValue();
	}
	public static ItemPotionValue getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemPotionValue(){}
	
	@Override
	public Object info(L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc) {
		int val = item.getItem().getPotionValue();
		L1ItemType itemType = item.getItem().getItemType();
		int enchantlevel = item.getEnchantLevel();
		
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getPotionPlusValue();
			if (value != 0) {
				val += value;
			}
		}
		
		if (itemType == L1ItemType.ARMOR && (item.getItem().getGrade() >= 0 && item.getItem().getGrade() <= 2) && enchantlevel > 5) {
			if (item.getItem().getType() == L1ItemArmorType.AMULET.getId()) {// 목걸이 5이상 물약 회복량
				return val += amulet_value(enchantlevel);
			}
			if (item.getItem().getType() == L1ItemArmorType.EARRING.getId()) {// 귀걸이 5이상 물약 회복량
				return val += earring_value(enchantlevel);
			}
		}
		return val;
	}
	
	/**
	 * 목걸이
	 * @param enchantlevel
	 * @return value
	 */
	int amulet_value(int enchantlevel) {
		switch (enchantlevel) {
		case 6:
			return 3;
		case 7:
			return 5;
		case 8:
			return 7;
		case 9:
			return 8;
		default:
			return 9;
		}
	}
	
	/**
	 * 귀걸이
	 * @param enchantlevel
	 * @return value
	 */
	int earring_value(int enchantlevel) {
		switch (enchantlevel) {
		case 6:
			return 2;
		case 7:
			return 4;
		case 8:
			return 6;
		case 9:
			return 7;
		default:
			return 8;
		}
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemPotionValue();
	}

}

