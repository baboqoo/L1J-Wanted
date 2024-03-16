package l1j.server.server.model.item.ablity;

import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemTitanUp implements ItemAbility {// 타이탄 계열 기술 발동 HP 구간 % 증가
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemTitanUp();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemTitanUp(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc) {
		int titan = 0;
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getTitanUp();
			if (value != 0) {
				titan += value;
			}
		}
		return titan;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemTitanUp();
	}

}

