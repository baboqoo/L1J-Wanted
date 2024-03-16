package l1j.server.server.datatables;

import java.sql.*;
import java.util.HashMap;
import java.util.logging.Logger;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.L1NpcTalkData;
import l1j.server.server.utils.SQLUtil;

public class NPCTalkDataTable {

	private static Logger _log = Logger.getLogger(NPCTalkDataTable.class.getName());

	private static NPCTalkDataTable _instance;

	private HashMap<Integer, L1NpcTalkData> _datatable = new HashMap<Integer, L1NpcTalkData>();

	public static NPCTalkDataTable getInstance() {
		if (_instance == null) {
			_instance = new NPCTalkDataTable();
		}
		return _instance;
	}

	public static void reload() {
		NPCTalkDataTable oldInstance = _instance;
		_instance = new NPCTalkDataTable();
		oldInstance._datatable.clear();
	}

	private NPCTalkDataTable() {
		parseList();
	}

	private void parseList() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM npcaction");
			rs = pstm.executeQuery();
			L1NpcTalkData l1npctalkdata = null;
			while(rs.next()){
				l1npctalkdata = new L1NpcTalkData();
				l1npctalkdata.setNpcID(rs.getInt(1));
				l1npctalkdata.setNormalAction(rs.getString(2));
				l1npctalkdata.setCaoticAction(rs.getString(3));
				l1npctalkdata.setTeleportURL(rs.getString(4));
				l1npctalkdata.setTeleportURLA(rs.getString(5));
				_datatable.put(new Integer(l1npctalkdata.getNpcID()), l1npctalkdata);
			}
			//_log.config("NPC 액션 리스트 " + _datatable.size() + "건 로드");
			_log.config("NPC Action List " + _datatable.size() + " entries loaded");
		} catch (SQLException e) {
			_log.warning("error while creating npc action table " + e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	public L1NpcTalkData getTemplate(int i) {
		return _datatable.get(new Integer(i));
	}

}

