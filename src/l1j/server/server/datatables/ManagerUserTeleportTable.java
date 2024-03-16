package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;

public class ManagerUserTeleportTable {
	public class BooksTeleportLoc {
		public int _getX;
		public int _getY;
		public int _getMapId;
	}

	private static Logger _log = Logger.getLogger(ManagerUserTeleportTable.class.getName());
	
	private Map<String, BooksTeleportLoc> _booksTeleport = new HashMap<String, BooksTeleportLoc>();

	private static ManagerUserTeleportTable _instance;

	public static ManagerUserTeleportTable getInstance() {
		if (_instance == null) {
			_instance = new ManagerUserTeleportTable();
		}
		return _instance;
	}

	public static void reload() {
		ManagerUserTeleportTable oldInstance = _instance;
		_instance = new ManagerUserTeleportTable();
		if (oldInstance != null) {
			oldInstance._booksTeleport.clear();
		}
	}

	private ManagerUserTeleportTable() {
		selectBooksTeleport();
	}

	private void selectBooksTeleport() {
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT * FROM manager_user_teleport");
			rs		= pstm.executeQuery();
			BooksTeleportLoc bt = null;
			while(rs.next()){
				bt				= new BooksTeleportLoc();
				String bookId	= rs.getString("name");
				bt._getX		= rs.getInt("locX");
				bt._getY		= rs.getInt("locY");
				bt._getMapId	= rs.getInt("locMap");
				_booksTeleport.put(bookId, bt);
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	public BooksTeleportLoc getTeleportLoc(String bookId) {
		BooksTeleportLoc btl = _booksTeleport.get(bookId);
		if (btl == null) {
			return null;
		}
		return btl;
	}
}
