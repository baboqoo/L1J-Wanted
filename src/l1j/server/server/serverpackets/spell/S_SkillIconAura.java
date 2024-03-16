package l1j.server.server.serverpackets.spell;

import l1j.server.server.Opcodes;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_SkillIconAura extends ServerBasePacket {
	public static final S_SkillIconAura DANCING_BLADES_ICON_OFF = new S_SkillIconAura(L1SkillId.DANCING_BLADES, 0);

	public S_SkillIconAura(int i, int j) {
		writeC(Opcodes.S_EVENT);
		writeC(0x16);
		writeC(i);
		writeH(j);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}

