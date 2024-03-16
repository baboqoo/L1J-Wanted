package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.L1CharacterConfig;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_FreePVPRegionNoti;
import l1j.server.server.templates.L1FreePVPRegion;
import l1j.server.server.utils.SQLUtil;

/**
 * Free PVP Region
 * @author LinOffice
 */
public class FreePVPRegionTable {
	private static FreePVPRegionTable _instance;
	public static FreePVPRegionTable getInstance(){
		if (_instance == null) {
			_instance = new FreePVPRegionTable();
		}
		return _instance;
	}
	
	private static final ConcurrentHashMap<Integer, L1FreePVPRegion> DATA = new ConcurrentHashMap<Integer, L1FreePVPRegion>();
	
	/**
	 * FREE PVP 지역 검증
	 * @param worldNumber
	 * @param x
	 * @param y
	 * @return boolean
	 */
	public static boolean isFreePVPRegion(int worldNumber, int x, int y) {
		return getFreePVPRegion(worldNumber, x, y) != null;
	}
	
	/**
	 * FREE PVP 지역
	 * @param worldNumber
	 * @param x
	 * @param y
	 * @return L1FreePVPRegion
	 */
	public static L1FreePVPRegion getFreePVPRegion(int worldNumber, int x, int y) {
		L1FreePVPRegion region = DATA.get(worldNumber);
		if (region == null) {
			return null;
		}
		if (region.getBox() != null && !region.getBox().isEmpty()) {
			for (S_FreePVPRegionNoti.Box box : region.getBox()) {
				if (x >= box.get_sx() && x <= box.get_ex() && y >= box.get_sy() && y <= box.get_ey()) {
					return region;
				}
			}
			return null;
		}
		return region;
	}
	
	/**
	 * FREE PVP 지역
	 * @param pc
	 * @return L1FreePVPRegion
	 */
	public static L1FreePVPRegion getFreePVPRegion(L1PcInstance pc) {
		return getFreePVPRegion((int)pc.getMapId(), pc.getX(), pc.getY());
	}
	
	/**
	 * FREE PVP 지역 검증 후 알림
	 * @param pc
	 */
	public static void sendPacket(L1PcInstance pc) {
		final L1CharacterConfig config	= pc.getConfig();
		if (config == null) {
			return;
		}
		final L1FreePVPRegion region	= getFreePVPRegion(pc);
		final boolean isFreePVPRegion	= config.isFreePVPRegion();
		if (region == null) {
			if (isFreePVPRegion) {
				config.setFreePVPRegion(false);
			}
			return;
		}
		if (isFreePVPRegion) {
			return;
		}
		config.setFreePVPRegion(false);
		pc.sendPackets(region.getPck());
	}
	
	private FreePVPRegionTable(){
		load();
	}
	
	private void load(){
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		L1FreePVPRegion region	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT * FROM free_pvp_region WHERE isFreePvpZone = 'true' ORDER BY worldNumber, box_index");
			rs		= pstm.executeQuery();
			while(rs.next()){
				int worldNumber = rs.getInt("worldNumber");
				boolean isFreePvpZone = Boolean.parseBoolean(rs.getString("isFreePvpZone"));
				int box_sx = rs.getInt("box_sx");
				int box_sy = rs.getInt("box_sy");
				int box_ex = rs.getInt("box_ex");
				int box_ey = rs.getInt("box_ey");
				
				region = DATA.get(worldNumber);
				if (region == null) {
					region = new L1FreePVPRegion(worldNumber, isFreePvpZone, new java.util.LinkedList<S_FreePVPRegionNoti.Box>());
				}
				if (box_sx > 0 && box_ex > 0) {
					region.getBox().add(new S_FreePVPRegionNoti.Box(box_sx, box_sy, box_ex, box_ey));
				}
				region.setPck(new S_FreePVPRegionNoti(region.getWorldNumber(), region.isFreePvpZone(), region.getBox()));
				DATA.put(worldNumber, region);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public void reload(){
		DATA.clear();
		load();
	}
}

