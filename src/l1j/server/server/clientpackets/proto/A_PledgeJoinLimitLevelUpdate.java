package l1j.server.server.clientpackets.proto;

import l1j.server.common.data.eBloodPledgeRankType;
import l1j.server.server.GameClient;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.model.L1Clan;

public class A_PledgeJoinLimitLevelUpdate extends ProtoHandler {
	protected A_PledgeJoinLimitLevelUpdate(){}
	private A_PledgeJoinLimitLevelUpdate(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private int _limit_level = -1;
	
	void parse() {
		if (_total_length < 2) {
			return;
		}
		readP(1);// 0x08
		_limit_level = readC();
	}
	
	boolean isValidation() {
		return _limit_level >= 0;
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost() || _pc.getBloodPledgeRank() != eBloodPledgeRankType.RANK_NORMAL_KING) {
			return;
		}
		L1Clan pledge = _pc.getClan();
		if (pledge == null) {
			return;
		}
		parse();
		if (!isValidation()) {
			return;
		}
		pledge.set_limit_level(_limit_level);
		ClanTable.getInstance().updateLimitLevel(pledge);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_PledgeJoinLimitLevelUpdate(data, client);
	}

}

