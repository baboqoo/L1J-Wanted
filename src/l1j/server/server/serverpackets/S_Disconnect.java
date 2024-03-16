package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_Disconnect extends ServerBasePacket {
	public static final int CONNECT_MERGE = 22;
	
	public static final S_Disconnect DISCONNECT = new S_Disconnect();
	public static final S_Disconnect DISCONNECT_MERGE = new S_Disconnect(CONNECT_MERGE);
	
	public S_Disconnect() {
		writeC(Opcodes.S_KICK);
		writeH(500);
		writeD(0x00000000);
	}
	
	public S_Disconnect(int code) {
		writeC(Opcodes.S_KICK);
		writeD(code);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}

