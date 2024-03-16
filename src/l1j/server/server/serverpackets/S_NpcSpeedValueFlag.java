package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_NpcSpeedValueFlag extends ServerBasePacket {
	private static final String S_NPC_SPPED_VALUE_FLAG = "[S] S_NpcSpeedValueFlag";
	private byte[] _byte = null;
	public static final int FLAG	= 0x0246;

	public S_NpcSpeedValueFlag(int object_id, int speed_value_flag, int second_speed_type) {
		write_init();
		write_object_id(object_id);
		write_speed_value_flag(speed_value_flag);
		write_second_speed_type(second_speed_type);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(FLAG);
	}
	
	void write_object_id(int object_id) {
		writeRaw(0x08);// object_id
		writeBit(object_id);
	}
	
	void write_speed_value_flag(int speed_value_flag) {
		writeRaw(0x10);// speed_value_flag
		writeBit(speed_value_flag);
	}
	
	void write_second_speed_type(int second_speed_type) {
		writeRaw(0x18);// second_speed_type
		writeRaw(second_speed_type);
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
		return S_NPC_SPPED_VALUE_FLAG;
	}
}

