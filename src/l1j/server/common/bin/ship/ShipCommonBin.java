package l1j.server.common.bin.ship;

import java.util.ArrayList;
import java.util.HashMap;

import l1j.server.common.bin.ship.ShipInfo.ShipInfoListT.ShipT;

public class ShipCommonBin {
	private int id;
	private ShipT ship;
	private HashMap<Integer, ArrayList<L1ShipTime>> shipTime;
	private L1ShipStatus status;
	
	public ShipCommonBin(int id, ShipT ship, HashMap<Integer, ArrayList<L1ShipTime>> shipTime, L1ShipStatus status) {
		this.id			= id;
		this.ship		= ship;
		this.shipTime	= shipTime;
		this.status		= status;
	}

	public int getId() {
		return id;
	}
	
	public ShipT getShip() {
		return ship;
	}

	public HashMap<Integer, ArrayList<L1ShipTime>> getShipTime() {
		return shipTime;
	}

	public L1ShipStatus getStatus() {
		return status;
	}

	public void setStatus(L1ShipStatus status) {
		this.status = status;
	}
}

