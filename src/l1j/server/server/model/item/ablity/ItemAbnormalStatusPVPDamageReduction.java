package l1j.server.server.model.item.ablity;

import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemAbnormalStatusPVPDamageReduction implements ItemAbility {
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemAbnormalStatusPVPDamageReduction();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemAbnormalStatusPVPDamageReduction(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc) {
		int reduc = item.getItem().getAbnormalStatusPVPDamageReduction();
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getAbnormalStatusPVPDamageReduction();
			if (value != 0) {
				reduc += value;
			}
		}
		return reduc;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemAbnormalStatusPVPDamageReduction();
	}

}

