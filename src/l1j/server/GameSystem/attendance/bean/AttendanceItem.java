package l1j.server.GameSystem.attendance.bean;

import l1j.server.common.data.AttendanceBonusType;

public class AttendanceItem {
	private int _index;
	private int _itemId;
	private int _count;
	private int _enchant;
	private boolean _broadcast;
	private AttendanceBonusType _bonusType;
	
	public AttendanceItem(int index, int itemId, int count, int enchant, boolean broadcast, AttendanceBonusType bonusType) {
		this._index			= index;
		this._itemId		= itemId;
		this._count			= count;
		this._enchant		= enchant;
		this._broadcast		= broadcast;
		this._bonusType		= bonusType;
	}
	
	public int getIndex() {
		return _index;
	}
	public int getItemId() {
		return _itemId;
	}
	public int getCount() {
		return _count;
	}
	public int getEnchant() {
		return _enchant;
	}
	public boolean isBroadcast() {
		return _broadcast;
	}
	public AttendanceBonusType getBonusType() {
		return _bonusType;
	}
}

