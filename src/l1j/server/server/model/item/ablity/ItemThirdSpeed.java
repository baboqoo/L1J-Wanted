package l1j.server.server.model.item.ablity;

import l1j.server.server.model.item.L1ItemId;

public class ItemThirdSpeed implements ItemAbility {// 3단 가속
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemThirdSpeed();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemThirdSpeed(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc){
		int itemId = item.getItemId();
		if (itemId == 3000021 || itemId == 2000030 || itemId == 900057 || L1ItemId.isAdenSpeedGraceItem(itemId)) {// 드래곤의 부츠, 영웅의 부츠, 영웅의 각반
			return 1;
		}
		return 0;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemThirdSpeed();
	}
	
}

