package l1j.server.server.model.item.ablity;

import l1j.server.server.construct.item.L1ItemArmorType;
import l1j.server.server.construct.item.L1ItemType;
import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemToleranceAll implements ItemAbility {// 전체 내성
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemToleranceAll();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemToleranceAll(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc){
		int regist = item.getItem().getToleranceAll();
		int enchantlevel = item.getEnchantLevel();
		L1ItemType itemType = item.getItem().getItemType();
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getToleranceAll();
			if (value != 0) {
				regist += value;
			}
		}
		if (itemType == L1ItemType.ARMOR && (item.getItem().getGrade() >= 0 && item.getItem().getGrade() <= 2) 
				&& (L1ItemArmorType.isRing(item.getItem().getType()) || item.getItem().getType() == L1ItemArmorType.EARRING.getId()) 
				&& enchantlevel >= 10) {// 귀걸이, 반지
			return regist += 1;
		}
		return regist;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemToleranceAll();
	}
	
}

