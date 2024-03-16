package l1j.server.server.model.item.ablity;

import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemRegistSleep implements ItemAbility {// 수면 내성
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemRegistSleep();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemRegistSleep(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc) {
		int regist = item.getItem().getRegistSleep();
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getRegistSleep();
			if (value != 0) {
				regist += value;
			}
		}
		return regist;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemRegistSleep();
	}

}

