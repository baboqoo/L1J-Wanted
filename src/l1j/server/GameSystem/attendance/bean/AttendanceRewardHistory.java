package l1j.server.GameSystem.attendance.bean;

import l1j.server.GameSystem.attendance.AttendanceGroupType;

public class AttendanceRewardHistory {
	private AttendanceGroupType groupType;
	private int index;
	private int itemDescId;
	private int itemCount;
	
	public AttendanceRewardHistory(AttendanceGroupType groupType, int index, int itemDescId, int itemCount) {
		this.groupType	= groupType;
		this.index		= index;
		this.itemDescId	= itemDescId;
		this.itemCount	= itemCount;
	}
	
	public AttendanceGroupType getGroupType() {
		return groupType;
	}
	public int getIndex() {
		return index;
	}
	public int getItemDescId() {
		return itemDescId;
	}
	public int getItemCount() {
		return itemCount;
	}
}

