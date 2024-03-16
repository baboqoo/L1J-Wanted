package l1j.server.server.model.item.ablity;

import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemMagicEvasion implements ItemAbility {// 내성 ME
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemMagicEvasion();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemMagicEvasion(){}
	
	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc){
		int me = item.getItem().getAddMe();
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getMe();
			if (value != 0) {
				me += value;
			}
		}
		return me;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemMagicEvasion();
	}

}

