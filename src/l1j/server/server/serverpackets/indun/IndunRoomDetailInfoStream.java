package l1j.server.server.serverpackets.indun;

import l1j.server.common.data.eArenaMapKind;
import l1j.server.common.data.eDistributionType;
import l1j.server.common.data.eDungeonType;
import l1j.server.server.utils.BinaryOutputStream;

public class IndunRoomDetailInfoStream extends BinaryOutputStream {
	public IndunRoomDetailInfoStream() {
		super();
	}
	
	void write_min_level(int min_level) {
		writeC(0x08);// min_level
		writeC(min_level);
	}
	
	void write_dungeon_type(eDungeonType dungeon_type) {
		writeC(0x10);// dungeon_type
		writeC(dungeon_type.toInt());
	}
	
	void write_max_player(int max_player) {
		writeC(0x18);// max_player
		writeC(max_player);
	}
	
	void write_distribution_type(eDistributionType distribution_type) {
		writeC(0x20);// distribution_type
		writeC(distribution_type.toInt());
	}
	
	void write_condition(int fee, int key_item_id) {
		writeC(0x2a);// condition
		writeC(3 + getBitSize(fee));
		
		writeC(0x08); // fee
		writeBit(fee);
		
		writeC(0x10);// key_item_id
		writeC(key_item_id);
	}
	
	void write_map_kind(eArenaMapKind map_kind) {
		writeC(0x30);// map_kind
		writeBit(map_kind.toInt());
	}
	
}

