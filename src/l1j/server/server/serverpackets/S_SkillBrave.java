package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_SkillBrave extends ServerBasePacket {

	public S_SkillBrave(int i, int j, int k) {
		writeC(Opcodes.S_EMOTION);
		writeD(i);
		writeC(j);
		writeH(k);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}

