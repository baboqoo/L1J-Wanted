package l1j.server.server.model.item.ablity;

import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemMagicDamageReduction implements ItemAbility {// 마법 대미지 감소
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemMagicDamageReduction();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemMagicDamageReduction(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc){
		int reduc = item.getItem().getMagicDamageReduction();
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getReductionMagic();
			if (value != 0) {
				reduc += value;
			}
		}
		return reduc;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemMagicDamageReduction();
	}
	
}

