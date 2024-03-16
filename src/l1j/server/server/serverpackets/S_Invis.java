package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_Invis extends ServerBasePacket {
	private byte[] _byte = null;

	public S_Invis(int objid, int type) {
		writeC(Opcodes.S_INVISIBLE);
		writeD(objid);
		writeC(type);
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
		return S_INVIS;
	}

	private static final String S_INVIS = "[S] S_Invis";
}

