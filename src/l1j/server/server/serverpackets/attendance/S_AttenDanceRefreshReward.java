package l1j.server.server.serverpackets.attendance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import l1j.server.GameSystem.attendance.AttendanceGroupType;
import l1j.server.GameSystem.attendance.bean.AttendanceRandomItem;
import l1j.server.server.Opcodes;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.templates.L1Item;

public class S_AttenDanceRefreshReward extends ServerBasePacket {
	private static final String S_ATTENDANCE_REFRESH_REWARD = "[S] S_AttenDanceRefreshReward";
	private byte[] _byte = null;
	public static final int REFRESH_REWARD	= 0x0994;// 보상 변경
	
	public S_AttenDanceRefreshReward(int index, AttendanceGroupType groupType, HashMap<Integer, ArrayList<AttendanceRandomItem>> randomData) {
		write_init();
		write_attendance_id(index);
		write_group_id(groupType.getGroupId());
		if (randomData != null) {
			L1Item item;
			for (Map.Entry<Integer, ArrayList<AttendanceRandomItem>> randomDetail : randomData.entrySet()) {
				write_refresh_id(randomDetail.getKey());
				ItemTable tb = ItemTable.getInstance();
				for (AttendanceRandomItem random : randomDetail.getValue()) {
					item = tb.getTemplate(random.getItemId());
					if (item == null) {
						System.out.println(String.format("[S_AttenDanceRefreshReward] CHANGE_ITEM_TEMPLATE_EMPTY : ITEMID(%d)", random.getItemId()));
						continue;
					}
					write_random_item(item.getItemNameId());
				}
				break;
			}
		}
		write_season_num(groupType.getSeasonId());
		writeH(0);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(REFRESH_REWARD);
	}
	
	void write_error(S_AttenDanceRefreshReward.eErrorType error) {
		writeRaw(0x08);
		writeRaw(error.value);
	}
	
	void write_attendance_id(int attendance_id) {
		writeRaw(0x10);// attendance_id
		writeRaw(attendance_id);
	}
	
	void write_group_id(int group_id) {
		writeRaw(0x18);// group_id
		writeRaw(group_id);
	}
	
	void write_refresh_id(int refresh_id) {
		writeRaw(0x20);// refresh_id 교체 횟수
		writeRaw(refresh_id);
	}
	
	void write_random_item(int random_item) {
		writeRaw(0x28);// random_item
		writeBit(random_item);
	}
	
	void write_season_num(int season_num) {
		writeRaw(0x30);// season_num
		writeRaw(season_num);
	}
	
	public enum eErrorType{
		error_block(1),
		error_fail(2),
		error_money(3),
		error_maxcount(4),
		error_invalid(5),
		error_wait(6),
		error_xml(7),
		;
		private int value;
		eErrorType(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eErrorType v){
			return value == v.value;
		}
		public static eErrorType fromInt(int i){
			switch(i){
			case 1:
				return error_block;
			case 2:
				return error_fail;
			case 3:
				return error_money;
			case 4:
				return error_maxcount;
			case 5:
				return error_invalid;
			case 6:
				return error_wait;
			case 7:
				return error_xml;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eErrorType, %d", i));
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
		return S_ATTENDANCE_REFRESH_REWARD;
	}
}
