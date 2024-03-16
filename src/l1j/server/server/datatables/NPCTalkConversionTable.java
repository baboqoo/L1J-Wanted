package l1j.server.server.datatables;

import java.sql.*;
import java.util.HashMap;
import java.util.logging.Logger;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.L1NpcTalkData;
import l1j.server.server.utils.SQLUtil;

public class NPCTalkConversionTable {

	private static Logger _log = Logger.getLogger(NPCTalkConversionTable.class.getName());

	private static NPCTalkConversionTable _instance;

	private HashMap<String, String> _datatable = new HashMap<String, String>();

	public static NPCTalkConversionTable getInstance() {
		if (_instance == null) {
			_instance = new NPCTalkConversionTable();
		}
		return _instance;
	}

	public static void reload() {
		NPCTalkConversionTable oldInstance = _instance;
		_instance = new NPCTalkConversionTable();
		oldInstance._datatable.clear();
	}

	private NPCTalkConversionTable() {
		parseList();
	}

	private void parseList() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM uml_conversion");
			rs = pstm.executeQuery();
			String umlOld;
			String umlNew;
			while(rs.next()){
				umlOld = rs.getString("oldname");
				umlNew = rs.getString("newname");
				System.out.println("agregando " + umlOld + " " + umlNew);
				_datatable.put(umlOld, umlNew);
			}
			_log.config("NPC Action List " + _datatable.size() + " entries loaded");
		} catch (SQLException e) {
			_log.warning("error while creating npc conversion table " + e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	public String getTemplate(String s) {
		return _datatable.get(s);
	}

}

