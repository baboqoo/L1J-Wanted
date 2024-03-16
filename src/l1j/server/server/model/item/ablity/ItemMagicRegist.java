package l1j.server.server.model.item.ablity;

import l1j.server.server.construct.item.L1ItemArmorType;
import l1j.server.server.construct.item.L1ItemType;
import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemMagicRegist implements ItemAbility {// 마방
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemMagicRegist();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemMagicRegist(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc){
		int mr = item.getItem().getMr();
		L1ItemType itemType = item.getItem().getItemType();
		int itemId = item.getItemId();
		int enchantlevel = item.getEnchantLevel();
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getMr();
			if (value != 0) {
				mr += value;
			}
		}
		if (itemType == L1ItemType.ARMOR && (item.getItem().getGrade() >= 0 && item.getItem().getGrade() <= 2) && L1ItemArmorType.isRing(item.getItem().getType()) && enchantlevel > 5) {// 반지
			return mr += ring_mr(enchantlevel);
		}
		if (itemType == L1ItemType.ARMOR && (item.getItem().getGrade() >= 0 && item.getItem().getGrade() <= 2) && (item.getItem().getType() == L1ItemArmorType.AMULET.getId()) && enchantlevel > 4) {// 목걸이
			return mr += amulet_mr(enchantlevel);
		}
		if (((itemId >= 222312 && itemId <= 222316) || (itemId >= 600051 && itemId <= 600075) || (itemId >= 600080 && itemId <= 600094) || (itemId >= 600700 && itemId <= 600720)) && pc != null && pc.getType() == 7) {//70, 75, 80, 85, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 영광 엘릭서룬
			return mr += 5;
		}

		return mr;
	}
	
	/**
	 * 반지 MR
	 * @param enchantlevel
	 * @return MR
	 */
	int ring_mr(int enchantlevel) {
		if (enchantlevel == 6) {
			return 1;
		}
		if (enchantlevel == 7) {
			return 3;
		}
		if (enchantlevel == 8) {
			return 5;
		}
		if (enchantlevel == 9) {
			return 7;
		}
		return 9;
	}
	
	/**
	 * 목걸이 MR
	 * @param enchantlevel
	 * @return MR
	 */
	int amulet_mr(int enchantlevel) {
		if (enchantlevel == 5) {
			return 1;
		}
		if (enchantlevel == 6) {
			return 3;
		}
		if (enchantlevel == 7) {
			return 5;
		}
		if (enchantlevel == 8) {
			return 7;
		}
		if (enchantlevel == 9) {
			return 10;
		}
		return 12;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemMagicRegist();
	}
	
}

