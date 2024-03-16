package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;

public class A_PledgePreJoin extends ProtoHandler {
	protected A_PledgePreJoin(){}
	private A_PledgePreJoin(byte[] data, GameClient client) {
		super(data, client);
	}
	
	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_PledgePreJoin(data, client);
	}

}

