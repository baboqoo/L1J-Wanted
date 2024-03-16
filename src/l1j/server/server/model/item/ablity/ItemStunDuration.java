package l1j.server.server.model.item.ablity;

import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemStunDuration implements ItemAbility {// 스턴 지속 시간 + N초
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemStunDuration();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemStunDuration(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc){
		int stun = item.getItem().getStunDuration();
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getStunDuration();
			if (value != 0) {
				stun += value;
			}
		}
		return stun;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemStunDuration();
	}
	
}

