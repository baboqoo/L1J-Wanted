package l1j.server.server.clientpackets;

import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Party;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.party.S_Party;

public class C_Party extends ClientBasePacket {

	private static final String C_PARTY = "[C] C_Party";

	public C_Party(byte abyte0[], GameClient client) {
		super(abyte0);
		L1PcInstance pc = client.getActiveChar();
		if (pc == null || pc.isGhost()) {
			return;
		}
		if (!pc.isInParty()) {
			pc.sendPackets(L1ServerMessage.sm425);
			return;
		}
		L1Party party = pc.getParty();
		pc.sendPackets(new S_Party("party", pc.getId(), party.getLeader().getName(), party.getMembersNameList()), true);
	}

	@Override
	public String getType() {
		return C_PARTY;
	}

}

