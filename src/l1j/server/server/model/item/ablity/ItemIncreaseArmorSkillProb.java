package l1j.server.server.model.item.ablity;

import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemIncreaseArmorSkillProb implements ItemAbility {// 갑옷 마법 발동 확률
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemIncreaseArmorSkillProb();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemIncreaseArmorSkillProb(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc){
		int prob			= item.getItem().getIncreaseArmorSkillProb();
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getIncreaseArmorSkillProb();
			if (value != 0) {
				prob += value;
			}
		}
		return prob;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemIncreaseArmorSkillProb();
	}
	
}

