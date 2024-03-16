package l1j.server.server.clientpackets;

import java.util.Calendar;

import l1j.server.Config;
import l1j.server.common.bin.ShipCommonBinLoader;
import l1j.server.common.bin.ship.L1ShipStatus;
import l1j.server.common.bin.ship.ShipCommonBin;
import l1j.server.common.bin.ship.ShipInfo;
import l1j.server.server.GameClient;
import l1j.server.server.model.Instance.L1PcInstance;

public class C_EnterShip extends ClientBasePacket {
	private static final String C_ENTER_SHIP = "[C] C_EnterShip";

	public C_EnterShip(byte abyte0[], GameClient client) {
		super(abyte0);
		L1PcInstance pc = client.getActiveChar();
		if (pc == null) {
			return;
		}
		int id = readC();
		if ((id == 0 || id == 1) && !Config.DUNGEON.ISLAND_LOCAL_ACTIVE) {
			return;
		}
		ShipCommonBin ship = ShipCommonBinLoader.getShip(id);
		if (ship == null) {
			return;
		}
		if (ship.getShip().get_levelLimit() > pc.getLevel()
				|| ship.getStatus() != L1ShipStatus.STAY
				|| !ship.getShipTime().containsKey(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1)
				|| pc.getInventory().findItemNameId(ship.getShip().get_ticket()) == null) {			
			return;
		}
		ShipInfo.PointT loc = ship.getShip().get_ShipLoc();
		if (loc == null) {
			System.out.println(String.format("[C_EnterShip] POINT_LOC_EMPTY : ID(%d)", id));
			return;
		}
		pc.getTeleport().start(loc.get_x(), loc.get_y(), (short)ship.getShip().get_shipWorld(), pc.getMoveState().getHeading(), 0, false, true);
	}

	@Override
	public String getType() {
		return C_ENTER_SHIP;
	}
}

