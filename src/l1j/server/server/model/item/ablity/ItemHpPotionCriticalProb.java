package l1j.server.server.model.item.ablity;

import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemHpPotionCriticalProb implements ItemAbility {// HP 포션 크리티컬 확률 +%d
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemHpPotionCriticalProb();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemHpPotionCriticalProb(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc){
		int prob			= 0;
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getHpPotionCriticalProb();
			if (value != 0) {
				prob += value;
			}
		}
		return prob;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemHpPotionCriticalProb();
	}
	
}

