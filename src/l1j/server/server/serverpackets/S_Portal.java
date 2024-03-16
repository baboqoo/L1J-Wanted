package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_Portal extends ServerBasePacket {
	private static final String S_PORTAL = "[S] S_Portal";
	private byte[] _byte = null;

	public S_Portal(L1PcInstance pc) {
		writeC(Opcodes.S_PORTAL);
		writeH(0x00);
		writeD(0x00);
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
		return S_PORTAL;
	}
}

