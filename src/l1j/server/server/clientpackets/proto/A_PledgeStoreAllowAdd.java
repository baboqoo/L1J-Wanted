package l1j.server.server.clientpackets.proto;

import l1j.server.common.data.eBloodPledgeRankType;
import l1j.server.server.GameClient;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.L1Clan.ClanMember;
import l1j.server.server.serverpackets.pledge.S_BloodPledgeStoreAllowAdd;
import l1j.server.server.serverpackets.pledge.S_BloodPledgeStoreAllowInfoNoti;
import l1j.server.server.utils.StringUtil;

public class A_PledgeStoreAllowAdd extends ProtoHandler {
	protected A_PledgeStoreAllowAdd(){}
	private A_PledgeStoreAllowAdd(byte[] data, GameClient client) {
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
		ClanMember member = pledge.getClanMember(_add_user_name);
		if (member == null) {
			_pc.sendPackets(S_BloodPledgeStoreAllowAdd.eRESULT_NO_USER);
			return;
		}
		if (!pledge.add_store_allow_list(_add_user_name)) {
			_pc.sendPackets(S_BloodPledgeStoreAllowAdd.eRESULT_MAX_USER);
			return;
		}
		L1PcInstance user = member.player;
		if (user != null) {
			user.sendPackets(S_BloodPledgeStoreAllowInfoNoti.ALLOW);
		}
		_pc.sendPackets(S_BloodPledgeStoreAllowAdd.eRESULT_OK);
		ClanTable.getInstance().updateStoreAllow(pledge);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_PledgeStoreAllowAdd(data, client);
	}

}

