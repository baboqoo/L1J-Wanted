package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;

public class A_PetballContentEnd extends ProtoHandler {
	protected A_PetballContentEnd(){}
	private A_PetballContentEnd(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		_pc.getTeleport().stateLeave();
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_PetballContentEnd(data, client);
	}

}

