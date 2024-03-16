package l1j.server.server.model.item.collection.time.bean;

import java.util.HashMap;
import java.util.LinkedList;

import l1j.server.common.bin.timecollection.TimeCollection;
import l1j.server.server.utils.StringUtil;

/**
 * 실렉티스 전시회 완성 가동 시간 오브젝트
 * @author LinOffice
 */
public class L1TimeCollectionDuration {
	private long defaultTime;// 기본 완성 시간
	private HashMap<Integer, LinkedList<EnchantBonusTime>> bonusTimes;// key: slotId, value: LinkedList<EnchantBonusTime>
	
	public L1TimeCollectionDuration(TimeCollection obj, TimeCollection.GroupT.SetT setT) {
		this.defaultTime	= StringUtil.get_time_string_to_long(setT.get_DefaultTime());
		this.bonusTimes		= new HashMap<Integer, LinkedList<EnchantBonusTime>>();
		
		for (TimeCollection.GroupT.SetT.ItemSlotT slotT : setT.get_ItemSlot()) {
			LinkedList<EnchantBonusTime> bonusTimList = bonusTimes.get(slotT.get_Slot());
			if (bonusTimList == null) {
				bonusTimList = new LinkedList<L1TimeCollectionDuration.EnchantBonusTime>();
				bonusTimes.put(slotT.get_Slot(), bonusTimList);
			}
			
			for (TimeCollection.EnchantSectionT.EnchantIDT enchant : obj.get_EnchantSection().get_EnchantID()) {
				if (enchant.get_ID() == slotT.get_EnchantID()) {
					for (TimeCollection.EnchantSectionT.EnchantIDT.EnchantBonusT bonus : enchant.get_EnchantBonus()) {
						bonusTimList.add(new EnchantBonusTime(bonus.get_Enchant(), StringUtil.get_time_string_to_long(bonus.get_BonusTime())));
					}
					break;
				}
			}
		}
	}
	
	/**
	 * 완성 기본 시간 조사
	 * @return time
	 */
	public long getDefaultTime() {
		return defaultTime;
	}

	/**
	 * 슬롯에 등록할 아이템의 보너스 시간을 조사한다.
	 * @param slot_id
	 * @param enchantLevel
	 * @return time
	 */
	public long getBonusTime(int slot_id, int enchantLevel) {
		LinkedList<EnchantBonusTime> bonusList = bonusTimes.get(slot_id);
		if (bonusList != null && !bonusList.isEmpty()) {
			EnchantBonusTime bonus = null;
			for (int i=bonusList.size() - 1; i>=0; i--) {
				bonus = bonusList.get(i);
				if (enchantLevel >= bonus.enchant) {// 아이템의 인챈트 수치가 크다면 마지막 수치를 반환한다.
					return bonus.time;
				}
			}
		}
		return 0L;
	}

	/**
	 * 인챈트 수치에 대한 보너스 시간
	 */
	private static class EnchantBonusTime {
		private int enchant;
		private long time;
		private EnchantBonusTime(int enchant, long time) {
			this.enchant = enchant;
			this.time = time;
		}
	}
}

