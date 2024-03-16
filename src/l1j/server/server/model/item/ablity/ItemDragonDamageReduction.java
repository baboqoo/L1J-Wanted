package l1j.server.server.model.item.ablity;

public class ItemDragonDamageReduction implements ItemAbility {// 드래곤 피해 감소
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemDragonDamageReduction();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemDragonDamageReduction(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc) {
		int itemId = item.getItemId();
		if (itemId == 757) {
			return 10;
		}
		return 0;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemDragonDamageReduction();
	}

}

