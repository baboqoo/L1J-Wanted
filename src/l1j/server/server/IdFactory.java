package l1j.server.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;

public class IdFactory {
	private static Logger _log = Logger.getLogger(IdFactory.class.getName());

	private int _curId;
	private Object _monitor = new Object();
	private static final int FIRST_ID = 0x10000000;

	private static IdFactory _instance = new IdFactory();

	private IdFactory() {
		loadState();
	}

	public static IdFactory getInstance() {
		return _instance;
	}

	public int nextId() {
		synchronized (_monitor) {
			return _curId++;
		}
	}

	private void loadState() {
		// DB로부터 MAXID를 요구한다
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT max(id)+1 AS nextid FROM ("
					+ "SELECT id FROM character_items UNION ALL "
					+ "SELECT id FROM character_teleport UNION ALL "
					+ "SELECT id FROM character_warehouse UNION ALL "
					+ "SELECT id FROM character_elf_warehouse UNION ALL "
					+ "SELECT id FROM character_package_warehouse UNION ALL "
					+ "SELECT id FROM character_present_warehouse UNION ALL "
					+ "SELECT id FROM character_special_warehouse UNION ALL "
					+ "SELECT id FROM clan_warehouse UNION ALL "
					+ "SELECT objid AS id FROM characters UNION ALL "
					+ "SELECT clan_id AS id FROM clan_data UNION ALL "
					+ "SELECT db_id AS id FROM character_death_item UNION ALL "
					+ "SELECT objId AS id FROM tj_coupon UNION ALL "
					+ "SELECT itemObjId AS id FROM character_favorbook UNION ALL "
					+ "SELECT objid AS id FROM character_companion) t");
			rs = pstm.executeQuery();
			int id = rs.next() ? rs.getInt("nextid") : 0;
			if (id < FIRST_ID) {
				id = FIRST_ID;
			}
			_curId = id;
//			System.out.println("■ 오브젝트ID NUMBER.......................... " + _curId);
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
}

