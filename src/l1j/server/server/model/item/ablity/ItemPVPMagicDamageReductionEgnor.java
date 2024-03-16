package l1j.server.server.model.item.ablity;

import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemPVPMagicDamageReductionEgnor implements ItemAbility {// PVP 마법 대미지 감소 무시
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemPVPMagicDamageReductionEgnor();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemPVPMagicDamageReductionEgnor(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc){
		int egnor = item.getItem().getPVPMagicDamageReductionEgnor();
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getPVPReductionMagicEgnor();
			if (value != 0) {
				egnor += value;
			}
		}
		return egnor;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemPVPMagicDamageReductionEgnor();
	}
}

