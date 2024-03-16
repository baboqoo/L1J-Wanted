package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_HypertextProto extends ServerBasePacket {
	private static final String S_HYPER_TEXT_PROTO = "[S] S_HypertextProto";
	private byte[] _byte = null;
	
	public static final int TEXT	= 0x033a;

	public S_HypertextProto(String url, int npcId, int parameter, String text, String cookie){
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(TEXT);
		
		writeC(0x0a);
		writeBytesWithLength(url.getBytes());
		
		writeC(0x10);
		writeBit(npcId);
		
		writeC(0x18);
		writeC(parameter);
		
		writeC(0x22);
		writeBytesWithLength(text.getBytes());
		
		writeC(0x2a);
		writeBytesWithLength(cookie.getBytes());
		
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
		return S_HYPER_TEXT_PROTO;
	}
}

