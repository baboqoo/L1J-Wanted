package l1j.server.server.model.item.ablity;

import l1j.server.server.construct.item.L1ItemArmorType;
import l1j.server.server.construct.item.L1ItemType;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemPotionPercent implements ItemAbility {// 물약 회복량
	private static class newInstance {
		public static final ItemPotionPercent INSTANCE = new ItemPotionPercent();
	}
	public static ItemPotionPercent getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemPotionPercent(){}
	
	@Override
	public Object info(L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc) {
		int percent = item.getItem().getPotionPercent();
		L1ItemType itemType = item.getItem().getItemType();
		int enchantlevel = item.getEnchantLevel();
		
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getPotionPlusPercent();
			if (value != 0) {
				percent += value;
			}
		}

		if (itemType == L1ItemType.ARMOR && (item.getItem().getGrade() >= 0 && item.getItem().getGrade() <= 2) && enchantlevel > 4) {
			if (item.getItem().getType() == L1ItemArmorType.AMULET.getId()) {// 목걸이 5이상 물약 회복량
				return percent += amulet_percent(enchantlevel);
			}
			if (item.getItem().getType() == L1ItemArmorType.EARRING.getId()) {// 귀걸이 5이상 물약 회복량
				return percent += earring_percent(enchantlevel);
			}
		}
		return percent;
	}
	
	/**
	 * 목걸이
	 * @param enchantlevel
	 * @return percent
	 */
	int amulet_percent(int enchantlevel) {
		switch (enchantlevel) {
		case 5:
			return 3;
		case 6:
			return 5;
		case 7:
			return 7;
		case 8:
			return 9;
		case 9:
			return 10;
		default:
			return 11;
		}
	}
	
	/**
	 * 귀걸이
	 * @param enchantlevel
	 * @return percent
	 */
	int earring_percent(int enchantlevel) {
		switch (enchantlevel) {
		case 5:
			return 2;
		case 6:
			return 4;
		case 7:
			return 6;
		case 8:
			return 8;
		case 9:
			return 9;
		default:
			return 10;
		}
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemPotionPercent();
	}

}

