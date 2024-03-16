package l1j.server.server.model.item.ablity;

import l1j.server.server.construct.item.L1ItemArmorType;
import l1j.server.server.construct.item.L1ItemType;
import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemDamageReduction implements ItemAbility {// 대미지 감소
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemDamageReduction();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemDamageReduction(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc){
		int reduc = item.getItem().getDamageReduction();
		L1ItemType itemType = item.getItem().getItemType();
		int itemId = item.getItemId();
		int enchantlevel = item.getEnchantLevel();
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getReduction();
			if (value != 0) {
				reduc += value;
			}
		}

		if (itemType == L1ItemType.ARMOR && item.getItem().getType() == L1ItemArmorType.BELT.getId() && enchantlevel >= 5) {//벨트 5이상
			return reduc += (enchantlevel - 4);
		}
		
		if (pc != null) {
			int class_type = pc.getType();
			if ((itemId == 222295 || itemId == 222296 || itemId == 222297 || itemId == 222298 || itemId == 222299) && (class_type == 0 || class_type == 8 || class_type == 9)) {//55 엘릭서룬
				return reduc += 3;
			}
			if ((itemId >= 222312 && itemId <= 222316) || (itemId >= 600051 && itemId <= 600075) || (itemId >= 600080 && itemId <= 600094) || (itemId >= 600700 && itemId <= 600720)) {//70, 75, 80, 85, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 영광의 엘릭서룬
				switch(class_type) {
				case 0:case 8:case 9:
					return reduc += 3;
				case 5:
					return reduc += 1;
				}
			}
		}
		return reduc;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemDamageReduction();
	}
	
}

