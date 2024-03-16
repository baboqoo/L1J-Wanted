package l1j.server.server.clientpackets;

import l1j.server.server.GameClient;
import l1j.server.server.datatables.BuddyTable;
import l1j.server.server.model.Instance.L1PcInstance;

public class C_DelBuddy extends ClientBasePacket {

	private static final String C_DEL_BUDDY = "[C] C_DelBuddy";

	public C_DelBuddy(byte abyte0[], GameClient clientthread) {
		super(abyte0);
		L1PcInstance pc = clientthread.getActiveChar();
		if (pc == null) {
			return;
		}
		String charName = readS();
		BuddyTable.getInstance().removeBuddy(pc.getId(), charName);
		charName = null;
	}

	@Override
	public String getType() {
		return C_DEL_BUDDY;
	}

}

