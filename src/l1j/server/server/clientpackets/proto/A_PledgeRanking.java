package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;
import l1j.server.server.model.L1ClanRanking;
import l1j.server.server.model.L1ClanRanking.RankData;
import l1j.server.server.serverpackets.S_RankingClan;

public class A_PledgeRanking extends ProtoHandler {
	protected A_PledgeRanking(){}
	private A_PledgeRanking(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		RankData[] allDatas = L1ClanRanking.getInstance().getRankerDatas();
		_pc.sendPackets(new S_RankingClan(allDatas), true);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_PledgeRanking(data, client);
	}

}

