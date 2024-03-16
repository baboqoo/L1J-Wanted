package l1j.server.server.model.item.ablity;

import l1j.server.server.construct.item.L1ItemArmorType;
import l1j.server.server.construct.item.L1ItemType;
import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemPVPDamageReduction implements ItemAbility {// PVP 대미지 감소
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemPVPDamageReduction();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemPVPDamageReduction(){}
	
	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc){
		int reduc = item.getItem().getPVPDamageReduction();
		L1ItemType itemType = item.getItem().getItemType();
		int enchantlevel = item.getEnchantLevel();
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getPVPReduction();
			if (value != 0) {
				reduc += value;
			}
		}
		if (itemType == L1ItemType.ARMOR && item.getItem().getType() == L1ItemArmorType.BELT.getId() && enchantlevel >= 6) {// 벨트 7부터
			return reduc += enchantlevel >= 10 ? 9 : 1 + ((enchantlevel - 6) << 1);
		}
		return reduc;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemPVPDamageReduction();
	}
	
}

