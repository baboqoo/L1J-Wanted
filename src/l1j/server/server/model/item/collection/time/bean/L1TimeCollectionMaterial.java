package l1j.server.server.model.item.collection.time.bean;

import l1j.server.common.bin.timecollection.TimeCollection;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.utils.StringUtil;

/**
 * 실렉티스 전시회 재료 오브젝트
 * @author LinOffice
 */
public class L1TimeCollectionMaterial {
	private int slotIndex;
	private java.util.LinkedList<Integer> nameIds;
	private TimeCollection.EnchantSectionT.EnchantIDT enchant;
	
	public L1TimeCollectionMaterial(TimeCollection obj, TimeCollection.GroupT.SetT.ItemSlotT slotT) {
		slotIndex	= slotT.get_Slot();
		nameIds		= slotT.get_NameId();
		
		for (TimeCollection.EnchantSectionT.EnchantIDT enchant : obj.get_EnchantSection().get_EnchantID()) {
			if (enchant.get_ID() == slotT.get_EnchantID()) {
				this.enchant = enchant;
				break;
			}
		}
	}
	
	public int getSlotIndex() {
		return slotIndex;
	}
	
	/**
	 * 등록가능한 아이템인지 조사한다.
	 * @param item
	 * @return boolean
	 */
	public boolean isMaterial(L1ItemInstance item){
		// 인챈트 수치 검증
		if (item.getEnchantLevel() < enchant.get_EnchantMin() || item.getEnchantLevel() > enchant.get_EnchantMax()) {
			return false;
		}
		return nameIds.contains(item.getItem().getItemNameId());
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("slotIndex: ").append(slotIndex).append(StringUtil.LineString);
		sb.append("enchant: ").append(enchant).append(StringUtil.LineString);
		sb.append("----- nameIds -----\r\n");
		for (int name_id : nameIds) {
			sb.append(name_id).append(StringUtil.LineString);
		}
		sb.append("---------------\r\n");
		return sb.toString();
	}
}

