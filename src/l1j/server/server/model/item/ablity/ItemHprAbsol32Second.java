package l1j.server.server.model.item.ablity;

import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemHprAbsol32Second implements ItemAbility {// 32초 마다 HP 회복 +N
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemHprAbsol32Second();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemHprAbsol32Second(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc) {
		int regen = item.getItem().getHprAbsol32Second();
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getHprAbsol32Second();
			if (value != 0) {
				regen += value;
			}
		}
		return regen;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemHprAbsol32Second();
	}

}

