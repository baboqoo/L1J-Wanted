package l1j.server.server.model.item.ablity;

import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemAttrWater implements ItemAbility {// 물 속성 저항
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemAttrWater();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemAttrWater(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc) {
		int attr = item.getItem().getAttrWater();
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getAttrWater();
			if (value != 0) {
				attr += value;
			}
		}
		return attr;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemAttrWater();
	}

}

