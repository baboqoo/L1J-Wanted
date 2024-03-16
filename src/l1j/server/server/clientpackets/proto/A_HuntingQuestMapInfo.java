package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;
import l1j.server.server.serverpackets.huntingquest.S_HuntingQuestMapInfo;

public class A_HuntingQuestMapInfo extends ProtoHandler {
	protected A_HuntingQuestMapInfo(){}
	private A_HuntingQuestMapInfo(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		readP(1);// 0x08
		int map_number	= read4(_total_length - 1);
		_pc.sendPackets(new S_HuntingQuestMapInfo(_pc, map_number), true);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_HuntingQuestMapInfo(data, client);
	}

}

