package l1j.server.server.model.item.ablity;

import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemHitupAll implements ItemAbility {// 전체 적중
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemHitupAll();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemHitupAll(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc){
		int hit = item.getItem().getHitupAll();
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getHitupAll();
			if (value != 0) {
				hit += value;
			}
		}
		return hit;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemHitupAll();
	}
}

