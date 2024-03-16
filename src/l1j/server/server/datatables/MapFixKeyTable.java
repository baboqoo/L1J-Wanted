package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.utils.SQLUtil;

public class MapFixKeyTable {
	private static Logger _log = Logger.getLogger(MapFixKeyTable.class.getName());

	private static MapFixKeyTable _instance;

	private final ArrayList<String> _Lockey;

	private MapFixKeyTable() {
		_Lockey = new ArrayList<String>();
		load();
	}

	public static MapFixKeyTable getInstance() {
		if (_instance == null) {
			_instance = new MapFixKeyTable();
		}
		return _instance;
	}

	public boolean isLockey(String key) {
		return _Lockey.contains(key);
	}

	private void load() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM map_fix_key");
			rs = pstm.executeQuery();

			while (rs.next()) {
				int srcX = rs.getInt("locX");
				int srcY = rs.getInt("locY");
				int srcMapId = rs.getInt("mapId");
				String key = new StringBuilder().append(srcMapId).append(srcX).append(srcY).toString();
				_Lockey.add(key);
				L1WorldMap.getInstance().getMap((short) srcMapId).setPassable(srcX, srcY, false);
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
			System.out.println("[MapFixKey] size : " + _Lockey.size());
		}
	}

	public void storeLocFix(int locX, int locY, int mapId) {
		Connection con = null;
		PreparedStatement pstm = null;
		String key = new StringBuilder().append(mapId).append(locX).append(locY).toString();
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO map_fix_key SET locX=?, locY=?, mapId=?");
			pstm.setInt(1, locX);
			pstm.setInt(2, locY);
			pstm.setInt(3, mapId);
			pstm.executeUpdate();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
			_Lockey.add(key);
		}
	}

	public void deleteLocFix(int locX, int locY, int mapId) {
		Connection con = null;
		PreparedStatement pstm = null;
		String key = new StringBuilder().append(mapId).append(locX).append(locY).toString();
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM map_fix_key WHERE locX=? AND locY=? AND mapId=?");
			pstm.setInt(1, locX);
			pstm.setInt(2, locY);
			pstm.setInt(3, mapId);
			pstm.executeUpdate();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
			_Lockey.remove(key);
		}
	}

	public static void reload() {
		MapFixKeyTable oldInstance = _instance;
		_instance = new MapFixKeyTable();
		oldInstance._Lockey.clear();
	}
}

