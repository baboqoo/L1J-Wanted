package l1j.server.server.model.item.ablity;

import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemBaseHpRate implements ItemAbility {// 최대 HP 퍼센트
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemBaseHpRate();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemBaseHpRate(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc){
		int rate = 0;
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getBaseHpRate();
			if (value != 0) {
				rate += value;
			}
		}
		return rate;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemBaseHpRate();
	}
	
}

