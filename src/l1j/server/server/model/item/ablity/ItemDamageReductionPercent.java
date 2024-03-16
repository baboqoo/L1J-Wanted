package l1j.server.server.model.item.ablity;

import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemDamageReductionPercent implements ItemAbility {// 대미지 감소 + 퍼센트
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemDamageReductionPercent();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemDamageReductionPercent(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc){
		int reduc = item.getItem().getDamageReductionPercent();
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getReductionPercent();
			if (value != 0) {
				reduc += value;
			}
		}
		return reduc;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemDamageReductionPercent();
	}
	
}

