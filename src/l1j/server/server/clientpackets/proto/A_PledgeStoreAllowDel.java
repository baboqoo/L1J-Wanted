package l1j.server.server.clientpackets.proto;

import l1j.server.common.data.eBloodPledgeRankType;
import l1j.server.server.GameClient;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Clan.ClanMember;
import l1j.server.server.serverpackets.pledge.S_BloodPledgeStoreAllowInfoNoti;

public class A_PledgeStoreAllowDel extends ProtoHandler {
	protected A_PledgeStoreAllowDel(){}
	private A_PledgeStoreAllowDel(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private java.util.LinkedList<String> _del_user_names;
	
	void parse() {
		if (_total_length <= 2) {
			return;
		}
		while (!isEnd()) {
			int tag = readC();
			switch (tag) {
			case 0x0a:
				int name_length = readC();
				if (name_length > 0) {
					if (_del_user_names == null) {
						_del_user_names = new java.util.LinkedList<String>();
					}
					_del_user_names.add(readS(name_length));
				}
				break;
			default:
				return;
			}
		}
	}
	
	boolean isValidation() {
		return _del_user_names != null && !_del_user_names.isEmpty();
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
		
		ClanMember member = null;
		for (String name : _del_user_names) {
			member = pledge.getClanMember(name);
			if (member == null) {
				continue;
			}
			pledge.del_store_allow_list(name);
			if (member.player != null) {
				member.player.sendPackets(S_BloodPledgeStoreAllowInfoNoti.DISALLOW);
			}
		}
		ClanTable.getInstance().updateStoreAllow(pledge);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_PledgeStoreAllowDel(data, client);
	}

}

