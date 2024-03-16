package l1j.server.server.model.item.ablity;

import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemAttrWind implements ItemAbility {// 바람 속성 저항
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemAttrWind();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemAttrWind(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc) {
		int attr = item.getItem().getAttrWind();
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getAttrWind();
			if (value != 0) {
				attr += value;
			}
		}
		return attr;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemAttrWind();
	}

}

