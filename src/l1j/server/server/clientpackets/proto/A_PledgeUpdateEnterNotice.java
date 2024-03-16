package l1j.server.server.clientpackets.proto;

import l1j.server.common.data.eBloodPledgeRankType;
import l1j.server.server.GameClient;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.utils.StringUtil;

public class A_PledgeUpdateEnterNotice extends ProtoHandler {
	protected A_PledgeUpdateEnterNotice(){}
	private A_PledgeUpdateEnterNotice(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private String _enter_notice = StringUtil.EmptyString;
	
	void parse() {
		if (_total_length <= 2) {
			return;
		}
		readP(1);// 0x0a
		int notice_length	= readC();// size
		if (notice_length <= 0) {
			return;
		}
		_enter_notice = readS(notice_length);
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
		pledge.setEnterNotice(_enter_notice);
		ClanTable.getInstance().updateEnterNotice(pledge);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_PledgeUpdateEnterNotice(data, client);
	}

}

