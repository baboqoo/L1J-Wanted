package l1j.server.server.model.item.ablity;

import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemToleranceDragon implements ItemAbility {// 용언 내성
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemToleranceDragon();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemToleranceDragon(){}
	
	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc){
		int regist = item.getItem().getToleranceDragon();
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getToleranceDragon();
			if (value != 0) {
				regist += value;
			}
		}
		return regist;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemToleranceDragon();
	}
	
}

