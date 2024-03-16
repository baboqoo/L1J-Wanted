package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.templates.L1CharSoldier;
import l1j.server.server.utils.SQLUtil;

public class CharSoldierTable {

	private static Logger _log = Logger.getLogger(CharSoldierTable.class.getName());

	private static CharSoldierTable _instance;

	private final ArrayList<L1CharSoldier> _charsoldier = new ArrayList<L1CharSoldier>();

	public static CharSoldierTable getInstance() {
		if (_instance == null) {
			_instance = new CharSoldierTable();
		}
		return _instance;
	}

	public CharSoldierTable() {
		charSoldierload();
	}

	private void charSoldierload() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM character_soldier");
			rs = pstm.executeQuery();
			L1CharSoldier charSoldier = null;
			while(rs.next()){
				charSoldier = new L1CharSoldier(rs.getInt(1));
				charSoldier.setSoldierNpc(rs.getInt(2));
				charSoldier.setSoldierCount(rs.getInt(3));
				charSoldier.setSoldierCastleId(rs.getInt(4));
				charSoldier.setSoldierTime(rs.getInt(5));

				_charsoldier.add(charSoldier);
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	public void addCharSoldier(L1CharSoldier newCharSoldier) {
		_charsoldier.add(newCharSoldier);
	}

	public ArrayList<L1CharSoldier> getCharSoldier(int id, long currentTime) {
		ArrayList<L1CharSoldier> list = new ArrayList<L1CharSoldier>();
		L1CharSoldier t;
		for (int i = 0; i < _charsoldier.size(); i++) {
			t = _charsoldier.get(i);
			if (t.getCharId() == id && t.getSoldierTime() < currentTime) {
				list.add(t);
			}
		}
		t = null;
		return list;
	}

	public void storeCharSoldier(L1CharSoldier newCharSoldier) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO character_soldier SET char_id=?, npc_id=?, count=?, castle_id=?, time=?");
			pstm.setInt(1, newCharSoldier.getCharId());
			pstm.setInt(2, newCharSoldier.getSoldierNpc());
			pstm.setInt(3, newCharSoldier.getSoldierCount());
			pstm.setInt(4, newCharSoldier.getSoldierCastleId());
			pstm.setInt(5, newCharSoldier.getSoldierTime());
			pstm.executeUpdate();

			addCharSoldier(newCharSoldier);
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

	/**
	 * 캐릭터가 가지고 있는 용병 갯수를 가져온다.
	 * 
	 * @param id
	 *            는 char_id
	 * @return Cscount 캐릭터가 가지고 있는 용병 갯수
	 */

	public int SoldierCalculate(int id) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int CScount = 0;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT count FROM character_soldier WHERE char_id='" + id + "'");
			rs = pstm.executeQuery();
			while (rs.next()) {
				CScount += (rs.getInt("count"));
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return CScount;
	}

	/**
	 * 용병 배치후 디비 삭제
	 * 
	 * @param castleid
	 */
	public void delCharCastleSoldier(int charid, int time) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM character_soldier WHERE char_id=? AND time=?");
			pstm.setInt(1, charid);
			pstm.setInt(2, time);
			pstm.executeUpdate();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
		L1CharSoldier c;
		for (int i = 0; i < _charsoldier.size(); i++) {
			c = _charsoldier.get(i);
			if (c.getCharId() == charid && c.getSoldierTime() == time) {
				_charsoldier.remove(c);
			}
		}
	}

	/**
	 * 전쟁후 해당성의 용병을 모두 클리어 한다.
	 * 
	 * @param castleid
	 */
	public void delCastleSoldier(int castleid) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM character_soldier WHERE castle_id=?");
			pstm.setInt(1, castleid);
			pstm.executeUpdate();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}

		L1CharSoldier c;
		for (int i = 0; i < _charsoldier.size(); i++) {
			c = _charsoldier.get(i);
			if (c.getSoldierCastleId() == castleid) {
				_charsoldier.remove(c);
			}
		}
	}
}
