package l1j.server.server.model.item.ablity;

import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemPVPDamageReductionEgnor implements ItemAbility {// PVP 대미지 감소 무시
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemPVPDamageReductionEgnor();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemPVPDamageReductionEgnor(){}
	
	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc){
		int egnor = item.getItem().getPVPReductionEgnor();
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getPVPReductionEgnor();
			if (value != 0) {
				egnor += value;
			}
		}
		return egnor;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemPVPDamageReductionEgnor();
	}
	
}

