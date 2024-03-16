package l1j.server.server.serverpackets.system;

import l1j.server.GameSystem.freebuffshield.FreeBuffShieldHandler;
import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_FreeBuffShieldUpdateNoti extends ServerBasePacket {
	private static final String S_FREE_BUFF_SHIELD_UPDATE_NOTI = "[S] S_FreeBuffShieldUpdateNoti";
	private byte[] _byte = null;
	public static final int NOTI	= 0x0ab8;
	
	public S_FreeBuffShieldUpdateNoti(FreeBuffShieldHandler handler) {
		write_init();
		if (handler.get_disable_state() != null) {
			write_disable_state(handler.get_disable_state());
		}
		if (handler.get_free_buff_shield_info() != null && !handler.get_free_buff_shield_info().isEmpty()) {
			write_free_buff_shield_info(handler.get_free_buff_shield_info());
		}
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_disable_state(DISABLE_FREE_BUFF_SHIELD disable_state) {
		disable_state.write_data();
		writeRaw(0x0a);
		writeBytesWithLength(disable_state.getBytes());
	}
	
	void write_free_buff_shield_info(java.util.LinkedList<FREE_BUFF_SHIELD_INFO> free_buff_shield_info) {
		for (FREE_BUFF_SHIELD_INFO val : free_buff_shield_info) {
			val.write_data();
			writeRaw(0x12);
			writeBytesWithLength(val.getBytes());
		}
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
		return S_FREE_BUFF_SHIELD_UPDATE_NOTI;
	}
}

