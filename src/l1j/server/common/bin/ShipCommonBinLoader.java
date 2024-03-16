package l1j.server.common.bin;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.common.bin.ship.L1ShipStatus;
import l1j.server.common.bin.ship.L1ShipTime;
import l1j.server.common.bin.ship.ShipCommonBin;
import l1j.server.common.bin.ship.ShipInfo;
import l1j.server.common.bin.ship.ShipInfo.ShipInfoListT;
import l1j.server.common.bin.ship.ShipInfo.ShipInfoListT.ShipT;
import l1j.server.server.utils.SQLUtil;

/**
 * ShipInfo-common.bin 파일 로더
 * @author LinOffice
 */
public class ShipCommonBinLoader {
	private static Logger _log = Logger.getLogger(ShipCommonBinLoader.class.getName());
	private static ShipCommonBinLoader _instance;
	public static ShipCommonBinLoader getInstance(){
		if (_instance == null) {
			_instance = new ShipCommonBinLoader();
		}
		return _instance;
	}
	
	private static final HashMap<Integer, ShipCommonBin> BIN_DATA = new HashMap<Integer, ShipCommonBin>();
	private static ShipInfo bin;
	
	public static Collection<ShipCommonBin> getShips() {
		return BIN_DATA.values();
	}
	
	public static ShipCommonBin getShip(int id) {
		return BIN_DATA.get(id);
	}
	
	private ShipCommonBinLoader(){
		if (Config.COMMON.COMMON_SHIP_BIN_UPDATE) {
			loadFile();
		} else {
			load();
		}
	}
	
