package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;
import l1j.server.server.serverpackets.S_UserPlayInfoNoti;

public class A_UserPlayInfo extends ProtoHandler {
	protected A_UserPlayInfo(){}
	private A_UserPlayInfo(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost() || _pc.getDungoenTimer() == null) {
			return;
		}
		_pc.sendPackets(new S_UserPlayInfoNoti(_pc), true);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_UserPlayInfo(data, client);
	}

}

