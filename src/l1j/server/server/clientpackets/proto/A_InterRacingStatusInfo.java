package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;

public class A_InterRacingStatusInfo extends ProtoHandler {
	protected A_InterRacingStatusInfo(){}
	private A_InterRacingStatusInfo(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private int _worldNumber;

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		readP(1);
		_worldNumber = readBit();
		if (_pc.getMapId() != _worldNumber) {
			return;
		}
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_InterRacingStatusInfo(data, client);
	}

}

