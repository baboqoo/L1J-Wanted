package l1j.server.server.model.item.ablity;

import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemExpBonus implements ItemAbility {// EXP +n
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemExpBonus();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemExpBonus(){}
	
	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc){
		int exp	= item.getItem().getExpBonus();
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getExpBonus();
			if (value != 0) {
				exp += value;
			}
		}
		return exp;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemExpBonus();
	}

}

