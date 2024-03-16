package l1j.server.server.model.item.ablity;

import l1j.server.server.construct.item.L1ItemArmorType;
import l1j.server.server.construct.item.L1ItemType;
import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemToleranceFear implements ItemAbility {// 공포 내성
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemToleranceFear();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemToleranceFear(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc){
		int regist = item.getItem().getToleranceFear();
		int enchantlevel = item.getEnchantLevel();
		L1ItemType itemType = item.getItem().getItemType();
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getToleranceFear();
			if (value != 0) {
				regist += value;
			}
		}
		if (itemType == L1ItemType.ARMOR && (item.getItem().getGrade() >= 0 && item.getItem().getGrade() <= 2) 
				&& (item.getItem().getType() == L1ItemArmorType.AMULET.getId() || item.getItem().getType() == L1ItemArmorType.BELT.getId()) 
				&& enchantlevel >= 10) {// 목걸이, 벨트
			return regist += 2;
		}
		return regist;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemToleranceFear();
	}
	
}

