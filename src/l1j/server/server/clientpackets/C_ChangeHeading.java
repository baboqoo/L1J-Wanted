package l1j.server.server.clientpackets;

import l1j.server.server.GameClient;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.action.S_ChangeHeading;

public class C_ChangeHeading extends ClientBasePacket {
	private static final String C_CHANGE_HEADING = "[C] C_ChangeHeading";

	public C_ChangeHeading(byte[] decrypt, GameClient client) {
		super(decrypt);
		L1PcInstance pc = client.getActiveChar();
		if (pc == null) {
			return;
		}
		int heading = readC();
		if (heading < 0 || heading > 7 || heading == pc.getMoveState().getHeading()) {
			return;
		}
		pc.getMoveState().setHeading(heading);
		if (!pc.isGmInvis() && !pc.isGhost() && !pc.isInvisble()) {
			pc.broadcastPacket(new S_ChangeHeading(pc), true);
		}
	}

	@Override
	public String getType() {
		return C_CHANGE_HEADING;
	}
}
