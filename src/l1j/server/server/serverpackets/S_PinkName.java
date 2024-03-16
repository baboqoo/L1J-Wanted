package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_PinkName extends ServerBasePacket {

	public S_PinkName(int objecId, int time) {
		writeC(Opcodes.S_CRIMINAL);
		writeD(objecId);
		writeC(time);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return S_PINK_NAME;
	}

	private static final String S_PINK_NAME = "[S] S_PinkName";
}

