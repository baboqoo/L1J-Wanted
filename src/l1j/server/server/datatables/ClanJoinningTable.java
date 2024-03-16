package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1PledgeJoinningRequest;
import l1j.server.server.utils.SQLUtil;

/**
 * 혈맹 승인 가입 처리
 * @author LinOffice
 */
public class ClanJoinningTable {
	private static Logger _log = Logger.getLogger(ClanJoinningTable.class.getName());
	private static ClanJoinningTable _instance;

	public static ClanJoinningTable getInstance() {
		if (_instance == null) {
			_instance = new ClanJoinningTable();
		}
		return _instance;
	}

	private ClanJoinningTable() {
	}
	
	public void load(L1Clan clan) {
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT * FROM clan_joinning WHERE pledge_uid =? ORDER BY join_date DESC");
			pstm.setInt(1, clan.getClanId());
			rs		= pstm.executeQuery();
			while (rs.next()) {
				clan.addJoinningList(new L1PledgeJoinningRequest(clan.getClanId(),
						rs.getString("pledge_name"), rs.getInt("user_uid"), rs.getString("user_name"),
						rs.getString("join_message"), rs.getInt("class_type"), rs.getInt("join_date")));
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	public boolean add(L1Clan clan, L1PcInstance pc, String join_message) {
		if (clan.isJoinningList(pc.getId())) {
			return false;
		}
		L1PledgeJoinningRequest request = new L1PledgeJoinningRequest(clan.getClanId(), 
				clan.getClanName(), pc.getId(), pc.getName(), join_message, pc.getType(), (int)(System.currentTimeMillis() / 1000));
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO clan_joinning SET pledge_uid=?, pledge_name=?, user_uid=?, user_name=?, join_message=?, class_type=?, join_date=?");
			int index = 0;
			pstm.setInt(++index, request.getPledge_uid());
			pstm.setString(++index, request.getPledge_name());
			pstm.setInt(++index, request.getUser_uid());
			pstm.setString(++index, request.getUser_name());
			pstm.setString(++index, request.getJoin_message());
			pstm.setInt(++index, request.getClass_type());
			pstm.setInt(++index, request.getJoin_date());
			if (pstm.executeUpdate() > 0) {
				clan.addJoinningList(request);
				return true;
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
		return false;
	}

	public void delete(L1Clan clan) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM clan_joinning WHERE pledge_uid=?");
			pstm.setInt(1, clan.getClanId());
			if (pstm.executeUpdate() > 0) {
				clan.clearJonningList();
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	public boolean delete(L1PcInstance pc) {
		return delete(pc.getId());
	}
	
	public boolean delete(int user_uid) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM clan_joinning WHERE user_uid=?");
			pstm.setInt(1, user_uid);
			if (pstm.executeUpdate() > 0) {
				for (L1Clan clan : L1World.getInstance().getAllClans()) {
					clan.removeJoinningList(user_uid);
				}
				return true;
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
		return false;
	}
	
	public boolean cancel(L1Clan clan, int user_uid) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM clan_joinning WHERE pledge_uid=? AND user_uid=?");
			pstm.setInt(1, clan.getClanId());
			pstm.setInt(2, user_uid);
			if (pstm.executeUpdate() > 0) {
				clan.removeJoinningList(user_uid);
				return true;
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
		return false;
	}
	
	public boolean cancel(L1Clan clan, String user_name) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM clan_joinning WHERE pledge_uid=? AND user_name=?");
			pstm.setInt(1, clan.getClanId());
			pstm.setString(2, user_name);
			if (pstm.executeUpdate() > 0) {
				clan.removeJoinningList(user_name);
				return true;
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
		return false;
	}

}

