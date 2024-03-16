package l1j.server.server.serverpackets.attendance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import l1j.server.Config;
import l1j.server.GameSystem.attendance.AttendanceGroupType;
import l1j.server.GameSystem.attendance.bean.AttendanceAccount;
import l1j.server.GameSystem.attendance.bean.AttendanceItem;
import l1j.server.GameSystem.attendance.bean.AttendanceRandomItem;
import l1j.server.common.data.AttendanceBonusType;
import l1j.server.server.Opcodes;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.BinaryOutputStream;

public class S_AttenDanceInfoNoti extends ServerBasePacket {
	private static final String S_ATTENDANCE_INFO_NOTI = "[S] S_AttenDanceInfoNoti";
	private byte[] _byte = null;
	public static final int INFO = 0x03ec;// 출석 정보
	
	public S_AttenDanceInfoNoti(L1PcInstance pc, AttendanceAccount attend, int seasonId) {
		write_init();

		// 전체 그룹 정보
		for (AttendanceGroupType groupType : AttendanceGroupType.getAllList()) {
			write_group_info(groupType, seasonId, attend);
		}
		
		ArrayList<AttendanceGroupType> useList = AttendanceGroupType.getUseList();
		// 상세 정보(잠금 상태거나 PC방버프가 없다면 출력되지 않는다)
		int dailyCount		= attend.getDailyCount();
		for (AttendanceGroupType groupType : useList) {
			if (groupType.getSeasonId() != seasonId) {
				continue;
			}
			if (groupType == AttendanceGroupType.PC_CAFE && !pc.isPCCafe()) {// PC방이 아니다.
				continue;
			}
			if (groupType.isTab() && !attend.isGroupOpen(groupType)) {// 탭을 개방하지 않앗다.
				continue;
			}
			byte[] attendBytes	= attend.getGroupData(groupType);
			if (attendBytes == null) {
				continue;
			}
			
			for (int i=0; i<attendBytes.length; i++) {
				// 0:준비, 1:시작, 2:시간완료, 3:보상완료
				int state = attendBytes[i] == 2 ? 3 
						: attendBytes[i] == 1 ? 2 
						: attendBytes[i] == 0 && attend.isCompleted() ? 0 
						: 1;
				write_attendance_info(getAttendanceInfo(attend, i, groupType, state, dailyCount));
				if (state == 0 || state == 1) {
					break;
				}
			}
		}
		
		// 잠금 상태 정보(잠금 해제시 출력되지 않는다)
		for (AttendanceGroupType groupType : useList) {
			if (!groupType.isTab() || groupType.getSeasonId() != seasonId || attend.isGroupOpen(groupType)) {
				continue;
			}
			write_attendance_decoy(groupType, attend.getCurrentIndex());
		}
		
		write_title_str(seasonId == 0 ? 6198 : 4380);
		writeH(0);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(INFO);
	}
	
	void write_group_info(AttendanceGroupType groupType, int seasonId, AttendanceAccount attend) {
		writeRaw(0x0a);// group_info
		writeRaw(0x08);
		
		writeRaw(0x08);// group_id
		writeRaw(groupType.getGroupId());
		
		writeRaw(0x10);// max_check_minute 완료까지의 시간
		writeRaw(Config.ATTEND.ATTENDANCE_DAILY_MINUTE);
		
		writeRaw(0x18);// cur_complete_today 완료 횟수
		writeRaw(groupType.getSeasonId() != seasonId ? 0 : attend.isCompleted() ? 1 : 0);
		
		writeRaw(0x20);// max_complete_today 완료가능한 총 횟수
		writeRaw(Config.ATTEND.ATTENDANCE_DAILY_MAX_COUNT);
	}
	
	void write_attendance_info(byte[] attendance_info) {
		writeRaw(0x12);// attendance_info
		writeBytesWithLength(attendance_info);
	}
	
	void write_attendance_decoy(AttendanceGroupType groupType, int index) {
		ATTENDANCE_GROUP_DECOY os = new ATTENDANCE_GROUP_DECOY(groupType.getGroupId(), index + 1, groupType.getSeasonId());
		writeRaw(0x1a);// attendance_decoy
		writeBytesWithLength(os.getBytes());
		try {
			os.close();
			os = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void write_title_str(int title_str) {
		writeRaw(0x20);// title_str 6198:출석체크, 4380:출석체크 이벤트
		writeBit(title_str);
	}
	
	byte[] getAttendanceInfo(AttendanceAccount attend, int index, AttendanceGroupType groupType, int state, int dailyCount){
		ATTENDANCE_INFO os = null;
		try {
			os = new ATTENDANCE_INFO();
			os.write_attendance_id(index + 1);
			os.write_group_id(groupType.getGroupId());
			os.write_status(state);
			os.write_playtimeminute(state == 2 || state == 3 ? Config.ATTEND.ATTENDANCE_DAILY_MINUTE : dailyCount);
			if (state == 2) {// 0:준비, 1:미완료, 2:받기전, 3:받기후
				AttendanceItem itemInfo = attend.getGroupItems(groupType).get(index + 1);
				Map<Integer, HashMap<Integer, ArrayList<AttendanceRandomItem>>> randomItemList = attend.getRandomItems(groupType);
				if (itemInfo != null && itemInfo.getBonusType() == AttendanceBonusType.RandomDiceItem
						&& randomItemList != null && !randomItemList.isEmpty()) {
					HashMap<Integer, ArrayList<AttendanceRandomItem>> randomData = randomItemList.get(index + 1);
					byte[] reservedRandom = getReservedRandom(randomData);
					os.write_reserved_random(reservedRandom);
				}
			}
			os.write_season_num(groupType.getSeasonId());
			return os.getBytes();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
					os = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	byte[] getReservedRandom(HashMap<Integer, ArrayList<AttendanceRandomItem>> randomData){
		BinaryOutputStream os = new BinaryOutputStream();
		L1Item item;
		ItemTable tb = ItemTable.getInstance();
		for (Map.Entry<Integer, ArrayList<AttendanceRandomItem>> randomDetail : randomData.entrySet()) {
			os.writeC(0x08);// refresh_count 교체 횟수
			os.writeC(randomDetail.getKey());
			
			for (AttendanceRandomItem random : randomDetail.getValue()) {
				item = tb.getTemplate(random.getItemId());
				if (item == null) {
					System.out.println(String.format("[S_AttenDanceInfoNoti] RANDOM_ITEM_TEMPLATE_EMPTY : ITEMID(%d)", random.getItemId()));
					continue;
				}
				os.writeC(0x10);// random_item
				os.writeBit(item.getItemNameId());
			}
		}
		try {
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return os.getBytes();
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
		return S_ATTENDANCE_INFO_NOTI;
	}
}
