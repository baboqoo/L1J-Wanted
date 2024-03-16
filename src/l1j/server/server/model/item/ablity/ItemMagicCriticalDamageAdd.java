package l1j.server.server.model.item.ablity;

public class ItemMagicCriticalDamageAdd implements ItemAbility {// 마법 치명타 대미지 상승
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemMagicCriticalDamageAdd();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemMagicCriticalDamageAdd(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc){
		/*int itemId = item.getItemId();
		if (itemId == 203058) {// 에바의 서약
			return 1;
		}*/
		return 0;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemMagicCriticalDamageAdd();
	}
	
}

