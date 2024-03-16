package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat; //## A1 war_time 오류 수정 위해 임포트 추가 
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.templates.L1Castle;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

public class CastleTable {

	private static Logger _log = Logger.getLogger(CastleTable.class.getName());

	private static CastleTable _instance;

	private final Map<Integer, L1Castle> _castles = new ConcurrentHashMap<Integer, L1Castle>();

	public static CastleTable getInstance() {
		if (_instance == null) {
			_instance = new CastleTable();
		}
		return _instance;
	}

	public void updateWarTime(String name, Calendar cal) {
		if (StringUtil.isNullOrEmpty(name)) {
			return;
		}
		for (int id : _castles.keySet()) {
			L1Castle castle = _castles.get(id);
			if (castle.getName().startsWith(name)) {
				castle.setWarTime((Calendar) cal.clone());
				updateCastle(castle);
			}
		}
	}

	public static void reload() {
		CastleTable oldInstance = _instance;
		_instance = new CastleTable();
		oldInstance._castles.clear();
	}

	private CastleTable() {
		load();
	}

	private Calendar timestampToCalendar(Timestamp ts) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(ts.getTime());
		return cal;
	}

	private void load() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM castle");
			rs = pstm.executeQuery();
			L1Castle castle = null;
			while (rs.next()) {
				castle = new L1Castle(rs.getInt("castle_id"), rs.getString("name"));
				castle.setWarTime(timestampToCalendar((Timestamp) rs.getObject("war_time")));
				castle.setTaxRate(rs.getInt("tax_rate"));
				castle.setPublicMoney(rs.getInt("public_money"));
				_castles.put(castle.getId(), castle);
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	public L1Castle[] getCastleTableList() {
		return _castles.values().toArray(new L1Castle[_castles.size()]);
	}

	public L1Castle getCastleTable(int id) {
		return _castles.get(id);
	}

	public void updateCastle(L1Castle castle) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE castle SET name=?, war_time=?, tax_rate=?, public_money=? WHERE castle_id=?");
			pstm.setString(1, castle.getName());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			String fm = sdf.format(castle.getWarTime().getTime());
			// String fm = DateFormat.getDateTimeInstance().format( //## A1 원본
			// castle.getWarTime().getTime()); //#
			pstm.setString(2, fm);
			pstm.setInt(3, castle.getTaxRate());
			pstm.setInt(4, castle.getPublicMoney());
			pstm.setInt(5, castle.getId());
			pstm.execute();

			_castles.put(castle.getId(), castle);
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

}

