package l1j.server.server.model.item.ablity;

import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemMagicDamage implements ItemAbility {// 마법 대미지
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemMagicDamage();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemMagicDamage(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc){
		int dmg = 0;
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getMagicDamage();
			if (value != 0) {
				dmg += value;
			}
		}
		return dmg;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemMagicDamage();
	}
	
}

