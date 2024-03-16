package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1TownLocation;
import l1j.server.server.model.item.L1ItemId;

public class A_SkyGardenTeleport extends ProtoHandler {
	protected A_SkyGardenTeleport(){}
	private A_SkyGardenTeleport(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost() || _pc.isNotTeleport()) {
			return;
		}
		if ((_pc.getMapId() == 99 || _pc.getMapId() == 6202 || _pc.getMapId() == 5490 || _pc.getMapId() == 5166 || _client.isInterServer() || !_pc.getMap().isEscapable()) && !_pc.isGm()) {
			_pc.sendPackets(L1ServerMessage.sm647);// 이곳에서는 텔레포트를 할 수 없습니다.
			return;
		}
		if (!_pc.isPCCafe() && !_pc.getInventory().consumeItem(L1ItemId.PIXIE_GOLD_FEATHER, 1)) {
			_pc.sendPackets(L1ServerMessage.sm4487);// 픽시의 금빛 깃털이 부족하여 사용할 수 없습니다.
			return;
		}
		int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_SKYGARDEN);
		_pc.getTeleport().c_start(loc[0], loc[1], (short) loc[2], _pc.getMoveState().getHeading(), true);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_SkyGardenTeleport(data, client);
	}

}

