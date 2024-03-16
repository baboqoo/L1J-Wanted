package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_GMHtml extends ServerBasePacket {
	public S_GMHtml(int _objid, String html) {
		writeC(Opcodes.S_HYPERTEXT);
		writeD(_objid);
		writeS("hsiw");
		writeS(html);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}

