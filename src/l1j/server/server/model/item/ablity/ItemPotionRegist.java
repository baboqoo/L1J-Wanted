package l1j.server.server.model.item.ablity;

import l1j.server.server.construct.item.L1ItemArmorType;
import l1j.server.server.construct.item.L1ItemType;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemPotionRegist implements ItemAbility {// 회복 악화 방어
	private static class newInstance {
		public static final ItemPotionRegist INSTANCE = new ItemPotionRegist();
	}
	public static ItemPotionRegist getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemPotionRegist(){}
	
	@Override
	public Object info(L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc) {
		int regist = item.getItem().getPotionRegist();
		L1ItemType itemType = item.getItem().getItemType();
		int enchantlevel = item.getEnchantLevel();
		
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int defens = enchantAblity.getPotionPlusDefens();
			if (defens != 0) {
				regist += defens;
			}
		}

		if (itemType == L1ItemType.ARMOR && (item.getItem().getGrade() >= 0 && item.getItem().getGrade() <= 2) && enchantlevel > 4) {
			if (item.getItem().getType() == L1ItemArmorType.AMULET.getId()) {// 목걸이 5이상 물약 회복량
				return regist += amulet_regist(enchantlevel);
			}
			if (item.getItem().getType() == L1ItemArmorType.EARRING.getId()) {// 귀걸이 5이상 물약 회복량
				return regist += earring_regist(enchantlevel);
			}
		}
		return regist;
	}
	
	/**
	 * 목걸이
	 * @param enchantlevel
	 * @return regist
	 */
	int amulet_regist(int enchantlevel) {
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
	 * @return regist
	 */
	int earring_regist(int enchantlevel) {
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
		return new ItemPotionRegist();
	}

}

