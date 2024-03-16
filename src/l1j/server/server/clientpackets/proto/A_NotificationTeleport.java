package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.datatables.NotificationTable;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.message.NotificationTeleportDataStream;
import l1j.server.server.templates.L1Notification;

public class A_NotificationTeleport extends ProtoHandler {
	protected A_NotificationTeleport(){}
	private A_NotificationTeleport(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isNotTeleport() || _pc.isGhost()) {
			return;
		}
		if (!_pc.getMap().isEscapable() || _client.isInterServer()) {
			_pc.sendPackets(L1ServerMessage.sm647);// 이곳에서는 텔레포트를 할 수 없습니다.
			return;
		}
		readP(1);
		L1Notification noti = NotificationTable.getNotification(readC());
		if (noti == null) {
			return;
		}
		int[] loc = noti.getTeleportLoc();
		if (loc == null) {
			return;
		}
		if (!_pc.getInventory().consumeItem(L1ItemId.ADENA, NotificationTeleportDataStream.ADENA_COUNT)) {
			_pc.sendPackets(L1ServerMessage.sm189);// 아데나가 부족합니다.
			return;
	    }
		_pc.getTeleport().start(loc[1], loc[2], (short) loc[0], _pc.getMoveState().getHeading(), true);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_NotificationTeleport(data, client);
	}

}

