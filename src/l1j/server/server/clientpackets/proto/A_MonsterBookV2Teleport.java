package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.serverpackets.S_Paralysis;

public class A_MonsterBookV2Teleport extends ProtoHandler {
	protected A_MonsterBookV2Teleport(){}
	private A_MonsterBookV2Teleport(byte[] data, GameClient client) {
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
			if (_pc.getMap().isEscapable() && !_client.isInterServer()) {
				_pc.getQuest().getWeekQuest().teleport(difficulty, section);
			} else {
				_pc.sendPackets(S_Paralysis.TELEPORT_UNLOCK);
				_pc.sendPackets(L1ServerMessage.sm4726);
			}
		}
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_MonsterBookV2Teleport(data, client);
	}

}

