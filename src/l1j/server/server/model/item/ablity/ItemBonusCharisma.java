package l1j.server.server.model.item.ablity;

import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemBonusCharisma implements ItemAbility {// cha
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemBonusCharisma();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemBonusCharisma(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc){
		byte cha = item.getItem().getAddCha();
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			byte value = enchantAblity.getCha();
			if (value != 0) {
				cha += value;
			}
		}
		return cha;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemBonusCharisma();
	}
	
}

