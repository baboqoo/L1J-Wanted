package l1j.server.server.serverpackets.party;

import l1j.server.Config;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.BinaryOutputStream;

public class PartyMemberInfo extends BinaryOutputStream {
	protected PartyMemberInfo() {
		super();
	}
	
	protected PartyMemberInfo write_member_info(L1PcInstance member) {
		write_name(member.getName());
		write_accountid(0);
		write_object_id(member.getId());
		write_game_class(member.getType());
		write_gender(member.getGender().toInt());
		write_hp_ratio(member.getCurrentHpPercent());
		write_mp_ratio(member.getCurrentMpPercent());
		write_world(member.getMapId());
		write_location(member.getLongLocationReverse());
		write_party_mark(member._partyMark);
		write_server_no(Config.VERSION.SERVER_NUMBER);
		write_cache_server_no(Config.VERSION.SERVER_NUMBER);
		return this;
	}
	
	void write_name(String name) {
		writeC(0x0a);// name
		writeS2(name);
	}
	
	void write_accountid(int accountid) {
		writeC(0x10);// accountid
		writeC(accountid);
	}
	
	void write_object_id(int object_id) {
		writeC(0x18);// object_id
		writeBit(object_id);
	}
	
	void write_game_class(int game_class) {
		writeC(0x20);// game_class
		writeC(game_class);
	}
	
	void write_gender(int gender) {
		writeC(0x28);// gender
		writeC(gender);
	}
	
	void write_hp_ratio(int hp_ratio) {
		writeC(0x30);// hp_ratio
		writeBit(hp_ratio);
	}
	
	void write_mp_ratio(int mp_ratio) {
		writeC(0x38);// mp_ratio
		writeBit(mp_ratio);
	}
	
	void write_world(int world) {
		writeC(0x40);// world
		writeBit(world);
	}
	
	void write_location(int location) {
		writeC(0x48);// location
		writeBit(location);
	}
	
	void write_party_mark(int party_mark) {
		writeC(0x50);// party_mark
		writeC(party_mark);
	}
	
	void write_alive_time_stamp(int alive_time_stamp) {
		writeC(0x58);// alive_time_stamp
		writeBit(alive_time_stamp);
	}
	
	void write_server_no(int server_no) {
		writeC(0x60);// server_no
		writeBit(server_no);
	}
	
	void write_activated_spell(int activated_spell) {
		writeC(0x6a);// activated_spell
		writeBit(activated_spell);
	}
	
	void write_cache_server_no(int cache_server_no) {
		writeC(0x70);// cache_server_no
		writeBit(cache_server_no);
	}
	
}

