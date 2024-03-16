package l1j.server.server.serverpackets.indun;

import l1j.server.common.data.eArenaMapKind;
import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_IndunQuickStart extends ServerBasePacket {
	private byte[] _byte = null;
	private static final String S_INDUN_QUICK_START = "[S] S_IndunQuickStart";
	public static final int QUICK_START = 0x08ca;
	
	public S_IndunQuickStart(boolean result, int room_id, eArenaMapKind mapkind) {
		write_init();
		write_result(result);
		write_is_owner(result);
		write_room_id(room_id);
		write_mapkind(mapkind);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(QUICK_START);
	}
	
	void write_result(boolean result) {
		writeRaw(0x08);// result
		writeB(result);
	}
	
	void write_is_owner(boolean is_owner) {
		writeRaw(0x10);// is_owner
		writeB(is_owner);
	}
	
	void write_room_id(int room_id) {
		writeRaw(0x18);// room_id
		writeBit(room_id);
	}
	
	void write_mapkind(eArenaMapKind mapkind) {
		writeRaw(0x20);// mapkind
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
		return S_INDUN_QUICK_START;
	}
}
