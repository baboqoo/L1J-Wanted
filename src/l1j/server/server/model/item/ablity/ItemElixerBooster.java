package l1j.server.server.model.item.ablity;

public class ItemElixerBooster implements ItemAbility {// 엘릭서 경험치 부스터
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemElixerBooster();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemElixerBooster(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc){
		int itemId = item.getItemId();
		if (itemId >= 600056 && itemId <= 600060) {// 85 엘릭서 룬
			return 1;
		}
		return 0;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemElixerBooster();
	}
	
}

