package l1j.server.server.clientpackets.proto;

import l1j.server.Config;
import l1j.server.server.GameClient;
import l1j.server.server.serverpackets.indun.S_IndunMatchingCancel;

public class A_IndunMatchingCancel extends ProtoHandler {
	protected A_IndunMatchingCancel(){}
	private A_IndunMatchingCancel(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost() || !Config.INTER.INTER_SERVER_ACTIVE || _client.isInterServer() || !_pc.getConfig()._indunAutoMatching) {
			return;
		}
		_pc.getConfig()._indunAutoMatching = false;
		_pc.getConfig()._indunAutoMatchingMapKind = null;
		_pc.sendPackets(S_IndunMatchingCancel.CANCEL);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_IndunMatchingCancel(data, client);
	}

}

