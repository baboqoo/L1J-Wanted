package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_MssNewKeywordNoti extends ServerBasePacket {
	private static final String S_MSS_NEW_KEYWORD_NOTI = "[S] S_MssNewKeywordNoti";
	private byte[] _byte = null;
	public static final int NOTI = 0x0a56;
	
	public S_MssNewKeywordNoti(boolean has_new) {
		write_init();
		write_has_new(has_new);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_has_new(boolean has_new) {
		writeRaw(0x08);// has_new
		writeB(has_new);
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
		return S_MSS_NEW_KEYWORD_NOTI;
	}
}

