package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_DisconnectSocket extends ServerBasePacket {
	private static final String S_DISCONNECT_SOCKET = "[S] S_DisconnectSocket";
	private byte[] _byte = null;
	public static final int DISCONNECTED_SOCKET	= 0x0083;
	
	public static final S_DisconnectSocket DISCONNECTED	= new S_DisconnectSocket();
	
	public S_DisconnectSocket() {
		write_init();
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(DISCONNECTED_SOCKET);
	}
	
	void write_is_empty(boolean is_empty) {
		writeRaw(0x08);
		writeB(is_empty);
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
		return S_DISCONNECT_SOCKET;
	}
}
