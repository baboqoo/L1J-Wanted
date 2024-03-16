
package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_Horun extends ServerBasePacket {
	public S_Horun(int o, L1PcInstance pc) {
		writeC(Opcodes.S_EXCHANGEABLE_SPELL_LIST);
		writeC(0x08);
		writeC(0);
		writeD(0x00);
		writeD(0x01);
		writeD(0x02);
		writeD(0x03);
		writeD(0x04);
		writeD(0x05);
		writeD(0x06);
		writeD(0x07);
		writeC(0);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return S_HORUN;
	}

	private static final String S_HORUN = "[S] S_Horun";
}

