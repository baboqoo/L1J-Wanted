package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;
import l1j.server.server.model.L1Clan;
import l1j.server.server.serverpackets.pledge.S_BloodPledgeStoreAllowInfo;

public class A_PledgeStoreAllowInfo extends ProtoHandler {
	protected A_PledgeStoreAllowInfo(){}
	private A_PledgeStoreAllowInfo(byte[] data, GameClient client) {
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
		_pc.sendPackets(new S_BloodPledgeStoreAllowInfo(pledge), true);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_PledgeStoreAllowInfo(data, client);
	}

}

