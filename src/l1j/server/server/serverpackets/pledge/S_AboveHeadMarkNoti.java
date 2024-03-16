package l1j.server.server.serverpackets.pledge;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_AboveHeadMarkNoti extends ServerBasePacket {
	private static final String S_ABOVE_HEAD_MARK_NOTI = "[S] S_AboveHeadMarkNoti";
	private byte[] _byte = null;
	
	public S_AboveHeadMarkNoti(int code, int user_id, int mark_id) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(code);
		write_user_id(user_id);
		write_mark_id(mark_id);
		writeH(0x00);
	}
	
	void write_user_id(int user_id) {
		writeRaw(0x08);
		writeBit(user_id);
	}
	
	void write_mark_id(int mark_id) {
		writeRaw(0x10);
		writeBit(mark_id);
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
		return S_ABOVE_HEAD_MARK_NOTI;
	}
}

