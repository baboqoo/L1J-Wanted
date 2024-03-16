package l1j.server.server.model.item.ablity;

import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemFowSlayerDamage implements ItemAbility {// 포우 슬레이어 대미지
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemFowSlayerDamage();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemFowSlayerDamage(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc) {
		int dmg = 0;
		int itemId = item.getItemId();
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getFowSlayerDamage();
			if (value != 0) {
				dmg += value;
			}
		}
		if (itemId == 3000086 || itemId == 31128 || itemId == 2510 || itemId == 2519) {
			return dmg += 10;
		}
		return dmg;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemFowSlayerDamage();
	}

}

