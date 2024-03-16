package l1j.server.server.model.item.ablity;

public class ItemReflectEmasculate implements ItemAbility {// 반격 회피 무시
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemReflectEmasculate();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemReflectEmasculate(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc){
		int itemId = item.getItemId();
		if (itemId == 203060) {// 실렌의 결의
			return 1;
		}
		return 0;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemReflectEmasculate();
	}
	
}

