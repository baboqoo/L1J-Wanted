package l1j.server.server.serverpackets.action;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_FlyingAttackNoti extends ServerBasePacket {
	private static final String S_FLYING_ATTACK_NOTI = "[S] S_FlyingAttackNoti";
	private byte[] _byte = null;

	public S_FlyingAttackNoti(int code, int location) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(code);
		write_location(location);
		writeH(0x00);
	}
	
	void write_location(int location) {
		writeRaw(0x08);
		writeBit(location);
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
		return S_FLYING_ATTACK_NOTI;
	}
}

