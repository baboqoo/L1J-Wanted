package l1j.server.server.clientpackets.proto;

import l1j.server.Config;
import l1j.server.server.GameClient;
import l1j.server.server.model.L1Clan;
import l1j.server.server.serverpackets.pledge.S_BloodPledgeContribution;

public class A_PledgeContribution extends ProtoHandler {
	protected A_PledgeContribution(){}
	private A_PledgeContribution(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		L1Clan clan = _pc.getClan();
		if (clan == null || clan.getClanId() == Config.PLEDGE.BEGINNER_PLEDGE_ID || clan.isBot()) {
			return;
		}
		_pc.sendPackets(new S_BloodPledgeContribution(clan.getContribution()), true);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_PledgeContribution(data, client);
	}

}

