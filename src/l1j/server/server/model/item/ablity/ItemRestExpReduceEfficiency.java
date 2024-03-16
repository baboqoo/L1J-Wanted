package l1j.server.server.model.item.ablity;

import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemRestExpReduceEfficiency implements ItemAbility {// 축복 소모 효율
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemRestExpReduceEfficiency();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemRestExpReduceEfficiency(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc){
		int val = item.getItem().getRestExpReduceEfficiency();
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getRestExpReduceEfficiency();
			if (value != 0) {
				val += value;
			}
		}
		return val;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemRestExpReduceEfficiency();
	}
}

