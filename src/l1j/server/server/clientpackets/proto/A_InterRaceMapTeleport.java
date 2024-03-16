package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;
import l1j.server.server.datatables.InterRaceRegionTable;

public class A_InterRaceMapTeleport extends ProtoHandler {
	protected A_InterRaceMapTeleport(){}
	private A_InterRaceMapTeleport(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		readP(1);// 0x08
		int worldNumber = readBit();
		if (_pc.getMapId() != worldNumber) {
			return;
		}
		readP(1);// 0x10
		int x = readBit();
		readP(1);// 0x18
		int y = readBit();
		if (!isEnableLoc(x, y, worldNumber)) {
			return;
		}
		_pc.getTeleport().start(x, y, (short) worldNumber, _pc.getMoveState().getHeading(), true);
	}
	
	boolean isEnableLoc(int x, int y, int worldNumber) {
		return InterRaceRegionTable.isLockey(new StringBuilder().append(worldNumber).append(x).append(y).toString());
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_InterRaceMapTeleport(data, client);
	}

}

