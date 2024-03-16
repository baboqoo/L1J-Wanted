package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;
import l1j.server.server.serverpackets.huntingquest.S_HuntingQuestUserValue;

public class A_HuntingQuestUserValue extends ProtoHandler {
	protected A_HuntingQuestUserValue(){}
	private A_HuntingQuestUserValue(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		_pc.sendPackets(S_HuntingQuestUserValue.HUNTING_QUEST_USER_VALUE);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_HuntingQuestUserValue(data, client);
	}

}

