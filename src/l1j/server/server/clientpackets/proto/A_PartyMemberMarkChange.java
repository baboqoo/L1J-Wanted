package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;
import l1j.server.server.construct.L1InterServer;
import l1j.server.server.model.L1Party;
import l1j.server.server.serverpackets.party.S_PartyMarkChange;

public class A_PartyMemberMarkChange extends ProtoHandler {
	protected A_PartyMemberMarkChange(){}
	private A_PartyMemberMarkChange(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost() || _client.getInter() == L1InterServer.INSTANCE_DUNGEON) {
			return;
		}
		L1Party party = _pc.getParty();
		if (party == null || !party.isLeader(_pc)) {
			return;
		}
		byte[] flag	= readByte(_total_length);
		party.sendPacketToMembers(new S_PartyMarkChange(flag), true);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_PartyMemberMarkChange(data, client);
	}

}

