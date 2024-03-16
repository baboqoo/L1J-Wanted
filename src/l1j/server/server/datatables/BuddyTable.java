package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap; // import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.L1Buddy;
import l1j.server.server.utils.SQLUtil;

/**
 * 친구 정보
 * @author LinOffice
 */
public class BuddyTable {
	private static Logger _log = Logger.getLogger(BuddyTable.class.getName());
	private static BuddyTable _instance;
	private final Map<Integer, L1Buddy> _buddys = new HashMap<Integer, L1Buddy>();
	public static BuddyTable getInstance() {
		if (_instance == null) {
			_instance = new BuddyTable();
		}
		return _instance;
	}

	private BuddyTable() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT distinct(char_id) as char_id FROM character_buddys");
			rs = pstm.executeQuery();
			PreparedStatement buddysPS = null;
			ResultSet buddysRS = null;
			while (rs.next()) {
				try {
					buddysPS = con.prepareStatement("SELECT buddy_name, buddy_memo FROM character_buddys WHERE char_id = ?");
					int charId = rs.getInt("char_id");
					buddysPS.setInt(1, charId);
					L1Buddy buddy = new L1Buddy(charId);

					buddysRS = buddysPS.executeQuery();
					while (buddysRS.next()) {
						buddy.add(buddysRS.getString("buddy_name"), buddysRS.getString("buddy_memo"));
					}
					_buddys.put(buddy.getCharId(), buddy);
				} catch (Exception e) {
					_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				} finally {
					SQLUtil.close(buddysRS, buddysPS);
				}
			}
			_log.config("loaded " + _buddys.size() + " character's buddylists");
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	} 

	public L1Buddy getBuddyTable(int charId) {
		L1Buddy buddy = _buddys.get(charId);
		if (buddy == null) {
			buddy = new L1Buddy(charId);
			_buddys.put(charId, buddy);
		}
		return buddy;
	}

	public void addBuddy(int charId, String name, String memo) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO character_buddys SET char_id=?, buddy_name=?, buddy_memo=?");
			pstm.setInt(1, charId);
			pstm.setString(2, name);
			pstm.setString(3, memo);
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

	public void removeBuddy(int charId, String buddyName) {
		L1Buddy buddy = getBuddyTable(charId);
		if (!buddy.containsName(buddyName)) {
			return;
		}
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM character_buddys WHERE char_id=? AND buddy_name=?");
			pstm.setInt(1, charId);
			pstm.setString(2, buddyName);
			pstm.execute();
			buddy.remove(buddyName);
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	/** 메모 업데이트 **/
	public int updateBuddyMemo(int charId, String name, String memo) {
		int result = 0;
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE character_buddys SET buddy_memo=? WHERE char_id=? AND buddy_name=?");
			pstm.setString(1, memo);
			pstm.setInt(2, charId);
			pstm.setString(3, name);
			result = pstm.executeUpdate();
			
			L1Buddy buddy = _buddys.get(charId);
			buddy.updateMemo(name, memo);
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
		return result;
	}
	
	/** 메모 취득 **/
	public String getBuddyMemo(int charId, String name) {
		String bb = null;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT buddy_memo FROM character_buddys WHERE char_id='" + charId + "' AND buddy_name='" + name + "'");
			rs = pstm.executeQuery();
			if (rs.next()) {
				bb = rs.getString("buddy_memo");
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return bb;
	}
	
}
