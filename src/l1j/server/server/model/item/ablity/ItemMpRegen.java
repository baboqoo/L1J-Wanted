package l1j.server.server.model.item.ablity;

import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemMpRegen implements ItemAbility {// MP 회복
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemMpRegen();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemMpRegen(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc){
		int regen = item.getItem().getAddMpr();
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getMpRegen();
			if (value != 0) {
				regen += value;
			}
		}
		
		if (pc != null && pc.getType() == 3) {
			int itemId = item.getItemId();
			if (itemId == 222295 || itemId == 222296 || itemId == 222297 || itemId == 222298 || itemId == 222299) {//55 엘릭서룬
				return regen += 3;
			}
			if ((itemId >= 222312 && itemId <= 222316) || (itemId >= 600051 && itemId <= 600075) || (itemId >= 600080 && itemId <= 600094) || (itemId >= 600700 && itemId <= 600720)) {//70, 75, 80, 85, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 영광 엘릭서룬
				return regen += 3;
			}
		}
		return regen;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemMpRegen();
	}
	
}

