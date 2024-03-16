package l1j.server.server.serverpackets.attendance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import l1j.server.GameSystem.attendance.AttendanceGroupType;
import l1j.server.GameSystem.attendance.bean.AttendanceRandomItem;
import l1j.server.server.Opcodes;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.BinaryOutputStream;

public class S_AttenDanceCompleteNoti extends ServerBasePacket {
	private static final String S_ATTENDANCE_COMPLETE_NOTI = "[S] S_AttenDanceCompleteNoti";
	private byte[] _byte = null;
	public static final int COMPLETE	= 0x03ed;// 완료 알림
	
	public S_AttenDanceCompleteNoti(int index, AttendanceGroupType groupType, HashMap<Integer, ArrayList<AttendanceRandomItem>> randomData) {
		write_init();
		write_attendance_id(index);
		write_group_id(groupType.getGroupId());
		write_status(2);
		if (randomData != null) {
			write_reserved_random(getReservedRandom(randomData));
		}
		write_season_num(groupType.getSeasonId());
		writeH(0);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(COMPLETE);
	}
	
	void write_attendance_id(int attendance_id) {
		writeRaw(0x08);// attendance_id
		writeRaw(attendance_id);
	}
	
	void write_group_id(int group_id) {
		writeRaw(0x10);// group_id
		writeRaw(group_id);
	}
	
	void write_status(int status) {
		writeRaw(0x18);// status
		writeRaw(status);
	}
	
	void write_reserved_random(byte[] reserved_random) {
		writeRaw(0x22);// reserved_random
		writeBytesWithLength(reserved_random);
	}
	
	void write_season_num(int season_num) {
		writeRaw(0x28);// season_num
		writeRaw(season_num);
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
					System.out.println(String.format("[S_AttenDanceCompleteNoti] RANDOM_ITEM_TEMPLATE_EMPTY : ITEMID(%d)", random.getItemId()));
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
		return S_ATTENDANCE_COMPLETE_NOTI;
	}
}
