package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;

public class A_MonsterBookV2Reward extends ProtoHandler {
	protected A_MonsterBookV2Reward(){}
	private A_MonsterBookV2Reward(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		if (_pc.getQuest().getWeekQuest() != null) {
			readP(1); // 0x08
			int difficulty = readC();
			readP(1); // 0x10
			int section = readC();
			_pc.getQuest().getWeekQuest().complete(difficulty, section);
		}
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_MonsterBookV2Reward(data, client);
	}

}

