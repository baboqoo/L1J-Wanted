package l1j.server.server.model.item.ablity;

import l1j.server.server.model.L1Cooking;

public class ItemFoodType implements ItemAbility {// 음식
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemFoodType();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemFoodType(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc) {
		int itemId	= item.getItemId();
		if (L1Cooking.isCooking(itemId)) {// 요리
			return 0;
		}
		if (L1Cooking.isSoup(itemId)) {// 스프
			return 1;
		}
		return -1;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemFoodType();
	}

}

