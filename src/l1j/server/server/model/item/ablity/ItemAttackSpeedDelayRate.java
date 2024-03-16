package l1j.server.server.model.item.ablity;

import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemAttackSpeedDelayRate implements ItemAbility {// 공격 속도 증가
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemAttackSpeedDelayRate();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemAttackSpeedDelayRate(){}
	
	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc){
		int rate = item.getItem().getAttackSpeedDelayRate();
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getAttackSpeedDelayRate();
			if (value != 0) {
				rate += value;
			}
		}
		return rate;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemAttackSpeedDelayRate();
	}

}

