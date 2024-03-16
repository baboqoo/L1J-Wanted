package l1j.server.server.clientpackets;

import l1j.server.server.GameClient;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.pledge.S_Emblem;

public class C_Emblem extends ClientBasePacket {
	private static final String C_EMBLEM = "[C] C_Emblem";

	public C_Emblem(byte abyte0[], GameClient clientthread) {
		super(abyte0);
		L1PcInstance pc = clientthread.getActiveChar();
		if (pc == null) {
			return;
		}
		int emblemId	= readD();
		int numId		= readD();
		if (emblemId == 0) {
			return;
		}
		pc.sendPackets(new S_Emblem(emblemId, numId), true);
	}

	@Override
	public String getType() {
		return C_EMBLEM;
	}
}
