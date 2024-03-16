package l1j.server.server.model.item.ablity;

public class ItemAddExpPercent implements ItemAbility {// 경험치 추가 %
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemAddExpPercent();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemAddExpPercent(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc) {
		int itemId = item.getItemId();
		if (itemId == 30207 || itemId == 30105) {
			return 20;
		}
		return 0;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemAddExpPercent();
	}

}

