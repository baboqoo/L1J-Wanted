package l1j.server.server.serverpackets.inventory;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_NotiToggleInfo extends ServerBasePacket {
	private static final String S_NOTI_TOGGLE_INFO = "[S] S_NotiToggleInfo";
	private byte[] _byte = null;
	public static final int NOTI = 0x0a03;
	
	public static final S_NotiToggleInfo DEFAULT_FAITH_OF_HALPAH = new S_NotiToggleInfo(eToggleInfoType.TOGGLE_INFO_FAITH_OF_HALPAH_USABLE_TYPE, false, false);
	
	public S_NotiToggleInfo(eToggleInfoType toggle_info_type, boolean is_enable, boolean by_user){
		write_init();
		write_toggle_info_type(toggle_info_type);
		write_is_enable(is_enable);
		write_by_user(by_user);
        writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_toggle_info_type(eToggleInfoType toggle_info_type) {
		writeC(0x08);
		writeC(toggle_info_type.toInt());
	}
	
	void write_is_enable(boolean is_enable) {
		writeC(0x10);
		writeB(is_enable);
	}
	
	void write_by_user(boolean by_user) {
		writeC(0x18);
		writeB(by_user);
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
		return S_NOTI_TOGGLE_INFO;
	}
}

