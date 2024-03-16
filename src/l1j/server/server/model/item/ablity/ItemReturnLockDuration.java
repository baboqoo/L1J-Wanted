package l1j.server.server.model.item.ablity;

import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemReturnLockDuration implements ItemAbility {// 귀환 불가 지속 시간 + N초
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemReturnLockDuration();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemReturnLockDuration(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc){
		int lock = 0;
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getReturnLockDuraion();
			if (value != 0) {
				lock += value;
			}
		}
		return lock;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemReturnLockDuration();
	}
	
}

