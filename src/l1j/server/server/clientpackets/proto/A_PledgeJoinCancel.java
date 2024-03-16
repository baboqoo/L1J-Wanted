package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;
import l1j.server.server.datatables.ClanJoinningTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.serverpackets.pledge.S_BloodPledgeJoinCancel;

public class A_PledgeJoinCancel extends ProtoHandler {
	protected A_PledgeJoinCancel(){}
	private A_PledgeJoinCancel(byte[] data, GameClient client) {
		super(data, client);
	}
	
	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		readP(1);// 0x08
		L1Clan clan = L1World.getInstance().getClan(readBit());
		if (clan == null) {
			_pc.sendPackets(S_BloodPledgeJoinCancel.NOT);
			return;
		}
		boolean result = ClanJoinningTable.getInstance().cancel(clan, _pc.getId());
		_pc.sendPackets(result ? S_BloodPledgeJoinCancel.OK : S_BloodPledgeJoinCancel.ERROR);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_PledgeJoinCancel(data, client);
	}

}

