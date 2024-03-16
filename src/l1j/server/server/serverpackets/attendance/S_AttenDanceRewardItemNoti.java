package l1j.server.server.serverpackets.attendance;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.GameSystem.attendance.AttendanceGroupType;
import l1j.server.GameSystem.attendance.bean.AttendanceRewardHistory;
import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_AttenDanceRewardItemNoti extends ServerBasePacket {
	private static final String S_ATTENDANCE_REWARD_ITEM_NOTI = "[S] S_AttenDanceRewardItemNoti";
	private byte[] _byte = null;
	public static final int NOTI = 0x03f1;// 보상 결과

	public S_AttenDanceRewardItemNoti(ConcurrentHashMap<AttendanceGroupType, ArrayList<AttendanceRewardHistory>> historys) {
		write_init();

		ArrayList<AttendanceRewardHistory> list;
		AttendanceRewardHistory completed;
		for (AttendanceGroupType groupType : AttendanceGroupType.getUseList()) {
			list = historys.get(groupType);
			if (list == null || list.isEmpty()) {
				continue;
			}
			for (int i=0; i<list.size(); i++) {
				completed = list.get(i);
				if (completed == null) {
					continue;
				}
				write_reward_item_info(groupType, completed);
			}
		}

		writeH(0);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_reward_item_info(AttendanceGroupType groupType, AttendanceRewardHistory completed) {
		int length = getBitSize(completed.getItemDescId()) + getBitSize(completed.getItemCount()) + 6;
		writeRaw(0x0a);// reward_item_info
		writeRaw(length);
		
		writeRaw(0x08);// attendance_id
		writeRaw(completed.getIndex());
		
		writeRaw(0x10);// group_id
		writeRaw(groupType.getGroupId());
		
		writeRaw(0x18);// item_name_id
		writeBit(completed.getItemDescId());
		
		writeRaw(0x20);// item_count
		writeBit(completed.getItemCount());
		
		writeRaw(0x28);// season_num
		writeRaw(groupType.getSeasonId());
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
		return S_ATTENDANCE_REWARD_ITEM_NOTI;
	}
}
