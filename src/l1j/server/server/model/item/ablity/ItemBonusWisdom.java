package l1j.server.server.model.item.ablity;

import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemBonusWisdom implements ItemAbility {// wis
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemBonusWisdom();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemBonusWisdom(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc){
		byte wis = item.getItem().getAddWis();
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			byte value = enchantAblity.getWis();
			if (value != 0) {
				wis += value;
			}
		}
		return wis;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemBonusWisdom();
	}
	
}

