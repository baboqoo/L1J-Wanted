package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_ShowSummonList extends ServerBasePacket {
	private static final String S_ShowSummonList = "[S] S_ShowSummonList";

	public S_ShowSummonList(int objid) {
		writeC(Opcodes.S_HYPERTEXT);
		writeD(objid);
		writeS("summonlist");
	}

	@Override
	public byte[] getContent() {
		return _bao.toByteArray();
	}
	@Override
	public String getType() {
		return S_ShowSummonList;
	}
}

