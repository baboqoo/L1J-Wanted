package l1j.server.server.model.item.ablity;

import l1j.server.server.construct.item.L1ItemArmorType;
import l1j.server.server.construct.item.L1ItemType;
import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemMaxMp implements ItemAbility {// 최대 MP
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemMaxMp();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemMaxMp(){}
	
	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc){
		int mp = item.getItem().getAddMp();
		L1ItemType itemType = item.getItem().getItemType();
		int itemId = item.getItemId();
		int enchantlevel = item.getEnchantLevel();
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getMaxMp();
			if (value != 0) {
				mp += value;
			}
		}
		
		if (itemType == L1ItemType.ARMOR && item.getItem().getType() == L1ItemArmorType.BELT.getId() && enchantlevel > 0) {//벨트
			return mp += belt_mp(enchantlevel);
		}
		
		if (pc != null) {
			int class_type = pc.getType();
			if ((itemId == 222295 || itemId == 222296 || itemId == 222297 || itemId == 222298 || itemId == 222299) && class_type == 2) {//55 엘릭서룬
				return mp += 50;
			}
			if ((itemId >= 222312 && itemId <= 222316) || (itemId >= 600051 && itemId <= 600075) || (itemId >= 600080 && itemId <= 600094) || (itemId >= 600700 && itemId <= 600720)){//70, 75, 80, 85, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 영광 엘릭서룬
				if (class_type == 2) {
					return mp += 50;
				}
				if (class_type == 4) {
					return mp += 30;
				}
			}
		}

		return mp;
	}
	
	/**
	 * 벨트 MP
	 * @param enchantlevel
	 * @return MP
	 */
	int belt_mp(int enchantlevel) {
		switch (enchantlevel) {
		case 1:
			return 5;
		case 2:
			return 10;
		case 3:
			return 20;
		case 4:
			return 30;
		case 5:
		case 6:
			return 40;
		case 7:
		case 8:
			return 50;
		case 9:
			return 60;
		default:
			return 100;
		}
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemMaxMp();
	}
	
}

