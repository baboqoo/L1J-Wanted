
package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_HorunOK extends ServerBasePacket {
	public S_HorunOK(int type, L1PcInstance pc) {
		writeC(Opcodes.S_EXCHANGEABLE_SPELL_LIST);
		writeC(type);
		writeC(0);
		writeC(0);
		writeC(0);
		writeC(0);
		writeC(0);
		writeC(0);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return S_HORUN_OK;
	}

	private static final String S_HORUN_OK = "[S] S_HorunOK";
}

