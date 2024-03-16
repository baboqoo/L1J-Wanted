package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_Resurrection extends ServerBasePacket {

	public S_Resurrection(L1PcInstance target, L1PcInstance use, int type) {
		writeC(Opcodes.S_RESURRECT);
		writeD(target.getId());
		writeC(type);
		writeD(use.getId());
		writeD(target.getClassId());
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return S_RESURRECTION;
	}

	private static final String S_RESURRECTION = "[S] S_Resurrection";
}

