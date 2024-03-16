package l1j.server.server.model.item.ablity;

import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemRegistBlind implements ItemAbility {// 암흑 내성
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemRegistBlind();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemRegistBlind(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc) {
		int regist = item.getItem().getRegistBlind();
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getRegistBlind();
			if (value != 0) {
				regist += value;
			}
		}
		return regist;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemRegistBlind();
	}

}

