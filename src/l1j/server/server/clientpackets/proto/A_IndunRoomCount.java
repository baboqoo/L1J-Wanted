package l1j.server.server.clientpackets.proto;

import l1j.server.Config;
import l1j.server.server.GameClient;
import l1j.server.server.serverpackets.indun.S_IndunNoti;
import l1j.server.server.serverpackets.indun.S_IndunNoti.ArenaNotiResult;

public class A_IndunRoomCount extends ProtoHandler {
	protected A_IndunRoomCount(){}
	private A_IndunRoomCount(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost() || !Config.INTER.INTER_SERVER_ACTIVE || _client.isInterServer()) {
			return;
		}
		_pc.sendPackets(new S_IndunNoti(ArenaNotiResult.SUCCESS), true);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_IndunRoomCount(data, client);
	}

}

