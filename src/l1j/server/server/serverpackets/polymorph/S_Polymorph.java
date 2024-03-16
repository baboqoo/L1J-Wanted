package l1j.server.server.serverpackets.polymorph;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_Polymorph extends ServerBasePacket {
	private static final String S_POLYMORPH = "[S] S_Polymorph";
	private byte[] _byte = null;
	public static final int POLY	= 0x040d;

	public S_Polymorph(int objId, int sprite, int action) {
		write(objId, sprite, action, null, 0, 0, 0);
	}
	public S_Polymorph(int objId, int sprite, int action, String desc, int class_id) {
		write(objId, sprite, action, desc, class_id, 0, 0);
	}
	public S_Polymorph(int objId, int sprite, int action, String desc, int class_id, int fix_frame_level) {
		write(objId, sprite, action, desc, class_id, fix_frame_level, 0);
	}
	public S_Polymorph(int objId, int sprite, int action, String desc, int class_id, int fix_frame_level, int effect_id) {
		write(objId, sprite, action, desc, class_id, fix_frame_level, effect_id);
	}
	
	void write(int objId, int sprite, int action, String desc, int class_id, int fix_frame_level, int effect_id) {
		write_init();
		write_objId(objId);
		write_sprite(sprite);
		write_action(action);
		if (desc != null) {
			write_desc(desc.getBytes());
		}
		if (class_id > 0) {
			write_class_id(class_id);
		}
		if (fix_frame_level > 0) {
			write_fix_frame_level(fix_frame_level);
		}
		if (effect_id > 0) {
			write_effect_id(effect_id);
		}
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(POLY);
	}
	
	void write_objId(int objId) {
		writeRaw(0x08);
		writeBit(objId);
	}
	
	void write_sprite(int sprite) {
		writeRaw(0x10);
		writeBit(sprite);
	}
	
	void write_action(int action) {
		writeRaw(0x18);
		writeBit(action);
	}
	
	void write_desc(byte[] desc) {
		writeRaw(0x22);
		writeBytesWithLength(desc);
	}
	
	void write_class_id(int class_id) {
		writeRaw(0x28);
		writeBit(class_id);
	}
	
	void write_fix_frame_level(int fix_frame_level) {
		writeRaw(0x30);
		writeBit(fix_frame_level);
	}
	
	void write_effect_id(int effect_id) {
		writeRaw(0x38);
		writeBit(effect_id);
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
		return S_POLYMORPH;
	}
}

