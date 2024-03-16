package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;

public class A_IndunUIRemove extends ProtoHandler {
	protected A_IndunUIRemove(){}
	private A_IndunUIRemove(byte[] data, GameClient client) {
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
		return new A_IndunUIRemove(data, client);
	}

}

