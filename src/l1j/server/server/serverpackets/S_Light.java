package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_Light extends ServerBasePacket {
	private static final String S_LIGHT = "[S] S_Light";
	private byte[] _byte = null;

	public S_Light(int objid, int type) {
		writeC(Opcodes.S_CHANGE_LIGHT);
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
		return S_LIGHT;
	}
}

