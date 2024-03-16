package l1j.server.server.clientpackets.proto;

import java.util.Iterator;

import javolution.util.FastMap;
import l1j.server.GameSystem.eventpush.EventPushLoader;
import l1j.server.GameSystem.eventpush.user.EventPushUser;
import l1j.server.server.GameClient;
import l1j.server.server.serverpackets.eventpush.S_EventPushListStatusDelete;

public class A_EventPushListStatusDelete extends ProtoHandler {
	protected A_EventPushListStatusDelete(){}
	private A_EventPushListStatusDelete(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		if (_total_length <= 0) {
			return;
		}
		FastMap<Integer, Integer> deleteMap = new FastMap<Integer, Integer>();
		for (int i=0; i<_total_length; i++) {
			if (readLength() == 0) {
				break;
			}
			int code = readC();
			if (code == 0x0a) {// 하나의 정보
				readP(2);
				int index = readBit();
				readP(1);// 10
				int number = readC();
				deleteMap.put(index, number);
			} else {
				break;
			}
		}
		if (deleteMap.isEmpty()) {
			return;
		}
		EventPushUser userInfo = EventPushLoader.getInfo(_pc.getId());
		if (userInfo == null) {
			return;
		}
		Iterator<Integer> itr = deleteMap.keySet().iterator();
		while(itr.hasNext()){
			userInfo.remove(itr.next());
		}
		_pc.sendPackets(new S_EventPushListStatusDelete(deleteMap), true);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_EventPushListStatusDelete(data, client);
	}

}

