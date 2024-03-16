package l1j.server.server.model;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.utils.StringUtil;

/**
 * 아이템 장착 세트
 * L1PcInstance에 할당된다.
 * @author LinOffice
 */
public class L1EquipmentSet {
	private int current_set;
	private ConcurrentHashMap<Integer, EquipSet> equip_sets;
	
	public L1EquipmentSet() {
		equip_sets = new ConcurrentHashMap<>();
		equip_sets.put(0, new EquipSet(0, new ArrayList<L1ItemInstance>(), StringUtil.EmptyString, 0));
		equip_sets.put(1, new EquipSet(1, new ArrayList<L1ItemInstance>(), StringUtil.EmptyString, 0));
		equip_sets.put(2, new EquipSet(2, new ArrayList<L1ItemInstance>(), StringUtil.EmptyString, 0));
		equip_sets.put(3, new EquipSet(3, new ArrayList<L1ItemInstance>(), StringUtil.EmptyString, 0));
	}
	
	public int getCurrentSet() {
		return current_set;
	}
	public void setCurrentSet(int current_set) {
		this.current_set = current_set;
	}
	
	public ConcurrentHashMap<Integer, EquipSet> getEquipSets() {
		return equip_sets;
	}
	public EquipSet getEquipSet(int set) {
		return equip_sets.get(set);
	}
	
	public void reset() {
		current_set = 0;
		for (EquipSet slot : equip_sets.values()) {
			slot.slot_items.clear();
			slot.slot_name = StringUtil.EmptyString;
			slot.slot_color = 0;
		}
	}

	public static class EquipSet {
		private int equip_set;
		private ArrayList<L1ItemInstance> slot_items;
		private String slot_name;
		private int slot_color;
		
		public EquipSet(int equip_set, ArrayList<L1ItemInstance> slot_items, String slot_name, int slot_color) {
			this.equip_set	= equip_set;
			this.slot_items	= slot_items;
			this.slot_name	= slot_name;
			this.slot_color	= slot_color;
		}
		
		public int getEquipSet() {
			return equip_set;
		}
		public ArrayList<L1ItemInstance> getSlotItems() {
			return slot_items;
		}
		public void setSlotItems(ArrayList<L1ItemInstance> slot_items) {
			this.slot_items = slot_items;
		}
		public String getSlotName() {
			return slot_name;
		}
		public void setSlotName(String slot_name) {
			this.slot_name = slot_name;
		}
		public int getSlotColor() {
			return slot_color;
		}
		public void setSlotColor(int slot_color) {
			this.slot_color = slot_color;
		}
		
		public String toItemString() {
			StringBuilder sb = new StringBuilder();
			for (L1ItemInstance item : slot_items) {
				if (sb.length() > 0) {
					sb.append(StringUtil.LineString);
				}
				sb.append(item.getId());
			}
			return sb.toString();
		}
	}
	
}

