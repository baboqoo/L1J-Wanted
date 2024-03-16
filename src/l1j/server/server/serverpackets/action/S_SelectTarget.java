package l1j.server.server.serverpackets.action;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_SelectTarget extends ServerBasePacket {
	private static final String S_SELECT_TARGET = "[S] S_SelectTarget";
	private byte[] _byte = null;

	public S_SelectTarget(int ObjectId) {
		writeC(Opcodes.S_SLAVE_CONTROL);
		writeD(ObjectId);
		writeC(0x00);
		writeC(0x00);
		writeC(0x02);
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
		return S_SELECT_TARGET;
	}
}

