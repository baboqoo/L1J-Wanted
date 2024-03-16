package l1j.server.server.model.item.ablity;

public class ItemAddExpPercentPC implements ItemAbility {// PC방 경험치 추가 %
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemAddExpPercentPC();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemAddExpPercentPC(){}
	
	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc){
		int itemId = item.getItemId();
		if (itemId == 30105) {
			return 80;
		}
		if (itemId == 30209) {
			return 20;
		}
		if (itemId == 520282) {
			return 93;
		}
		return 0;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemAddExpPercentPC();
	}

}

