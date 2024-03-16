package l1j.server.server.model.item.ablity;

public class ItemBuffDurationSecond implements ItemAbility {// 지속 시간
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemBuffDurationSecond();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemBuffDurationSecond(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc) {
		return item.getItem().getBuffDurationSecond();
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemBuffDurationSecond();
	}

}

