package l1j.server.server.clientpackets.proto;

import l1j.server.common.data.eBloodPledgeRankType;
import l1j.server.server.GameClient;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.serverpackets.pledge.S_BloodPledgeJoinLimitUserAdd;
import l1j.server.server.utils.StringUtil;

public class A_PledgeJoinLimitIUserAdd extends ProtoHandler {
	protected A_PledgeJoinLimitIUserAdd(){}
	private A_PledgeJoinLimitIUserAdd(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private String _add_user_name;
	
	void parse() {
		if (_total_length <= 2) {
			return;
		}
		readP(1);// 0x0a
		int name_length	= readC();// size
		if (name_length <= 0) {
			return;
		}
		_add_user_name = readS(name_length);
	}
	
	boolean isValidation() {
		return !StringUtil.isNullOrEmpty(_add_user_name);
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
		if (!CharacterTable.getInstance().isContainNameList(_add_user_name)) {
			_pc.sendPackets(S_BloodPledgeJoinLimitUserAdd.eRESULT_NO_USER);
			return;
		}
		if (!pledge.add_limit_user_names(_add_user_name)) {
			_pc.sendPackets(S_BloodPledgeJoinLimitUserAdd.eRESULT_MAX_USER);
			return;
		}
		
		ClanTable.getInstance().updateLimitUserNames(pledge);
		_pc.sendPackets(S_BloodPledgeJoinLimitUserAdd.eRESULT_OK);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_PledgeJoinLimitIUserAdd(data, client);
	}

}

