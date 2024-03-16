package l1j.server.server.model.item.ablity;

import l1j.server.common.data.Material;
import l1j.server.server.construct.item.L1ItemType;

public class ItemUndead implements ItemAbility {// 언데드 추가 대미지
	private static class newInstance {
		public static final ItemAbility INSTANCE = new ItemUndead();
	}
	public static ItemAbility getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemUndead(){}
	
	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc) {
		return item.getItem().getItemType() == L1ItemType.WEAPON 
				&& (Material.isUndeadMaterial(item.getItem().getMaterial()) || item.getItem().getBless() == 0);// 은·미스릴·오리하르콘, 축복
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemUndead();
	}

}

