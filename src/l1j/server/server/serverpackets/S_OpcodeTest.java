package l1j.server.server.serverpackets;

import l1j.server.server.model.Instance.L1PcInstance;

public class S_OpcodeTest extends ServerBasePacket {
	private static final String S_OPCODE_TEST = "[C]  S_OpcodeTest";

	public S_OpcodeTest(L1PcInstance pc, int opcode, int type) {
		writeC(opcode);
		writeD(type == 0 ? pc.getId() : 1);
	}
	
	public S_OpcodeTest(int opcode, int objid, int value) {
		writeC(opcode);
		writeD(objid);
		writeC(value);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return S_OPCODE_TEST;
	}
}
