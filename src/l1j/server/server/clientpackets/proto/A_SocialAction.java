package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;
import l1j.server.server.serverpackets.action.S_SocialAction;
import l1j.server.server.serverpackets.action.S_SocialAction.SOCIAL_ACTION_TYPE;

public class A_SocialAction extends ProtoHandler {
	protected A_SocialAction(){}
	private A_SocialAction(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		readP(1);// 0x08
		SOCIAL_ACTION_TYPE action_type = SOCIAL_ACTION_TYPE.fromInt(readC());
		if (action_type == null) {
			return;
		}
		readP(1);// 0x10
		int action_code = readC();
		if (action_code < 1 || action_code > 11) {
			return;
		}
		_pc.broadcastPacketWithMe(new S_SocialAction(action_type, action_code, _pc.getId()), true);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_SocialAction(data, client);
	}

}

