package l1j.server.server.model.item.ablity;

import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemTripleArrowStun implements ItemAbility {// 트리플 애로우 스턴 발동
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemTripleArrowStun();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemTripleArrowStun(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc){
		int tri = item.getItem().getTripleArrowStun();
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getTripleArrowStun();
			if (value != 0) {
				tri += value;
			}
		}
		return tri;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemTripleArrowStun();
	}
	
}

