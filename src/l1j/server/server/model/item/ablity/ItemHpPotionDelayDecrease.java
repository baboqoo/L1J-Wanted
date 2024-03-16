package l1j.server.server.model.item.ablity;

import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemHpPotionDelayDecrease implements ItemAbility {// HP 포션 딜레이 감소 +%d
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemHpPotionDelayDecrease();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemHpPotionDelayDecrease(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc){
		int decrease	= 0;
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getHpPotionDelayDecrease();
			if (value != 0) {
				decrease += value;
			}
		}
		return decrease;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemHpPotionDelayDecrease();
	}
	
}

