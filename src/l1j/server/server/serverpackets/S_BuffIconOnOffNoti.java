package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_BuffIconOnOffNoti extends ServerBasePacket {
	private static final String S_BUFF_ICON_ON_OFF_NOTI = "[S] S_BuffIconOnOffNoti";
	private byte[] _byte = null;
	public static final int NOTI = 0x021e;
	
	public S_BuffIconOnOffNoti(int iconid, boolean on){
		write_init();
		write_iconid(iconid);
		write_on(on);
        writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_iconid(int iconid) {
		writeRaw(0x08);
		writeBit(iconid);
	}
	
	void write_on(boolean on) {
		writeRaw(0x10);
		writeB(on);
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
		return S_BUFF_ICON_ON_OFF_NOTI;
	}
}

