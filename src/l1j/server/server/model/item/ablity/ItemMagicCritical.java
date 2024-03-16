package l1j.server.server.model.item.ablity;

import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemMagicCritical implements ItemAbility {// 마법 치명타
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemMagicCritical();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemMagicCritical(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc){
		int cri = item.getItem().getMagicCritical();
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getMagicCritical();
			if (value != 0) {
				cri += value;
			}
		}
		return cri;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemMagicCritical();
	}
	
}

