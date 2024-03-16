package l1j.server.server.serverpackets.polymorph;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_PolymorphEvent extends ServerBasePacket {
	private static final String S_POLYMORPH_EVENT = "[S] S_PolymorphEvent";
	private byte[] _byte = null;
	public static final int EVENT	= 0x01d0;
	
	public static final S_PolymorphEvent POLY_EVENT_ON	= new S_PolymorphEvent(true);
	public static final S_PolymorphEvent POLY_EVENT_OFF	= new S_PolymorphEvent(false);
	
	public S_PolymorphEvent(boolean eventEnable) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(EVENT);
		writeRaw(0x08);
		writeB(eventEnable);
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
		return S_POLYMORPH_EVENT;
	}
}

