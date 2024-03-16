package l1j.server.server.serverpackets.indun;

import l1j.server.IndunSystem.indun.IndunInfo;
import l1j.server.common.data.eArenaMapKind;
import l1j.server.common.data.eDistributionType;
import l1j.server.server.utils.BinaryOutputStream;

public class IndunRoomInfoStream extends BinaryOutputStream {

	public IndunRoomInfoStream(IndunInfo info) {
		super();
		write_room_id(info.room_id);
		write_title(info.title);
		write_member_count_cur(1);
		write_member_count_max(info.max_player);
		write_member_count_min(info.min_player);
		write_map_kind(info.map_kind);
		write_closed(info.is_closed);
		write_min_level(info.min_level);
		write_fee(info.fee);
		write_distribution_type(info.distribution_type);
		write_is_playing(info.is_playing);
		write_is_locked(info.is_locked);
	}
	
	void write_room_id(int room_id) {
		writeC(0x08);// room_id
		writeBit(room_id);
	}
	
	void write_title(byte[] title) {
		writeC(0x12);// title
		writeBytesWithLength(title);
	}
	
	void write_member_count_cur(int member_count_cur) {
		writeC(0x18);// member_count_cur
		writeC(member_count_cur);
	}
	
	void write_member_count_max(int member_count_max) {
		writeC(0x20);// member_count_max
		writeC(member_count_max);
	}
	
	void write_member_count_min(int member_count_min) {
		writeC(0x28);// member_count_min
		writeC(member_count_min);
	}
	
	void write_map_kind(eArenaMapKind map_kind) {
		writeC(0x30);// map_kind
		writeBit(map_kind.toInt());
	}
	
	void write_closed(boolean closed) {
		writeC(0x38);// closed
		writeB(closed);
	}
	
	void write_min_level(int min_level) {
		writeC(0x40);// min_level
		writeC(min_level);
	}
	
	void write_fee(int fee) {
		writeC(0x48);// fee
		writeBit(fee);
	}
	
	void write_distribution_type(eDistributionType distribution_type) {
		writeC(0x50);// distribution_type
		writeC(distribution_type.toInt());
	}
	
	void write_is_playing(boolean is_playing) {
		writeC(0x58);// is_playing
		writeB(is_playing);
	}
	
	void write_is_locked(boolean is_locked) {
		writeC(0x60);// is_locked
		writeB(is_locked);
	}
	
}

