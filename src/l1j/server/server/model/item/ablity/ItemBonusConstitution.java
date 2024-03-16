package l1j.server.server.model.item.ablity;

import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;

public class ItemBonusConstitution implements ItemAbility {// con
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemBonusConstitution();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemBonusConstitution(){}
	
	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc){
		byte con			= item.getItem().getAddCon();
		L1EnchantAblity enchantAblity = item.getEnchantAblity();
		if (enchantAblity != null) {
			byte value = enchantAblity.getCon();
			if (value != 0) {
				con += value;
			}
		}
		return con;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemBonusConstitution();
	}
	
}

