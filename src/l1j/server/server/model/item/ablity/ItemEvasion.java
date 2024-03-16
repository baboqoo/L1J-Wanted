package l1j.server.server.model.item.ablity;

import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemEvasion implements ItemAbility {// 내성 DG
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemEvasion();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemEvasion(){}
	
	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc){
		int dg				= item.getItem().getAddDg();
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getDg();
			if (value != 0) {
				dg += value;
			}
		}
		return dg;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemEvasion();
	}

}

