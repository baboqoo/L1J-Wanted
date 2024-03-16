package l1j.server.server.model.item.ablity;

import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemPVPDamagePercent implements ItemAbility {// PVP 대미지 증가 %
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemPVPDamagePercent();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemPVPDamagePercent(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc){
		int percent = item.getItem().getPVPDamagePercent();
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getPVPDamagePercent();
			if (value != 0) {
				percent += value;
			}
		}
		return percent;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemPVPDamagePercent();
	}
	
}

