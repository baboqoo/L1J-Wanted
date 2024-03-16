package l1j.server.server.model.item.ablity;

import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;
import l1j.server.server.utils.StringUtil;

public class ItemMagicName implements ItemAbility {// 발동
	private static class newInstance {
		public static final ItemMagicName INSTANCE = new ItemMagicName();
	}
	public static ItemMagicName getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemMagicName(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc) {
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			String value = enchantAblity.getMagicName();
			if (!StringUtil.isNullOrEmpty(value)) {
				return value;
			}
		}
		if (item.getItem().isHasteItem()) {
			return "$1463";
		}
		return item.getItem().getMagicName();
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemMagicName();
	}
	
}

