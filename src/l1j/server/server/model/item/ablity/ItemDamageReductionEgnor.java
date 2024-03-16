package l1j.server.server.model.item.ablity;

import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemDamageReductionEgnor implements ItemAbility {// 대미지 감소 무시
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemDamageReductionEgnor();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemDamageReductionEgnor(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc){
		int egnor = item.getItem().getDamageReductionEgnor();
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getReductionEgnor();
			if (value != 0) {
				egnor += value;
			}
		}
		return egnor;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemDamageReductionEgnor();
	}
	
}

