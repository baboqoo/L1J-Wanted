package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_SkillHaste extends ServerBasePacket {

	public S_SkillHaste(int i, int j, int k) {
		writeC(Opcodes.S_SPEED);
		writeD(i);
		writeC(j);
		writeH(k);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}

