package l1j.server.server.serverpackets.indun;

import l1j.server.Config;
import l1j.server.IndunSystem.indun.IndunInfo;
import l1j.server.common.data.Gender;
import l1j.server.common.data.eArenaRole;
import l1j.server.common.data.eArenaTeam;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.BinaryOutputStream;

public class ArenaUserInfoStream extends BinaryOutputStream {

	public ArenaUserInfoStream(IndunInfo info, L1PcInstance pc) {
		super();
		write_arena_char_id(pc.getId());
		write_server_id(Config.VERSION.SERVER_NUMBER);
		write_character_name(pc.getName().getBytes());
		write_character_class(pc.getType());
		write_gender(pc.getGender());
		write_team_id(eArenaTeam.TEAM_A);
		write_role(eArenaRole.Player);
		write_room_owner(info.chief_id == pc.getId());
		write_ready(pc.getConfig()._IndunReady);
		write_in_room(true);
		write_order(info.infoUserList.indexOf(info.getUserInfo(pc)));
		write_enter_count(0);
	}
	
	void write_arena_char_id(int arena_char_id) {
		writeC(0x08);// arena_char_id
		writeBit(arena_char_id);
	}
	
	void write_server_id(int server_id) {
		writeC(0x10);// server_id
		writeBit(server_id);
	}
	
	void write_character_name(byte[] character_name) {
		writeC(0x1a);// character_name
		writeBytesWithLength(character_name);
	}
	
	void write_character_class(int character_class) {
		writeC(0x20);// character_class
		writeC(character_class);
	}
	
	void write_gender(Gender gender) {
		writeC(0x28);// gender
		writeC(gender.toInt());
	}
	
	void write_team_id(eArenaTeam team_id) {
		writeC(0x30);// team_id
		writeC(team_id.toInt());
	}
	
	void write_role(eArenaRole role) {
		writeC(0x38);// role
		writeC(role.toInt());
	}
	
	void write_room_owner(boolean room_owner) {
		writeC(0x40);// room_owner
		writeB(room_owner);
	}
	
	void write_ready(boolean ready) {
		writeC(0x48);// ready
		writeB(ready);
	}
	
	void write_in_room(boolean in_room) {
		writeC(0x50);// in_room
		writeB(in_room);
	}
	
	void write_order(int order) {
		writeC(0x58);// order
		writeC(order);
	}
	
	void write_enter_count(int enter_count) {
		writeC(0x60);// enter_count
		writeC(enter_count);
	}
	
}

