package l1j.server.server.model.item.ablity;

import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemMoveSpeedDelayRate implements ItemAbility {// 이동 속도 증가
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemMoveSpeedDelayRate();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemMoveSpeedDelayRate(){}
	
	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc){
		int rate = item.getItem().getMoveSpeedDelayRate();
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getMoveSpeedDelayRate();
			if (value != 0) {
				rate += value;
			}
		}
		return rate;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemMoveSpeedDelayRate();
	}

}

