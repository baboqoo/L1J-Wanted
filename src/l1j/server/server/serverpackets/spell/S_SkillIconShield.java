package l1j.server.server.serverpackets.spell;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_SkillIconShield extends ServerBasePacket {
	
	public static final int SHIELD		= 1;
	public static final int FIRE_SHILD	= 4;
	public static final int IRON_SKIN	= 10;

	public S_SkillIconShield(int type, int time) {
		writeC(Opcodes.S_MAGE_SHIELD);
		writeH(time);
		writeC(type);
		writeD(0);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}

