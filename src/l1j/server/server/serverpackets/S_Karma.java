package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_Karma extends ServerBasePacket {
	private static final String S_KARMA = "[S] S_Karma";
	private byte[] _byte = null;
	
	public S_Karma(L1PcInstance pc) {
		writeC(Opcodes.S_EVENT);
	    writeC(0x57);
	    writeD(pc.getKarma());
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = _bao.toByteArray();
		}
		return _byte;
	}
	
	@Override
	public String getType() {
		return S_KARMA;
	}
}

