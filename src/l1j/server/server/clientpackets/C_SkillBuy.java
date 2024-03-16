package l1j.server.server.clientpackets;

import l1j.server.server.GameClient;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SkillBuy;

public class C_SkillBuy extends ClientBasePacket {
	private static final String C_SKILL_BUY = "[C] C_SkillBuy";

	public C_SkillBuy(byte abyte0[], GameClient clientthread) throws Exception {
		super(abyte0);
		L1PcInstance pc = clientthread.getActiveChar();
		if (pc == null || pc.isGhost()) {
			return;
		}
		int i = readD();
		pc.sendPackets(new S_SkillBuy(i, pc), true);
	}

	@Override
	public String getType() {
		return C_SKILL_BUY;
	}

}

