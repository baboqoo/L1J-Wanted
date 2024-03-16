package l1j.server.server.serverpackets.revenge;

import l1j.server.server.utils.BinaryOutputStream;

public class RevengeInfoStream extends BinaryOutputStream {
	
	public RevengeInfoStream() {
		super();
	}
	
	void write_register_timestamp(int register_timestamp) {
		writeC(0x08);// register_timestamp
		writeBit(register_timestamp);
	}
	
	void write_unregister_duration(int unregister_duration) {
		writeC(0x10);// unregister_duration
		writeBit(unregister_duration);
	}
	
	void write_action_type(S_RevengeInfo.eAction action_type) {
		writeC(0x18);// action_type
		writeC(action_type.toInt());
	}
	
	void write_action_result(S_RevengeInfo.eResult action_result) {
		writeC(0x20);// action_result
		writeC(action_result.toInt());
	}
	
	void write_action_timestamp(int action_timestamp) {
		writeC(0x28);// action_timestamp
		writeBit(action_timestamp);
	}
	
	void write_action_duration(int action_duration) {
		writeC(0x30);// action_duration
		writeBit(action_duration);
	}
	
	void write_action_remain_count(int action_remain_count) {
		writeC(0x38);// action_remain_count
		writeC(action_remain_count);
	}
	
	void write_action_count(int action_count) {
		writeC(0x40);// action_count
		writeBit(action_count);
	}
	
	void write_crimescene_server_no(int crimescene_server_no) {
		writeC(0x48);// crimescene_server_no
		writeBit(crimescene_server_no);
	}
	
	void write_user_uid(int user_uid) {
		writeC(0x50);// user_uid
		writeBit(user_uid);
	}
	
	void write_server_no(int server_no) {
		writeC(0x58);// server_no
		writeBit(server_no);
	}
	
	void write_game_class(int game_class) {
		writeC(0x60);// game_class
		writeC(game_class);
	}
	
	void write_user_name(String user_name) {
		writeC(0x6a);// user_name
		writeStringWithLength(user_name);
	}
	
	void write_pledge_id(int pledge_id) {
		writeC(0x70);// pledge_id
		writeBit(pledge_id);
	}
	
	void write_pledge_name(String pledge_name) {
		writeC(0x7a);// pledge_name
		writeStringWithLength(pledge_name);
	}
	
	void write_active(boolean active) {
		writeH(0x0180);// active
		writeB(active);
	}
	
	void write_activate_duration(int activate_duration) {
		writeH(0x0188);// activate_duration
		writeBit(activate_duration);
	}
	
	void write_anonymity_name(boolean anonymity_name) {
		writeH(0x0190);// anonymity_name
		writeB(anonymity_name);
	}
	
}

