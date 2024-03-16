package l1j.server.server.model.item.ablity;

public class ItemMpDrain implements ItemAbility {// 흡수 MP
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemMpDrain();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemMpDrain(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc) {
		int itemId = item.getItemId();
		if (itemId == 126 || itemId == 127 || itemId == 602 || itemId == 52024 || itemId == 203058) {
			return 1;
		}
		return 0;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemMpDrain();
	}

}

