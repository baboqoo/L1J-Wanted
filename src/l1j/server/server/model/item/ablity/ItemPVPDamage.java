package l1j.server.server.model.item.ablity;

import l1j.server.server.construct.item.L1ItemArmorType;
import l1j.server.server.construct.item.L1ItemType;
import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemPVPDamage implements ItemAbility {// PVP 추가 대미지
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemPVPDamage();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemPVPDamage(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc){
		int dmg = item.getItem().getPVPDamage();
		L1ItemType itemType = item.getItem().getItemType();
		int enchantlevel = item.getEnchantLevel();
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getPVPDamage();
			if (value != 0) {
				dmg += value;
			}
		}
		if (itemType == L1ItemType.ARMOR && (item.getItem().getGrade() >= 0 && item.getItem().getGrade() <= 2)
				&& L1ItemArmorType.isRing(item.getItem().getType()) && enchantlevel >= 6) {// 반지
			return dmg += enchantlevel <= 8 ? enchantlevel - 5 : enchantlevel == 9 ? 5 : 7;
		}

		return dmg;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemPVPDamage();
	}
	
}

