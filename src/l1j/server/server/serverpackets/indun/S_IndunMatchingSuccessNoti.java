package l1j.server.server.serverpackets.indun;

import l1j.server.common.data.eArenaMapKind;
import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_IndunMatchingSuccessNoti extends ServerBasePacket {
	private byte[] _byte = null;
	private static final String S_INDUN_MATCHING_SUCCESS_NOTI = "[S] S_IndunMatchingSuccessNoti";
	public static final int NOTI 	= 0x08aa;
	
	public S_IndunMatchingSuccessNoti(boolean is_owner, int room_id, eArenaMapKind mapkind) {
		write_init();
		write_is_owner(is_owner);
		write_room_id(room_id);
		write_mapkind(mapkind);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_is_owner(boolean is_owner) {
		writeRaw(0x08);// is_owner
		writeB(is_owner);
	}
	
	void write_room_id(int room_id) {
		writeRaw(0x10);// room_id
		writeBit(room_id);
	}
	
	void write_mapkind(eArenaMapKind mapkind) {
		writeRaw(0x18);// mapkind
		writeBit(mapkind.toInt());
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
		return S_INDUN_MATCHING_SUCCESS_NOTI;
	}
}
