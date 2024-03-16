package l1j.server.server.model.item.ablity;

import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemStrangeTimeDecrease implements ItemAbility {// 상태 이상 시간 감소
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemStrangeTimeDecrease();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemStrangeTimeDecrease(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc){
		int time			= item.getItem().getStrangeTimeDecrease();
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getStrangeTimeDecrease();
			if (value != 0) {
				time += value;
			}
		}
		return time;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemStrangeTimeDecrease();
	}
	
}

