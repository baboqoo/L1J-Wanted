package l1j.server.server.model.item.ablity;

import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemHpRegen implements ItemAbility {// HP 회복
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemHpRegen();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemHpRegen(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc){
		int regen = item.getItem().getAddHpr();
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getHpRegen();
			if (value != 0) {
				regen += value;
			}
		}
		return regen;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemHpRegen();
	}
	
}

