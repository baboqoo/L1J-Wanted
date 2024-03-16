package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_PingRequest extends ServerBasePacket {
	private static final String S_PING_REQUEST = "[S] S_PingRequest";
	private byte[] _byte = null;
	public static final int PING	= 0x03e8;

	public S_PingRequest(int objId){
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(PING);
		writeRaw(0x08);
		writeBit(objId);
        writeH(0x00);
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
		return S_PING_REQUEST;
	}
}

