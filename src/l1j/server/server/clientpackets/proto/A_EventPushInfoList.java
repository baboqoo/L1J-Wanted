package l1j.server.server.clientpackets.proto;

import javolution.util.FastMap;
import l1j.server.GameSystem.eventpush.EventPushLoader;
import l1j.server.GameSystem.eventpush.bean.EventPushObject;
import l1j.server.GameSystem.eventpush.user.EventPushUser;
import l1j.server.server.GameClient;
import l1j.server.server.serverpackets.eventpush.S_EventPushInfoList;

public class A_EventPushInfoList extends ProtoHandler {
	protected A_EventPushInfoList(){}
	private A_EventPushInfoList(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		EventPushUser userInfo = EventPushLoader.getInfo(_pc.getId());
		if (userInfo == null) {
			return;
		}
		FastMap<Integer, EventPushObject> map = userInfo.getInfo();
		if (map == null || map.isEmpty()) {
			return;
		}
		_pc.sendPackets(new S_EventPushInfoList(_pc, map.values()), true);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_EventPushInfoList(data, client);
	}

}

