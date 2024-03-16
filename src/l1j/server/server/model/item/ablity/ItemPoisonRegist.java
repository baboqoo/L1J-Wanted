package l1j.server.server.model.item.ablity;

public class ItemPoisonRegist implements ItemAbility {// 독 내성
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemPoisonRegist();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemPoisonRegist(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc) {
		return item.getItem().isPoisonRegist() ? 2 : 0;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemPoisonRegist();
	}

}

