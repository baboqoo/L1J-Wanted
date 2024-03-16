package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Object;

public class S_ObjectExplosion extends ServerBasePacket {
	private static final String S_OBJECT_EXPLOSION = "[S] S_ObjectExplosion";
	private byte[] _byte = null;
	private static final int OBJECT_EXPLOSION = 0x0332;
	
	public S_ObjectExplosion(L1Object obj, long remain_tims_ms) {
		write_init();
		write_obj_id(obj.getId());
		write_remain_tims_ms(remain_tims_ms);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(OBJECT_EXPLOSION);
	}
	
	void write_obj_id(int obj_id) {
		writeRaw(0x08);// obj_id
		writeBit(obj_id);
	}
	
	void write_remain_tims_ms(long remain_tims_ms) {
		writeRaw(0x10);// remain_tims_ms
		writeBit(remain_tims_ms);
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
		return S_OBJECT_EXPLOSION;
	}
}

