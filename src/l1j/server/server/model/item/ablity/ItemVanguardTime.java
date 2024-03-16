package l1j.server.server.model.item.ablity;

import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemVanguardTime implements ItemAbility {// 뱅가드 지속 시간 감소
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemVanguardTime();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemVanguardTime(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc){
		int time = 0;
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getVanguardTime();
			if (value != 0) {
				time += value;
			}
		}
		return time;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemVanguardTime();
	}
	
}

