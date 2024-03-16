package l1j.server.server.serverpackets.indun;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_IndunInvite extends ServerBasePacket {
	private byte[] _byte = null;
	private static final String S_INDUN_INVITE = "[S] S_IndunInvite";
	public static final int INVITE 	= 0x02ca;

	public S_IndunInvite(int room_id, String host_char_name, String guest_char_name, boolean need_password, boolean is_indun) {
		write_init();
		write_room_id(room_id);
		write_host_char_name(host_char_name.getBytes());
		write_guest_char_name(guest_char_name.getBytes());
		write_need_password(need_password);
		write_is_indun(is_indun);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(INVITE);
	}
	
	void write_room_id(int room_id) {
		writeC(0x08);// room_id
		writeBit(room_id);
	}
	
	void write_host_char_name(byte[] host_char_name) {
		writeC(0x12);// host_char_name
		writeBytesWithLength(host_char_name);
	}
	
	void write_guest_char_name(byte[] guest_char_name) {
		writeC(0x1a);// guest_char_name
		writeBytesWithLength(guest_char_name);
	}
	
	void write_need_password(boolean need_password) {
		writeC(0x20);// need_password
		writeB(need_password);
	}
	
	void write_buildercaster_password(byte[] buildercaster_password) {
		writeC(0x2a);// buildercaster_password
		writeBytesWithLength(buildercaster_password);
	}
	
	void write_from_builder(boolean from_builder) {
		writeC(0x30);// from_builder
		writeB(from_builder);
	}
	
	void write_is_indun(boolean is_indun) {
		writeC(0x38);// is_indun
		writeB(is_indun);
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
		return S_INDUN_INVITE;
	}
}
