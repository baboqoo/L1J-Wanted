package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.templates.L1GetBackRestart;
import l1j.server.server.utils.SQLUtil;

public class GetBackRestartTable {

	private static Logger _log = Logger.getLogger(GetBackRestartTable.class.getName());

	private static GetBackRestartTable _instance;

	private final HashMap<Integer, L1GetBackRestart> _getbackrestart = new HashMap<Integer, L1GetBackRestart>();

	public static GetBackRestartTable getInstance() {
		if (_instance == null) {
			_instance = new GetBackRestartTable();
		}
		return _instance;
	}

	public GetBackRestartTable() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM getback_restart");
			rs = pstm.executeQuery();
			L1GetBackRestart gbr = null;
			while(rs.next()){
				gbr = new L1GetBackRestart();
				int area = rs.getInt("area");
				gbr.setArea(area);
				gbr.setLocX(rs.getInt("locx"));
				gbr.setLocY(rs.getInt("locy"));
				gbr.setMapId(rs.getShort("mapid"));

				_getbackrestart.put(new Integer(area), gbr);
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	public L1GetBackRestart[] getGetBackRestartTableList() {
		return _getbackrestart.values().toArray(new L1GetBackRestart[_getbackrestart.size()]);
	}

}

