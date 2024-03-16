package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_TamPointNoti extends ServerBasePacket {
	private static final String S_TAM_POINT_NOTI = "S_TamPointNoti";
	private byte[] _byte = null;
	public static final int POINT	= 0x01c2;
	
	public S_TamPointNoti(int tam_point) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(POINT);
		writeRaw(0x08);
		write4bit(tam_point);
		writeH(0x00);
	}
	
	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	public String getType() {
		return S_TAM_POINT_NOTI;
	}
}
