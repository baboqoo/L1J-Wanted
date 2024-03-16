package l1j.server.server.model.item.ablity;

import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemAttrAll implements ItemAbility {// 모든 속성
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemAttrAll();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemAttrAll(){}
	
	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc){
		int attr = item.getItem().getAttrAll();
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getAttrAll();
			if (value != 0) {
				attr += value;
			}
		}
		return attr;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemAttrAll();
	}

}

