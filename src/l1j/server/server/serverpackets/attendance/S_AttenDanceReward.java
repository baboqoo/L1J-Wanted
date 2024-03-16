package l1j.server.server.serverpackets.attendance;

import l1j.server.GameSystem.attendance.AttendanceGroupType;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_AttenDanceReward extends ServerBasePacket {
	private static final String S_ATTENDANCE_REWARD = "[S] S_AttenDanceReward";
	private byte[] _byte = null;
	public static final int REWARD	= 0x03ef;// 보상 받기
	
	public S_AttenDanceReward(int attendance_id, AttendanceGroupType groupType, L1ItemInstance item, boolean broadcast_item) {
		write_init();
		write_attendance_id(attendance_id);
		write_group_id(groupType.getGroupId());
		write_status(3);
		write_item_name_id(item.getItem().getItemNameId());
		write_item_count(item.getCount());
		write_broadcast_item(broadcast_item);
		write_season_num(groupType.getSeasonId());
		writeH(0);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(REWARD);
	}
	
	void write_attendance_id(int attendance_id) {
		writeRaw(0x08);// attendance_id
		writeBit(attendance_id);
	}
	
	void write_group_id(int group_id) {
		writeRaw(0x10);// group_id
		writeRaw(group_id);
	}
	
	void write_status(int status) {
		writeRaw(0x18);// status
		writeRaw(status);
	}
	
	void write_item_name_id(int item_name_id) {
		writeRaw(0x20);// item_name_id
		writeBit(item_name_id);
	}
	
	void write_item_count(int item_count) {
		writeRaw(0x28);// item_count
		writeBit(item_count);
	}
	
	void write_broadcast_item(boolean broadcast_item) {
		writeRaw(0x30);// broadcast_item
		writeB(broadcast_item);
	}
	
	void write_season_num(int season_num) {
		writeRaw(0x38);// season_num
		writeRaw(season_num);
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_ATTENDANCE_REWARD;
	}
}
