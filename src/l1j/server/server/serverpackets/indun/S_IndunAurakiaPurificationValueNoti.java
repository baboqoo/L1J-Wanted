package l1j.server.server.serverpackets.indun;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_IndunAurakiaPurificationValueNoti extends ServerBasePacket {
	private byte[] _byte = null;
	private static final String S_INDUN_AURAKIA_PURIFICATION_VALUE_NOTI = "[S] S_IndunAurakiaPurificationValueNoti";
	public static final int NOTI = 0x08d0;
	
	public S_IndunAurakiaPurificationValueNoti(int value) {
		write_init();
		write_value(value);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_value(int value) {
		writeRaw(0x08);// value
		writeB(value);
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
		return S_INDUN_AURAKIA_PURIFICATION_VALUE_NOTI;
	}
}
