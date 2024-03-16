package l1j.server.server.serverpackets.action;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_FlyingAttackMove extends ServerBasePacket {
	private static final String S_FLYING_ATTACK_MOVE = "[S] S_FlyingAttackMove";
	private byte[] _byte = null;

	public S_FlyingAttackMove(int code, int object_id, int range, int start_loc, int end_loc) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(code);
		write_object_id(object_id);
		write_range(range);
		write_start_loc(start_loc);
		write_end_loc(end_loc);
		writeH(0x00);
	}
	
	void write_object_id(int object_id) {
		writeRaw(0x08);
		writeBit(object_id);
	}
	
	void write_range(int range) {
		writeRaw(0x10);
		writeRaw(range);
	}
	
	void write_start_loc(int start_loc) {
		writeRaw(0x18);
		writeBit(start_loc);
	}
	
	void write_end_loc(int end_loc) {
		writeRaw(0x20);
		writeBit(end_loc);
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
		return S_FLYING_ATTACK_MOVE;
	}
}

