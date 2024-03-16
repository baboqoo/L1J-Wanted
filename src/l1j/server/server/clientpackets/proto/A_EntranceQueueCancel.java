package l1j.server.server.clientpackets.proto;

import l1j.server.Config;
import l1j.server.server.GameClient;
import l1j.server.server.controller.action.EntranceQueue;

public class A_EntranceQueueCancel extends ProtoHandler {
	protected A_EntranceQueueCancel(){}
	private A_EntranceQueueCancel(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_client == null || !Config.SERVER.ACCESS_STANBY) {
			return;
		}
		EntranceQueue.getInstance().cancel(_client);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_EntranceQueueCancel(data, client);
	}

}

