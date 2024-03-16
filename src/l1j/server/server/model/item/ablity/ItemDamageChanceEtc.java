package l1j.server.server.model.item.ablity;

public class ItemDamageChanceEtc implements ItemAbility {// 추가 대미지 + N 확률
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemDamageChanceEtc();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemDamageChanceEtc(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc) {
		int itemId = item.getItemId();
		if (itemId == 41250 || itemId == 210072) {
			return 15;
		}
		return 0;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemDamageChanceEtc();
	}

}

