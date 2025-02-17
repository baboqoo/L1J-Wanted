package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;
import l1j.server.server.model.L1Clan;
import l1j.server.server.serverpackets.pledge.S_BloodPledgeJoinLimitInfo;

public class A_PledgeJoinLimitIInfo extends ProtoHandler {
	protected A_PledgeJoinLimitIInfo(){}
	private A_PledgeJoinLimitIInfo(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		L1Clan pledge = _pc.getClan();
		if (pledge == null) {
			return;
		}
		_pc.sendPackets(new S_BloodPledgeJoinLimitInfo(pledge), true);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_PledgeJoinLimitIInfo(data, client);
	}

}

