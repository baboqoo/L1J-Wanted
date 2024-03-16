package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;
import l1j.server.server.serverpackets.revenge.S_RevengeInfo;

public class A_RevengeInfo extends ProtoHandler {
	protected A_RevengeInfo(){}
	private A_RevengeInfo(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		_pc.sendPackets(new S_RevengeInfo(_pc), true);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_RevengeInfo(data, client);
	}

}

