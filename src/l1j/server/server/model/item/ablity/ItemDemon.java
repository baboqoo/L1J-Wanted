package l1j.server.server.model.item.ablity;

import l1j.server.server.construct.item.L1ItemType;

public class ItemDemon implements ItemAbility {// 추가 대미지 데몬
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemDemon();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemDemon(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc) {
		return item.getItem().getItemType() == L1ItemType.WEAPON && item.getBless() == 0;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemDemon();
	}

}

