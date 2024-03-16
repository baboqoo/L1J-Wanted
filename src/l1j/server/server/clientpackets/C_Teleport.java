package l1j.server.server.clientpackets;

import l1j.server.Config;
import l1j.server.server.GameClient;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1PcInstance;

public class C_Teleport extends ClientBasePacket {
	private static final String C_TELEPORT = "[C] C_Teleport";

	public C_Teleport(byte abyte0[], GameClient client) throws Exception {
		super(abyte0);
		final L1PcInstance pc = client.getActiveChar();
		if (pc == null) {
			return;
		}
		Runnable r = () -> {
			pc.getTeleport().end(false);
		};
		GeneralThreadPool.getInstance().schedule(r, Config.SPEED.TELEPORT_DELAY_SYNCHRONIZED);
	}

	@Override
	public String getType() {
		return C_TELEPORT;
	}
}
