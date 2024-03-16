package l1j.server.server.model.item.ablity;

import l1j.server.server.construct.item.L1ItemArmorType;
import l1j.server.server.construct.item.L1ItemType;
import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemToleranceSkill implements ItemAbility {// 기술 내성
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemToleranceSkill();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemToleranceSkill(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc){
		int regist = item.getItem().getToleranceSkill();
		L1ItemType itemType = item.getItem().getItemType();
		int enchantlevel = item.getEnchantLevel();
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getToleranceSkill();
			if (value != 0) {
				regist += value;
			}
		}
		if (itemType == L1ItemType.ARMOR && (item.getItem().getGrade() >= 0 && item.getItem().getGrade() <= 2) 
				&& (item.getItem().getType() == L1ItemArmorType.EARRING.getId() || item.getItem().getType() == L1ItemArmorType.AMULET.getId()) 
				&& enchantlevel >= 7) {// 귀걸이 목걸이
			return regist += enchantlevel <= 9 ? enchantlevel - 5 : 5;
		}
		return regist;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemToleranceSkill();
	}
	
}

