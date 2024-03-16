package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;

public class AutoLoot {
	private static Logger _log = Logger.getLogger(AutoLoot.class.getName());
	private static ArrayList<Integer> _idlist = new ArrayList<Integer>();
	private static AutoLoot _instance;
	public static AutoLoot getInstance() {
		if (_instance == null) {
			_instance = new AutoLoot();
		}
		return _instance;
	}

	private AutoLoot() {
		load();
	}
	
	public static ArrayList<Integer> getIdList() {
		return _idlist;
	}

	public static boolean isAutoLoot(int itemId) {
		return _idlist.contains(itemId);
	}

	private void load() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM autoloot");
			rs = pstm.executeQuery();
			while (rs.next()) {
				_idlist.add(rs.getInt("item_id"));
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	public void storeId(int itemid) {
		int index = _idlist.indexOf(itemid);
		if (index != -1) {
			return;
		}
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("INSERT INTO autoloot SET item_id=?");
			pstm.setInt(1, itemid);
			pstm.execute();
			_idlist.add(itemid);
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

	public void deleteId(int itemid) {
		Connection con = null;
		PreparedStatement pstm = null;
		int index = _idlist.indexOf(itemid);
		if (index == -1) {
			return;
		}
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM autoloot WHERE item_id=?");
			pstm.setInt(1, itemid);
			pstm.execute();
			_idlist.remove(index);
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

	public void reload() {
		_idlist.clear();
		load();
	}
	
}

