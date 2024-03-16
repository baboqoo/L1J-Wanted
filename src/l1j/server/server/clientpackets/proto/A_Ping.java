package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;

public class A_Ping extends ProtoHandler {
	protected A_Ping(){}
	private A_Ping(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		readP(1);// 0x08
		int objId = readBit();
		if (_pc.getId() != objId) {
			return;
		}
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_Ping(data, client);
	}

}

