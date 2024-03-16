package l1j.server.server.model.item.ablity;

import l1j.server.server.construct.item.L1ItemArmorType;
import l1j.server.server.construct.item.L1ItemType;
import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemAcSub implements ItemAbility {// 악세서리 방어력
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemAcSub();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemAcSub(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc){
		int ac = item.getItem().getAcSub();
		L1ItemType itemType = item.getItem().getItemType();
		int itemId = item.getItemId();
		int enchantlevel = item.getEnchantLevel();
		
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getAcSub();
			if (value != 0) {
				ac += value;
			}
		}
		
		if (itemType == L1ItemType.ARMOR && (item.getItem().getGrade() >= 0 && item.getItem().getGrade() <= 2) && (item.getItem().getType() == L1ItemArmorType.AMULET.getId() || item.getItem().getType() == L1ItemArmorType.EARRING.getId()) && enchantlevel > 4) {// 귀걸이,목걸이 +6부터
			return enchantlevel >= 10 ? -6 : -(enchantlevel - 4);
		}
		if (((itemId >= 222295 && itemId <= 222299) || (itemId >= 222312 && itemId <= 222316) || (itemId >= 600051 && itemId <= 600075) || (itemId >= 600080 && itemId <= 600094) || (itemId >= 600700 && itemId <= 600720)) && pc != null && pc.getType() == 4) {// 엘릭서 룬
			return -3;
		}
		return ac;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemAcSub();
	}
	
}

