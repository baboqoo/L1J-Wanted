package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.serverpackets.pledge.S_BloodPledgeFind;

public class A_PledgeFind extends ProtoHandler {
	protected A_PledgeFind(){}
	private A_PledgeFind(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		readP(1);// 0x0a
		int name_length	= readC();// size
		if (name_length <= 0) {
			return;
		}
		String pledge_name	= readS(name_length);
		L1Clan clan			= L1World.getInstance().getClan(pledge_name);
		if (clan == null || clan.isBot()) {
			return;
		}
		_pc.sendPackets(new S_BloodPledgeFind(clan), true);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_PledgeFind(data, client);
	}

}

