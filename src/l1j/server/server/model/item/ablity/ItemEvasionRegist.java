package l1j.server.server.model.item.ablity;

import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemEvasionRegist implements ItemAbility {// 내성 ER
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemEvasionRegist();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemEvasionRegist(){}
	
	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc){
		int er	= item.getItem().getAddEr();
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getEr();
			if (value != 0) {
				er += value;
			}
		}
		return er;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemEvasionRegist();
	}

}

