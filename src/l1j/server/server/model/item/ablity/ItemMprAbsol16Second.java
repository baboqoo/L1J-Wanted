package l1j.server.server.model.item.ablity;

import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemMprAbsol16Second implements ItemAbility {// MP절대 회복 +%d(16초)
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemMprAbsol16Second();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemMprAbsol16Second(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc) {
		int regen = item.getItem().getMprAbsol16Second();;
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getMprAbsol16Second();
			if (value != 0) {
				regen += value;
			}
		}
		return regen;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemMprAbsol16Second();
	}

}

