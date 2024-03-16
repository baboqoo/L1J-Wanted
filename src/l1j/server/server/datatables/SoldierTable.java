package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.templates.L1Soldier;
import l1j.server.server.utils.SQLUtil;

public class SoldierTable {

	private static Logger _log = Logger.getLogger(SoldierTable.class.getName());

	private static SoldierTable _instance;

	private final Map<Integer, L1Soldier> _soldiers = new ConcurrentHashMap<Integer, L1Soldier>();

	public static SoldierTable getInstance() {
		if (_instance == null) {
			_instance = new SoldierTable();
		}
		return _instance;
	}

	private SoldierTable() {
		load();
	}

	private void load() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM castle_soldier");
			rs = pstm.executeQuery();
			L1Soldier soldier = null;
			while (rs.next()) {
				soldier = new L1Soldier(rs.getInt(1));
				soldier.setSoldier1(rs.getInt(2));
				soldier.setSoldier1NpcId(rs.getInt(3));
				soldier.setSoldier1Name(rs.getString(4));
				soldier.setSoldier2(rs.getInt(5));
				soldier.setSoldier2NpcId(rs.getInt(6));
				soldier.setSoldier2Name(rs.getString(7));
				soldier.setSoldier3(rs.getInt(8));
				soldier.setSoldier3NpcId(rs.getInt(9));
				soldier.setSoldier3Name(rs.getString(10));
				soldier.setSoldier4(rs.getInt(11));
				soldier.setSoldier4NpcId(rs.getInt(12));
				soldier.setSoldier4Name(rs.getString(13));

				_soldiers.put(soldier.getId(), soldier);
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	public L1Soldier[] getSoldierTableList() {
		return _soldiers.values().toArray(new L1Soldier[_soldiers.size()]);
	}

	public L1Soldier getSoldierTable(int id) {
		return _soldiers.get(id);
	}

	public void updateSoldier(L1Soldier soldier) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE castle_soldier SET soldier1=?, soldier2=?, soldier3=?, soldier4=? WHERE castle_id=?");
			pstm.setInt(1, soldier.getSoldier1());
			pstm.setInt(2, soldier.getSoldier2());
			pstm.setInt(3, soldier.getSoldier3());
			pstm.setInt(4, soldier.getSoldier4());
			pstm.setInt(5, soldier.getId());
			pstm.executeUpdate();

			_soldiers.put(soldier.getId(), soldier);
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

}

