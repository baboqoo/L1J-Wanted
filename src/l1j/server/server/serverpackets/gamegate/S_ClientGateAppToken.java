package l1j.server.server.serverpackets.gamegate;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_ClientGateAppToken extends ServerBasePacket {
	private static final String S_CLIENTGATE_APP_TOKEN = "[S] S_ClientGateAppToken";
	private byte[] _byte = null;
	public static final int TOKEN	= 0x0070;
	
	public S_ClientGateAppToken(byte[] token){// 암호화된 게정 정보
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(TOKEN);
		writeRaw(0x0a);
		writeBytesWithLength(token);
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
		return S_CLIENTGATE_APP_TOKEN;
	}
}

