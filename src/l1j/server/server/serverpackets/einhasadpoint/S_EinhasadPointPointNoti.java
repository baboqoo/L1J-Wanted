package l1j.server.server.serverpackets.einhasadpoint;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_EinhasadPointPointNoti extends ServerBasePacket {
	private static final String S_EINHASAD_POINT_POINT_NOTI = "[S] S_EinhasadPointPointNoti";
	private byte[] _byte = null;
	public static final int POINT	= 0x092b;
	
	public S_EinhasadPointPointNoti(int point) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(POINT);
		writeRaw(0x08);// point
		writeBit(point);
		writeH(0x00);
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
		return S_EINHASAD_POINT_POINT_NOTI;
	}
}

