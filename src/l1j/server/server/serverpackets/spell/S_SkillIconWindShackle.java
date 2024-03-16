package l1j.server.server.serverpackets.spell;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_SkillIconWindShackle extends ServerBasePacket {

	public S_SkillIconWindShackle(int objectId, int time) {
		int buffTime = (time >> 2);
		writeC(Opcodes.S_EVENT);
		writeC(0x2c);
		writeD(objectId);
		writeH(buffTime);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}

