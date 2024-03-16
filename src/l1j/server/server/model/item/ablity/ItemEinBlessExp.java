package l1j.server.server.model.item.ablity;

import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemEinBlessExp implements ItemAbility {// 축복 경험치 + N
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemEinBlessExp();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemEinBlessExp(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc) {
		int exp = 0;
		int itemId = item.getItemId();
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getEinBlessExp();
			if (value != 0) {
				exp += value;
			}
		}
		if (itemId == 30208) {
			return exp += 20;
		}
		if (itemId == 43051) {
			return exp += 30;
		}
		return exp;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemEinBlessExp();
	}

}

