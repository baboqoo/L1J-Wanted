package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;

public class A_QuestLasted extends ProtoHandler {
	protected A_QuestLasted(){}
	private A_QuestLasted(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null) {
			return;
		}
		
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_QuestLasted(data, client);
	}

}