	private void loadFile(){
		bin = ShipInfo.newInstance();
		try {
			bin.readFrom(ProtoInputStream.newInstance("./data/Contents/ShipInfo-common.bin"));// bin 파일을 읽는다
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		if (!bin.isInitialized())
			throw new IllegalArgumentException(String.format("fail initialized info data.(ShipCommonBin) %d", bin.getInitializeBit()));
		
		regist();
	}
	
	private static final String TRUNCATE_QUERY = "truncate table bin_ship_common";
	private static final String INSERT_QUERY = "INSERT INTO bin_ship_common SET id=?, "
			+ "dockWorld=?, shipWorld=?, ticket=?, levelLimit=?, "
			+ "dock_startX=?, dock_startY=?, dock_endX=?, dock_endY=?, "
			+ "shipLoc_x=?, shipLoc_y=?, "
			+ "destWorld=?, destLoc_x=?, destLoc_y=?, destLoc_range=?, "
			+ "schedule_day=?, schedule_time=?, schedule_duration=?, schedule_ship_operating_duration=?, "
			+ "returnWorld=?, returnLoc_x=?, returnLoc_y=?";
	
	private void regist(){
		try {
			java.util.LinkedList<ShipInfoListT> list = bin.get_ShipInfoList();
			Connection con			= null;
			PreparedStatement pstm	= null;
			try {
				con		= L1DatabaseFactory.getInstance().getConnection();
				pstm	= con.prepareStatement(TRUNCATE_QUERY);
				pstm.execute();
			} catch(SQLException e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			} finally {
				SQLUtil.close(pstm, con);
			}
			

			if (list == null || list.isEmpty()) {
				return;
			}
			
			try {
				con		= L1DatabaseFactory.getInstance().getConnection();
				con.setAutoCommit(false);
				
				pstm	= con.prepareStatement(INSERT_QUERY);
				for (ShipInfoListT listT : list) {
					if (listT.get_Ship() == null) {
						continue;
					}
					for (ShipT info : listT.get_Ship()) {
						BIN_DATA.put(info.get_id(), new ShipCommonBin(info.get_id(), info, getShipTimeParse(info.get_Schedule()), L1ShipStatus.NONE));
						int idx = 0;
						pstm.setInt(++idx, info.get_id());
						pstm.setInt(++idx, info.get_dockWorld());
						pstm.setInt(++idx, info.get_shipWorld());
						pstm.setInt(++idx, info.get_ticket());
						pstm.setInt(++idx, info.get_levelLimit());
						ShipInfo.BoxT dock = info.get_Dock();
						pstm.setInt(++idx, dock == null ? 0 : dock.get_startX());
						pstm.setInt(++idx, dock == null ? 0 : dock.get_startY());
						pstm.setInt(++idx, dock == null ? 0 : dock.get_endX());
						pstm.setInt(++idx, dock == null ? 0 : dock.get_endY());
						ShipInfo.PointT shipLoc = info.get_ShipLoc();
						pstm.setInt(++idx, shipLoc == null ? 0 : shipLoc.get_x());
						pstm.setInt(++idx, shipLoc == null ? 0 : shipLoc.get_y());
						pstm.setInt(++idx, info.get_destWorld());
						ShipInfo.RandomPoint destLoc = info.get_DestLoc();
						pstm.setInt(++idx, destLoc == null ? 0 : destLoc.get_x());
						pstm.setInt(++idx, destLoc == null ? 0 : destLoc.get_y());
						pstm.setInt(++idx, destLoc == null ? 0 : destLoc.get_range());
						ShipInfo.ScheduleT schedule = info.get_Schedule();
						pstm.setString(++idx, schedule == null ? null : schedule.get_schedule_day());
						pstm.setBytes(++idx, schedule.get_schedule_time());
						pstm.setInt(++idx, schedule == null ? 0 : schedule.get_schedule_duration());
						pstm.setInt(++idx, schedule == null ? 0 : schedule.get_ship_operating_duration());
						pstm.setInt(++idx, info.get_returnWorld());
						ShipInfo.PointT returnLoc = info.get_ReturnLoc();
						pstm.setInt(++idx, returnLoc == null ? 0 : returnLoc.get_x());
						pstm.setInt(++idx, returnLoc == null ? 0 : returnLoc.get_y());
						pstm.addBatch();
						pstm.clearParameters();
					}
				}
				pstm.executeBatch();
				pstm.clearBatch();
				con.commit();
			} catch(SQLException e) {
				try {
					con.rollback();
				} catch(SQLException sqle){
					_log.log(Level.SEVERE, sqle.getLocalizedMessage(), sqle);
				}
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			} catch(Exception e) {
				try {
					con.rollback();
				} catch(SQLException sqle){
					_log.log(Level.SEVERE, sqle.getLocalizedMessage(), sqle);
				}
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			} finally {
				try {
					con.setAutoCommit(true);
				} catch (SQLException e) {
					_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				}
				SQLUtil.close(pstm, con);
			}
			System.out.println("ShipInfo-common.bin [update completed]. TABLE : bin_ship_common");
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	private void load(){
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT * FROM bin_ship_common ORDER BY id ASC");
			rs		= pstm.executeQuery();
			while(rs.next()){
				ShipT ship = new ShipT(rs);
				BIN_DATA.put(ship.get_id(), new ShipCommonBin(ship.get_id(), ship, getShipTimeParse(ship.get_Schedule()), L1ShipStatus.NONE));
			}
		} catch(SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch(Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	private HashMap<Integer, ArrayList<L1ShipTime>> getShipTimeParse(ShipInfo.ScheduleT schedule){
		ArrayList<L1ShipTime> list = new ArrayList<L1ShipTime>();
		L1ShipTime shipTime = null;

		// seems that before, the time data was in binary format, but now is in String format, so we need to change the way to calculate this
		String s = new String(schedule.get_schedule_time(), StandardCharsets.UTF_8);
		String[] splitted = s.trim().split("\\s*,\\s*");
		for (int i=0; i<splitted.length; i++) {									
			shipTime = new L1ShipTime();
			int _time = Integer.valueOf(splitted[i]) * 3600; 
			shipTime.setHour(_time);
			shipTime.setStay(schedule.get_schedule_duration() * 60);
			shipTime.setDrive(schedule.get_ship_operating_duration() * 60);
			shipTime.setLeave(shipTime.getHour() + shipTime.getStay());// 배 출발 시간
			shipTime.setArrive(shipTime.getLeave() + shipTime.getDrive());// 배 도착 시간
			list.add(shipTime);
		}

		/*for (int i=0; i<schedule.get_schedule_time().length; i++) {									
			shipTime = new L1ShipTime();
			int _time = (int)schedule.get_schedule_time()[i] * 3600; 
			//System.out.println("Para el elemento de la posicion " + i + " que es " + (int)schedule.get_schedule_time()[i] + " se le pone el valor " + _time);
			shipTime.setHour(_time);
			shipTime.setStay(schedule.get_schedule_duration() * 60);
			shipTime.setDrive(schedule.get_ship_operating_duration() * 60);
			shipTime.setLeave(shipTime.getHour() + shipTime.getStay());// 배 출발 시간
			shipTime.setArrive(shipTime.getLeave() + shipTime.getDrive());// 배 도착 시간
			list.add(shipTime);
		}*/
		
		HashMap<Integer, ArrayList<L1ShipTime>> result = new HashMap<Integer, ArrayList<L1ShipTime>>();
		String scheduleDay = schedule.get_schedule_day();
		for (int i = 0; i < scheduleDay.length(); i++) {
			if (scheduleDay.charAt(i) == '1') {
				result.put(i, list);
			}
		}
		return result;
	}
	
	public void reload(){
		BIN_DATA.clear();
		if (Config.COMMON.COMMON_SHIP_BIN_UPDATE) {
			loadFile();
		} else {
			load();
		}
	}
}

