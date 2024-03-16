package l1j.server.server.clientpackets.proto;

import l1j.server.GameSystem.eventpush.EventPushLoader;
import l1j.server.GameSystem.eventpush.bean.EventPushObject;
import l1j.server.GameSystem.eventpush.user.EventPushUser;
import l1j.server.server.GameClient;
import l1j.server.server.serverpackets.eventpush.S_EventPushListStatusRead;

public class A_EventPushListStatusRead extends ProtoHandler {
	protected A_EventPushListStatusRead(){}
	private A_EventPushListStatusRead(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		readP(3);// 0x0a 길이 0x08
		int index = readC();
		readP(1);// 0x10
		int number = readC();
		
		EventPushUser userInfo = EventPushLoader.getInfo(_pc.getId());
		if (userInfo == null) {
			return;
		}
		EventPushObject obj = userInfo.getTemp(index);
		if (obj == null) {
			return;
		}
		obj.setStatus(1);// 읽기
		_pc.sendPackets(new S_EventPushListStatusRead(index, number), true);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_EventPushListStatusRead(data, client);
	}

}

