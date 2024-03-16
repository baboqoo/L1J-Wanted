package l1j.server.GameSystem.attendance.bean;

import l1j.server.GameSystem.attendance.AttendanceGroupType;

public class AttendanceRandomItem {
	private AttendanceGroupType _groupType;
	private int _index;
	private int _itemId;
	private int _count;
	private boolean _broadcast;
	private int _level;
	
	public AttendanceRandomItem(AttendanceGroupType groupType, int index, int itemId, int count, boolean broadcast, int level) {
		this._groupType	= groupType;
		this._index		= index;
		this._itemId	= itemId;
		this._count		= count;
		this._broadcast	= broadcast;
		this._level		= level;
	}
	
	public AttendanceGroupType getGroupType() {
		return _groupType;
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
	public boolean isBroadcast() {
		return _broadcast;
	}
	public int getLevel() {
		return _level;
	}
}

