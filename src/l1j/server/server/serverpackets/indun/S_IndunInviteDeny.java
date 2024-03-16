package l1j.server.server.serverpackets.indun;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_IndunInviteDeny extends ServerBasePacket {
	private byte[] _byte = null;
	private static final String S_INDUN_INVITE_DENY = "[S] S_IndunInviteDeny";
	public static final int INVITE_DENY = 0x02cc;
	
	public S_IndunInviteDeny(int room_id, String host_char_name, String guest_char_name) {
		write_init();
		write_room_id(room_id);
		write_host_char_name(host_char_name.getBytes());
		write_guest_char_name(guest_char_name.getBytes());
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(INVITE_DENY);
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

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_INDUN_INVITE_DENY;
	}
}
