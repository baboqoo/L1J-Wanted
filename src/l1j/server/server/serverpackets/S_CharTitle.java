package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_CharTitle extends ServerBasePacket {

	public S_CharTitle(int objid, String title) {
		writeC(Opcodes.S_TITLE);
		writeD(objid);
		writeS(title);
		writeC(0x00);
		writeH(0x00);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return S_CHAR_TITLE;
	}

	private static final String S_CHAR_TITLE = "[S] S_CharTitle";
}

