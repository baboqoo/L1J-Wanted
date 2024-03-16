package l1j.server.server.serverpackets;

import l1j.server.Config;
import l1j.server.server.Opcodes;
import l1j.server.server.model.gametime.GameTimeClock;

public class S_ServerVersion extends ServerBasePacket {
	private static final String S_SERVER_VERSION = "[S] ServerVersion";
	public static final int VERSION = 0x0335;
	
	public S_ServerVersion() {
		int status_start_time	= (int) GameTimeClock.getInstance().getGameTime().getSeconds();
		status_start_time		= status_start_time - (status_start_time % 300);
		int game_real_time		= (int) (System.currentTimeMillis() / 1000L);
		long build_number		= Config.VERSION.CLIENT_VERSION;
		write_init();
		write_version_check(0);
		write_server_id(Config.VERSION.SERVER_NUMBER);
		write_build_number(build_number);
		write_cache_version(build_number);
		write_auth_version(Config.VERSION.AUTHSERVER_VERSION);
		write_npc_server_version(build_number);
		write_status_start_time(status_start_time);
		write_english_only_config(0);
		write_country_code(0);
		write_client_setting_switch(Config.VERSION.CLIENT_SETTING_SWITCH);
		write_game_real_time(game_real_time);
		write_global_cache_version(Config.VERSION.GLOBAL_CACHE_VERSION);
		write_tam_server_version(Config.VERSION.TAMSERVER_VERSION);
		write_arca_server_version(Config.VERSION.ARCASERVER_VERSION);
		write_hibreed_inter_server_version(Config.VERSION.HIBREED_INTERSERVER_VERSION);
		write_arenaco_server_version(Config.VERSION.ARENACOSERVER_VERSION);
		write_server_type(Config.VERSION.SERVER_TYPE);
		write_broker_server_version(Config.VERSION.BROKER_SERVER_VERSION);
		write_ai_agent_dll_version(Config.VERSION.AI_AGENT_DLL_VERSION);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(VERSION);
	}
	
	void write_version_check(int version_check) {
		writeRaw(0x08);// version_check
		writeRaw(version_check);
	}
	
	void write_server_id(int server_id) {
		writeRaw(0x10);// server_id
		writeBit(server_id);
	}
	
	void write_build_number(long build_number) {
		writeRaw(0x18);// build_number
		writeBit(build_number);
	}
	
	void write_cache_version(long cache_version) {
		writeRaw(0x20);// cache_version
		writeBit(cache_version);
	}
	
	void write_auth_version(long auth_version) {
		writeRaw(0x28);// auth_version
		writeBit(auth_version);
	}
	
	void write_npc_server_version(long npc_server_version) {
		writeRaw(0x30);// npc_server_version
		writeBit(npc_server_version);
	}
	
	void write_status_start_time(int status_start_time) {
		writeRaw(0x38);// status_start_time
		writeBit(status_start_time);
	}
	
	void write_english_only_config(int english_only_config) {
		writeRaw(0x40);// english_only_config
		writeRaw(english_only_config);
	}
	
	void write_country_code(int country_code) {
		writeRaw(0x48);// country_code
		writeRaw(country_code);
	}
	
	void write_client_setting_switch(long client_setting_switch) {
		writeRaw(0x50);// client_setting_switch
		writeBit(client_setting_switch);
	}
	
	void write_game_real_time(int game_real_time) {
		writeRaw(0x58);// game_real_time
		writeBit(game_real_time);
	}
	
	void write_global_cache_version(long global_cache_version) {
		writeRaw(0x60);// global_cache_version
		writeBit(global_cache_version);
	}
	
	void write_tam_server_version(long tam_server_version) {
		writeRaw(0x68);// tam_server_version
		writeBit(tam_server_version);
	}
	
	void write_arca_server_version(long arca_server_version) {
		writeRaw(0x70);// arca_server_version
		writeBit(arca_server_version);
	}
	
	void write_hibreed_inter_server_version(long hibreed_inter_server_version) {
		writeRaw(0x78);// hibreed_inter_server_version
		writeBit(hibreed_inter_server_version);
	}
	
	void write_arenaco_server_version(long arenaco_server_version) {
		writeH(0x0180);// arenaco_server_version
		writeBit(arenaco_server_version);
	}
	
	void write_server_type(int server_type) {
		writeH(0x0188);// server_type
		writeRaw(server_type);
	}
	
	void write_broker_server_version(long broker_server_version) {
		writeH(0x0190);// broker_server_version
		writeBit(broker_server_version);
	}
	
	void write_ai_agent_dll_version(long ai_agent_dll_version) {
		writeH(0x0198);// ai_agent_dll_version
		writeBit(ai_agent_dll_version);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return S_SERVER_VERSION;
	}
}
