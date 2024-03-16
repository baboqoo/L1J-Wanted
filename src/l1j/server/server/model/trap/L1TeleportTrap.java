package l1j.server.server.model.trap;

import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.storage.TrapStorage;

public class L1TeleportTrap extends L1Trap {
	private final L1Location _loc;

	public L1TeleportTrap(TrapStorage storage) {
		super(storage);
		int x		= storage.getInt("teleportX");
		int y		= storage.getInt("teleportY");
		int mapId	= storage.getInt("teleportMapId");
		_loc		= new L1Location(x, y, mapId);
	}

	@Override
	public void onTrod(L1PcInstance trodFrom, L1Object trapObj) {
		sendEffect(trapObj);
		trodFrom.getTeleport().start(_loc.getX(), _loc.getY(), (short) _loc.getMapId(), 5, true);
	}

}

