package l1j.server.server.serverpackets.action;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_DamageTime extends ServerBasePacket {
	private static final String S_DAMAGE_TIME = "[S] S_DamageTime";
	private byte[] _byte = null;
	public static final int DAMAGE	= 0x0193;
	
	public S_DamageTime(int attackerId, int targetId) {
		write_init();
		write_targetId(targetId);
		write_attackerId(attackerId);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(DAMAGE);
	}
	
	void write_targetId(int targetId) {
		writeC(0x08);
		writeBit(targetId);
	}
	
	void write_attackerId(int attackerId) {
		writeC(0x10);
		writeBit(attackerId);
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
		return S_DAMAGE_TIME;
	}
}

