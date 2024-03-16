package l1j.server.server.model.item.ablity;

import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemAbnormalStatusDamageReduction implements ItemAbility {
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemAbnormalStatusDamageReduction();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemAbnormalStatusDamageReduction(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc) {
		int reduc = item.getItem().getAbnormalStatusDamageReduction();
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getAbnormalStatusDamageReduction();
			if (value != 0) {
				reduc += value;
			}
		}
		return reduc;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemAbnormalStatusDamageReduction();
	}

}

