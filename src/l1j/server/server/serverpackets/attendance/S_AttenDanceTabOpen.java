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
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.BinaryOutputStream;

public class S_AttenDanceTabOpen extends ServerBasePacket {
	private static final String S_ATTENDANCE_TAB_OPEN = "[S] S_AttenDanceTabOpen";
	private byte[] _byte = null;
	public static final int TAB_OPEN	= 0x0996;// 잠금 해제
	
	public S_AttenDanceTabOpen(AttendanceAccount attend, AttendanceGroupType groupType, byte[] attendBytes, boolean result, boolean after) {
		write_init();
		if (!result) {
			write_error(AttendErrorType.error_money);
		}
		write_group_id(groupType.getGroupId());
		if (after) {
			for (int i=0; i<attendBytes.length; i++) {
				// 0:준비, 1:시작, 2:시간완료, 3:보상완료
				int status = attendBytes[i] == 2 ? 3 
						: attendBytes[i] == 1 ? 2 
						: attendBytes[i] == 0 && attend.isCompleted() ? 0 
						: 1;
				byte[] progressBytes = getAttendanceInfo(attend, i, groupType, status, attend.getDailyCount());
				write_attendance_info(progressBytes);
				if (status == 0 || status == 1) {
					break;
				}
			}
			
			write_open_step(AttendOpenStep.open_db);
			write_season_num(groupType.getSeasonId());
		} else if (!after && result) {
			write_open_step(AttendOpenStep.open_server);
			write_season_num(groupType.getSeasonId());
		}
		writeH(0);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(TAB_OPEN);
	}
	
	void write_error(AttendErrorType error) {
		writeRaw(0x08);// error
		writeRaw(error.value);
	}
	
	void write_group_id(int group_id) {
		writeRaw(0x10);// group_id
		writeRaw(group_id);
	}
	
	void write_attendance_info(byte[] attendance_info) {
		writeRaw(0x1a);// attendance_info
		writeBytesWithLength(attendance_info);
	}
	
	void write_open_step(AttendOpenStep open_step) {
		writeRaw(0x20);// open_step
		writeRaw(open_step.value);
	}
	
	void write_season_num(int season_num) {
		writeRaw(0x28);// season_num
		writeRaw(season_num);
	}
	
	byte[] getAttendanceInfo(AttendanceAccount attend, int attendance_id, AttendanceGroupType groupType, int status, int dailyCount){
		BinaryOutputStream os = null;
		try {
			os = new BinaryOutputStream();
			os.writeC(0x08);// attendance_id
			os.writeBit(attendance_id + 1);
			
			os.writeC(0x10);// group_id
			os.writeC(groupType.getGroupId());
			
			os.writeC(0x18);// status
			os.writeC(status);
			
			os.writeC(0x20);// playtimeminute
			os.writeBit(status == 2 || status == 3 ? Config.ATTEND.ATTENDANCE_DAILY_MINUTE : dailyCount);
			
			if (status == 2) {// 0:준비, 1:미완료, 2:받기전, 3:받기후
				AttendanceItem itemInfo = attend.getGroupItems(groupType).get(attendance_id + 1);
				Map<Integer, HashMap<Integer, ArrayList<AttendanceRandomItem>>> randomItemList = attend.getRandomItems(groupType);
				if (itemInfo != null && itemInfo.getBonusType() == AttendanceBonusType.RandomDiceItem
						&& randomItemList != null && !randomItemList.isEmpty()) {
					HashMap<Integer, ArrayList<AttendanceRandomItem>> randomData = randomItemList.get(attendance_id + 1);
					byte[] reservedRandom = getReservedRandom(randomData);
					os.writeC(0x2a);// reserved_random
					os.writeBytesWithLength(reservedRandom);
				}
			}
			os.writeC(0x30);// season_num
			os.writeC(groupType.getSeasonId());
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
					System.out.println(String.format("[S_AttenDanceTabOpen] RANDOM_ITEM_TEMPLATE_EMPTY : ITEMID(%d)", random.getItemId()));
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
	
	public enum AttendErrorType{
		error_block(1),
		error_fail(2),
		error_money(3),
		error_safepos(4),
		error_invalid(5),
		error_wait(6),
		error_wait_state(7),
		error_xml(8),
		error_system(9),
		error_unknown(10),
		error_fail_db(11),
		error_fail_group(12),
		;
		private int value;
		AttendErrorType(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(AttendErrorType v){
			return value == v.value;
		}
		public static AttendErrorType fromInt(int i){
			switch(i){
			case 1:
				return error_block;
			case 2:
				return error_fail;
			case 3:
				return error_money;
			case 4:
				return error_safepos;
			case 5:
				return error_invalid;
			case 6:
				return error_wait;
			case 7:
				return error_wait_state;
			case 8:
				return error_xml;
			case 9:
				return error_system;
			case 10:
				return error_unknown;
			case 11:
				return error_fail_db;
			case 12:
				return error_fail_group;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eErrorType, %d", i));
			}
		}
	}
	
	public enum AttendOpenStep{
		open_server(1),
		open_db(2),
		;
		private int value;
		AttendOpenStep(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(AttendOpenStep v){
			return value == v.value;
		}
		public static AttendOpenStep fromInt(int i){
			switch(i){
			case 1:
				return open_server;
			case 2:
				return open_db;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eOpenStep, %d", i));
			}
		}
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
		return S_ATTENDANCE_TAB_OPEN;
	}
}
