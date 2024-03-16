package l1j.server.server.clientpackets.proto;

import l1j.server.IndunSystem.indun.IndunInfo;
import l1j.server.IndunSystem.indun.IndunList;
import l1j.server.server.GameClient;
import l1j.server.server.serverpackets.indun.S_IndunRoomInfo;
import l1j.server.server.serverpackets.indun.S_IndunRoomInfo.ArenaInfomationResult;

public class A_IndunRoomInformation extends ProtoHandler {
	protected A_IndunRoomInformation(){}
	private A_IndunRoomInformation(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		readP(1);
		IndunInfo info = IndunList.getIndunInfo(readBit());
		_pc.sendPackets(new S_IndunRoomInfo(ArenaInfomationResult.SUCCESS, info), true);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_IndunRoomInformation(data, client);
	}

}

