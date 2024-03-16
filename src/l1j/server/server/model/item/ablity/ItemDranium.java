package l1j.server.server.model.item.ablity;

import l1j.server.common.data.Material;

public class ItemDranium implements ItemAbility {// 추가 대미지 드라니움
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemDranium();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemDranium(){}

	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc) {
		return item.getItem().getMaterial() == Material.DRANIUM;
	}

	@Override
	public ItemAbility copyInstance() {
		return new ItemDranium();
	}
	
}

