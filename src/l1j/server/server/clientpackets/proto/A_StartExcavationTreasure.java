package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;
import l1j.server.server.model.L1TownLocation;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1TreasureInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class A_StartExcavationTreasure extends ProtoHandler {
	protected A_StartExcavationTreasure(){}
	private A_StartExcavationTreasure(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost() || _pc.getMapId() != L1TownLocation.GETBACK_MAP_TREASURE_ISLAND) {
			return;
		}
		readP(1);
		int objid = readBit();
		L1TreasureInstance treasure = (L1TreasureInstance)L1World.getInstance().findObject(objid);

		if (treasure == null || treasure.isExcavation()) {
			return;
		}

		// if we left this, it will interrupt the TreasureDetectShovel and will not be able to get the treasures, so we need to disable it here and add in TreasureDetectShovel
		// treasure.setExcavation();
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_StartExcavationTreasure(data, client);
	}

}

