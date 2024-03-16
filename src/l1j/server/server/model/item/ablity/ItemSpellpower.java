package l1j.server.server.model.item.ablity;

import l1j.server.server.construct.item.L1ItemArmorType;
import l1j.server.server.construct.item.L1ItemType;
import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemSpellpower implements ItemAbility {// SP + N
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemSpellpower();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemSpellpower(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc){
		int sp = item.getItem().getAddSp();
		L1ItemType itemType = item.getItem().getItemType();
		int itemId = item.getItemId();
		int enchantlevel = item.getEnchantLevel();
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getSpellpower();
			if (value != 0) {
				sp += value;
			}
		}

		if (itemType == L1ItemType.ARMOR && (item.getItem().getGrade() >= 0 && item.getItem().getGrade() <= 2) && L1ItemArmorType.isRing(item.getItem().getType()) && enchantlevel >= 7) {//반지
			return sp += enchantlevel - 6;
		}
		
		if (pc != null) {
			int class_type = pc.getType();
			if (((itemId >= 222312 && itemId <= 222316) || (itemId >= 600051 && itemId <= 600075) || (itemId >= 600080 && itemId <= 600094) || (itemId >= 600700 && itemId <= 600720)) && class_type == 3) {//70, 75, 80, 85, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 영광의 엘릭서룬
				return sp += 1;
			}
			if (itemId == 22400 && (class_type == 3 || class_type == 6)) {
				return sp += 2;
			}
		}

		return sp;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemSpellpower();
	}
	
}

