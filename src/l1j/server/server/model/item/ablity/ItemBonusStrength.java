package l1j.server.server.model.item.ablity;

import l1j.server.server.construct.item.L1ItemWeaponType;
import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemBonusStrength implements ItemAbility {// str
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemBonusStrength();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemBonusStrength(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc){
		byte str = item.getItem().getAddStr();
		int itemId = item.getItemId();
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			byte value = enchantAblity.getStr();
			if (value != 0) {
				str += value;
			}
		}
		if ((itemId == 200505 || itemId == 93005 || itemId == 93007) && pc != null && !(pc.getType() == 2 && pc.getWeapon() != null && L1ItemWeaponType.isBowWeapon(pc.getWeapon().getItem().getWeaponType())) && pc.getType() != 3 && pc.getType() != 6) {
			return str += 2;
		}
		return str;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemBonusStrength();
	}
}

