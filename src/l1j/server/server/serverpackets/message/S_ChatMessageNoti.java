package l1j.server.server.serverpackets.message;

import l1j.server.Config;
import l1j.server.common.data.ChatType;
import l1j.server.common.data.ePolymorphAnonymityType;
import l1j.server.server.Opcodes;
import l1j.server.server.construct.L1InterServer;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_ChatMessageNoti extends ServerBasePacket {
	private static final String S_CHAT_MESSAGE_NOTI = "[S] S_ChatMessageNoti";
	private byte[] _byte = null;
	public static final int NOTI	= 0x0204;
	private static final byte[] GM_BYTES	= "******".getBytes();
	
	byte[] get_name_byte(L1PcInstance pc, ChatType type) {
		if (pc.isGm() && type == ChatType.CHAT_WORLD) {
			return GM_BYTES;// 운영자 전체 채팅
		}
		if (!pc.isGm() && type == ChatType.CHAT_NORMAL && L1InterServer.isAnonymityInter(pc.getMap().getInter())) {
			return pc.getConfig().getAnonymityName().getBytes();// 익명 채팅
		}
		return pc.getName().getBytes();
	}
	
	public S_ChatMessageNoti(L1PcInstance pc, ChatType type, byte[] message, byte[] link_message, int server_id) {
		write_init();
		write_time_t64();
		write_type(type);
		write_message(message);
		write_name(get_name_byte(pc, type));
		write_server_no(Config.VERSION.SERVER_NUMBER);
		if (type == ChatType.CHAT_NORMAL) {// 일반 채팅
			write_object_id(pc.getId());
			write_loc_x(pc.getX());
			write_loc_y(pc.getY());
		}
		if (pc.getConfig().get_ranker_rating() > 0) {
			write_ranker_rating(pc.getConfig().get_ranker_rating());
		}
		write_is_server_keeper(pc.isGm());
		if (pc.getConfig().getAnonymityType() != null) {
			write_anonymity_type(pc.getConfig().getAnonymityType());
			write_anonymity_name(pc.getConfig().getAnonymityName());
		}
		int class_rank = pc.getConfig().get_class_rank();
		if (class_rank == 1) {// 클래스 전체 랭킹 1위
			write_game_class(pc.getType());
		}
		if (link_message != null) {
			write_link_message(link_message);
		}
		if (class_rank == 1) {// 클래스 전체 랭킹 1위
	    	write_total_class_ranker_rating(1);
		}
		if (server_id > 0) {
			write_server_id(server_id);
		}
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_time_t64() {
		writeRaw(0x08);// time_t64
		writeBit(System.currentTimeMillis() / 1000L);
	}
	
	void write_type(ChatType type) {
		writeRaw(0x10);// type
		writeRaw(type.toInt());  
	}
	
	void write_message(byte[] message) {
		writeRaw(0x1a);// message
		writeBytesWithLength(message); 
	}
	
	void write_name(byte[] name) {
		writeRaw(0x2a);// name
		writeBytesWithLength(name);
	}
	
	void write_server_no(int server_no) {
		writeRaw(0x30);// server_no
		writeBit(server_no);
	}
	
	void write_object_id(int object_id) {
		writeRaw(0x38);// object_id
		writeK(object_id);
	}
	
	void write_loc_x(int loc_x) {
		writeRaw(0x40);// loc_x
		writeK(loc_x);
	}
	
	void write_loc_y(int loc_y) {
		writeRaw(0x48);// loc_y
		writeK(loc_y);
	}
	
	void write_ranker_rating(int ranker_rating) {
		writeRaw(0x50);// ranker_rating
		writeRaw(ranker_rating);
	}
	
	void write_target_user_name(String target_user_name) {
		writeRaw(0x5a);// target_user_name
		writeBytesWithLength(target_user_name.getBytes());
	}
	
	void write_is_server_keeper(boolean is_server_keeper) {
		writeRaw(0x60);// is_server_keeper
	    writeB(is_server_keeper);
	}
	
	void write_anonymity_type(ePolymorphAnonymityType anonymity_type) {
		writeRaw(0x68);// anonymity_type
		writeRaw(anonymity_type.toInt());
	}
	
	void write_anonymity_name(String anonymity_name) {
		writeRaw(0x70);// anonymity_name
		writeStringWithLength(anonymity_name);
	}
	
	void write_game_class(int game_class) {
		writeRaw(0x78);// game_class
		writeRaw(game_class);
	}
	
	void write_link_message(byte[] link_message) {
		writeH(0x0182);// link_message
		writeBytesWithLength(link_message);
	}
	
	void write_total_class_ranker_rating(int total_class_ranker_rating) {
		writeH(0x0188);// total_class_ranker_rating
		writeRaw(total_class_ranker_rating);
	}
	
	void write_team_id(int team_id) {
		writeH(0x0190);// team_id
		writeRaw(team_id);
	}
	
	void write_hide_chat_balloon(boolean hide_chat_balloon) {
		writeH(0x0198);// hide_chat_balloon
		writeB(hide_chat_balloon);
	}
	
	void write_server_id(int server_id) {
		writeH(0x01a0);// server_id
		writeBit(server_id);
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = _bao.toByteArray();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_CHAT_MESSAGE_NOTI;
	}
}
