package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_Alignment extends ServerBasePacket {
	private static final String S_ALIGNMENT = "[S] S_Alignment";
	private byte[] _byte = null;

	public S_Alignment(int objid, int align) {
		writeC(Opcodes.S_CHANGE_ALIGNMENT);
		writeD(objid);
		writeH(align);
		writeD(0);
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
		return S_ALIGNMENT;
	}

}
