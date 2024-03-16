package l1j.server.server.clientpackets.proto;

import java.util.Collection;

import l1j.server.server.GameClient;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.serverpackets.pledge.S_BloodPledgeList;

public class A_PledgeList extends ProtoHandler {
	protected A_PledgeList(){}
	private A_PledgeList(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		Collection<L1Clan> clanList = L1World.getInstance().getAllClans();
		if (clanList == null || clanList.isEmpty()) {
			return;
		}
		_pc.sendPackets(new S_BloodPledgeList(clanList), true);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_PledgeList(data, client);
	}

}

