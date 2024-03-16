package l1j.server.server.model.item.ablity;

import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemEmunEgnor implements ItemAbility {// 이뮨 투함 무시 +N
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemEmunEgnor();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemEmunEgnor(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc){
		int egnor = item.getItem().getImunEgnor();
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getImunEgnor();
			if (value != 0) {
				egnor += value;
			}
		}
		return egnor;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemEmunEgnor();
	}
	
}

