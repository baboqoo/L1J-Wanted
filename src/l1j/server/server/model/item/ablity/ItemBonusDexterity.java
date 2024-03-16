package l1j.server.server.model.item.ablity;

import l1j.server.server.construct.item.L1ItemWeaponType;
import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemBonusDexterity implements ItemAbility {// dex
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemBonusDexterity();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemBonusDexterity(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc){
		byte dex = item.getItem().getAddDex();
		int itemId = item.getItemId();
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			byte value = enchantAblity.getDex();
			if (value != 0) {
				dex += value;
			}
		}
		if ((itemId == 200505 || itemId == 93005 || itemId == 93007) && pc != null && pc.getType() == 2 && pc.getWeapon() != null && L1ItemWeaponType.isBowWeapon(pc.getWeapon().getItem().getWeaponType())) {
			return dex += 2;
		}
		return dex;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemBonusDexterity();
	}
}

