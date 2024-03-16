package l1j.server.server.serverpackets.spell;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_SkillIconBlessOfEva extends ServerBasePacket {

	public S_SkillIconBlessOfEva(int objectId, int time) {
		writeC(Opcodes.S_BREATH);
		writeD(objectId);
		writeH(time);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}

