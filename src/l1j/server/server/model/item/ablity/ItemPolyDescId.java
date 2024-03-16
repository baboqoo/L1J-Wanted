package l1j.server.server.model.item.ablity;

public class ItemPolyDescId implements ItemAbility {
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemPolyDescId();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemPolyDescId(){}
	
	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc){
		return item.getItem().getPolyDescId();
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemPolyDescId();
	}
	
}

