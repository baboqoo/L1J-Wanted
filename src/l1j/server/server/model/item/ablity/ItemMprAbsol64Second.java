package l1j.server.server.model.item.ablity;

import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemMprAbsol64Second implements ItemAbility {// 64초 마다 MP 회복 +N
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemMprAbsol64Second();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemMprAbsol64Second(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc) {
		int regen = item.getItem().getMprAbsol64Second();
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getMprAbsol64Second();
			if (value != 0) {
				regen += value;
			}
		}
		return regen;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemMprAbsol64Second();
	}

}

