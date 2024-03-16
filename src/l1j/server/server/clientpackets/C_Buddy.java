package l1j.server.server.clientpackets;

import l1j.server.server.GameClient;
import l1j.server.server.datatables.BuddyTable;
import l1j.server.server.model.L1Buddy;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Buddy;

public class C_Buddy extends ClientBasePacket {

	private static final String C_BUDDY = "[C] C_Buddy";

	public C_Buddy(byte abyte0[], GameClient client) {
		super(abyte0);
		if (client == null) {
			return;
		}
		L1PcInstance pc = client.getActiveChar();
		if (pc == null) {
			return;
		}
		L1Buddy buddy = BuddyTable.getInstance().getBuddyTable(pc.getId());
		pc.sendPackets(new S_Buddy(buddy, pc), true);
	}

	@Override
	public String getType() {
		return C_BUDDY;
	}

}

