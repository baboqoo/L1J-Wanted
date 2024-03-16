package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_Restart extends ServerBasePacket {
	private byte[] _byte = null;

	public S_Restart(int objid, int type) {
		writeC(Opcodes.S_RESTART);
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
		return S_RESTART;
	}

	private static final String S_RESTART = "[S] S_Restart";
}

