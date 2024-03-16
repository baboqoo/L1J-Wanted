package l1j.server.server.model.item.ablity;

import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemBonusIntelligence implements ItemAbility {// int
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemBonusIntelligence();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemBonusIntelligence(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc){
		byte inti = item.getItem().getAddInt();
		int itemId = item.getItemId();
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			byte value = enchantAblity.getInt();
			if (value != 0) {
				inti += value;
			}
		}
		if ((itemId == 200505 || itemId == 93005 || itemId == 93007) && pc != null && (pc.getType() == 3 || pc.getType() == 6)) {
			return inti += 2;
		}
		return inti;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemBonusIntelligence();
	}
	
}

