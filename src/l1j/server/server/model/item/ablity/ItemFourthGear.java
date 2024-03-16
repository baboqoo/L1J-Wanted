package l1j.server.server.model.item.ablity;

public class ItemFourthGear implements ItemAbility {// 4단 가속
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemFourthGear();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemFourthGear(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc){
		int itemId = item.getItemId();
		if (itemId == 776 || itemId == 777 || itemId == 778) {
			return 1;
		}
		return 0;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemFourthGear();
	}
	
}

