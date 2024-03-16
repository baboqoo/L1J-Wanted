package l1j.server.server.model.item.ablity;

import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemAcBonus implements ItemAbility {// 추가 방어력
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemAcBonus();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemAcBonus(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc) {
		int ac = item.getItem().getAcBonus();
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getAcBonus();
			if (value != 0) {
				ac += value;
			}
		}
		return ac;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemAcBonus();
	}

}

