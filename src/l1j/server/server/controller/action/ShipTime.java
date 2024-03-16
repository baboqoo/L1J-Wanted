package l1j.server.server.controller.action;

import java.util.ArrayList;
import java.util.Calendar;

import l1j.server.common.bin.ShipCommonBinLoader;
import l1j.server.common.bin.ship.L1ShipStatus;
import l1j.server.common.bin.ship.L1ShipTime;
import l1j.server.common.bin.ship.ShipCommonBin;
import l1j.server.common.bin.ship.ShipInfo;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_LocRefresh;
import l1j.server.server.utils.CommonUtil;

/**
 * 배 운행 컨트롤러
 * @author LinOffice
 */
public class ShipTime implements ControllerInterface {
	private static class newInstance {
		public static final ShipTime INSTANCE = new ShipTime();
	}
	public static ShipTime getInstance() {
		return newInstance.INSTANCE;
	}
	private ShipTime(){}

	@Override
	public void execute() {
		try {
			int day		= Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
			int nowTime	= CommonUtil.getCurrentDayTimeSeconds();
			for (ShipCommonBin ship : ShipCommonBinLoader.getShips()) {
				if (ship == null) {
					continue;
				}
				
				ArrayList<L1ShipTime> shipTimes = ship.getShipTime().get(day);
				if (shipTimes == null || shipTimes.isEmpty()) {
					continue;
				}
				for (L1ShipTime shipTime : shipTimes) {
					if (shipTime == null) {
						continue;
					}
					if (ship.getStatus() != L1ShipStatus.STAY && nowTime >= shipTime.getHour() && nowTime < shipTime.getLeave()) {// 배 도착
						ship.setStatus(L1ShipStatus.STAY);
						break;
					}
					if (ship.getStatus() != L1ShipStatus.LEAVE && nowTime >= shipTime.getLeave() && nowTime < shipTime.getArrive()) {// 배 출발
						ship.setStatus(L1ShipStatus.LEAVE);
						leaveShip(ship);
						break;
					}
					if (ship.getStatus() != L1ShipStatus.ARRIVE && nowTime >= shipTime.getArrive() && nowTime < shipTime.getArrive() + 100) {// 배 종착
						ship.setStatus(L1ShipStatus.ARRIVE);
						arriveShip(ship);
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void execute(L1PcInstance pc) {
	}
	
	// 배 출발
	void leaveShip(ShipCommonBin ship){
		for (L1PcInstance pc : L1World.getInstance().getMapPlayer(ship.getShip().get_shipWorld())) {// 배에 타고있는 인원
			if (pc == null) {
				continue;
			}
			pc.sendPackets(S_LocRefresh.REFRESH);
			pc.sendPackets(L1SystemMessage.SHIP_DRIVE_MESSAGE);
			pc.getInventory().consumeItemNameId(ship.getShip().get_ticket(), 1);// 배표 소모
			pc._isShipIn = true;
		}
	}
	
	// 배 종착
	void arriveShip(ShipCommonBin ship){
		for (L1PcInstance pc : L1World.getInstance().getMapPlayer(ship.getShip().get_shipWorld())) {// 배에 타고있는 인원
			if (pc == null) {
				continue;
			}
			pc.sendPackets(S_LocRefresh.REFRESH);
			pc._isShipIn = false;
			ShipInfo.RandomPoint loc = ship.getShip().get_DestLoc();
			pc.getTeleport().c_start(loc.get_x(), loc.get_y(), (short) ship.getShip().get_destWorld(), pc.getMoveState().getHeading(), false);// 배에서 내린다
		}
	}
}

