package l1j.server.server.serverpackets.indun;

import l1j.server.Config;
import l1j.server.IndunSystem.indun.IndunInfo;
import l1j.server.common.data.Gender;
import l1j.server.common.data.eArenaRole;
import l1j.server.common.data.eArenaTeam;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.BinaryOutputStream;

public class ArenaActorInfoStream extends BinaryOutputStream {

	public ArenaActorInfoStream(IndunInfo info, L1PcInstance pc) {
		super();
		write_arena_char_id(pc.getId());
		write_role(eArenaRole.Player);
		write_server_id(Config.VERSION.SERVER_NUMBER);
		write_character_name(pc.getName().getBytes());
		write_character_class(pc.getType());
		write_gender(pc.getGender());
		write_team_id(eArenaTeam.TEAM_A);
		write_marker_id(info.infoUserList.indexOf(info.getUserInfo(pc)) + 1);
	}
	
	void write_arena_char_id(int arena_char_id) {
		writeC(0x08);// arena_char_id
		writeBit(arena_char_id);
	}
	
	void write_role(eArenaRole role) {
		writeC(0x10);// role
		writeC(role.toInt());
	}
	
	void write_server_id(int server_id) {
		writeC(0x18);// server_id
		writeBit(server_id);
	}
	
	void write_character_name(byte[] character_name) {
		writeC(0x22);// character_name
		writeBytesWithLength(character_name);
	}
	
	void write_character_class(int character_class) {
		writeC(0x28);// character_class
		writeC(character_class);
	}
	
	void write_gender(Gender gender) {
		writeC(0x30);// gender
		writeC(gender.toInt());
	}
	
	void write_team_id(eArenaTeam team_id) {
		writeC(0x38);// team_id
		writeC(team_id.toInt());
	}
	
	void write_marker_id(int marker_id) {
		writeC(0x40);// marker_id
		writeC(marker_id);
	}
	
}

