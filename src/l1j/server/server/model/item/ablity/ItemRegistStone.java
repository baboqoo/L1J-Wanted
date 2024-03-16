package l1j.server.server.model.item.ablity;

import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemRegistStone implements ItemAbility {// 석화 내성
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemRegistStone();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemRegistStone(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc) {
		int regist = item.getItem().getRegistStone();
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			int value = enchantAblity.getRegistStone();
			if (value != 0) {
				regist += value;
			}
		}
		return regist;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemRegistStone();
	}

}

