package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.utils.StringUtil;

public class S_CloseList extends ServerBasePacket {
	public S_CloseList(int objid) {
		writeC(Opcodes.S_HYPERTEXT);
		writeD(objid);
		writeS(StringUtil.EmptyString);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}

