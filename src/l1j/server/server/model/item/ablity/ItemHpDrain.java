package l1j.server.server.model.item.ablity;

public class ItemHpDrain implements ItemAbility {// 흡수: HP
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemHpDrain();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemHpDrain(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc) {
		int itemId = item.getItemId();
		if (itemId == 12 || itemId == 601 || itemId == 1123 || itemId == 52022 || itemId == 52026 || itemId == 203048 || itemId == 203060) {
			return 1;
		}
		return 0;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemHpDrain();
	}

}

