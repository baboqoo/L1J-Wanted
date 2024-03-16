package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.Server;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.utils.SQLUtil;

public class LetterSpamTable {
	private static Logger _log = Logger.getLogger(LetterSpamTable.class.getName());
	private volatile static LetterSpamTable uniqueInstance = null;
	
	public LetterSpamTable() {
	}

	public static LetterSpamTable  getInstance() {
		if (uniqueInstance == null) {
			synchronized (Server.class) {
				if (uniqueInstance == null) {
					uniqueInstance = new LetterSpamTable();
				}
			}
		}
		return uniqueInstance;
	}

	/**
	 * 편지를 보낼 수 있는지 체크한다.
	 * @param senderName
	 * @param receiverName
	 * @return
	 */
	public boolean spamLetterCheck(String senderName, String receiverName) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM letter_spam WHERE name = ? AND spamname = ?");
			pstm.setString(1, receiverName);
			pstm.setString(2, senderName);
			rs = pstm.executeQuery();
			if (rs.next()) {
				return false;
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return true;
	}
	

	/**
	 * 편지 차단자를 저장한다.
	 * @param pcName 
	 * @param excludeName
	 */
	public void spamLetterAdd(L1PcInstance pc, String excludeName) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int no = 0;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT Max(no)+1 as cnt FROM letter_spam ORDER BY no");
			rs = pstm.executeQuery();
			if (rs.next()) {
				no = rs.getInt("cnt");
			}
			pstm.close();
			if (no >= 50) {
				pc.sendPackets(new S_ServerMessage(472), true); // 차단된 사용자가 너무 많습니다.
			} else {
				pstm = con.prepareStatement("INSERT INTO letter_spam SET no=?, name=?, spamname=?");
				pstm.setInt(1, no);
				pstm.setString(2, pc.getName());
				pstm.setString(3, excludeName);
				pstm.execute();
				//pc.sendPackets(new S_PacketBox(S_PacketBox.ADD_EXCLUDE, excludeName, 1), true);
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	/**
	 * 편지 차단자를 삭제한다.
	 * @param pcName
	 * @param excludeName
	 */
	public void spamLetterDel(L1PcInstance pc, String excludeName) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM letter_spam WHERE name = ? AND spamname = ?");
			pstm.setString(1, pc.getName());
			pstm.setString(2, excludeName);
			pstm.execute();
			//pc.sendPackets(new S_PacketBox(S_PacketBox.REM_EXCLUDE, excludeName, 1), true);
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	/**
	 * 내 차단목록에 상대방이 있는지 체크한다.
	 * @param PcName
	 * @param spamname
	 * @return
	 */
	public boolean spamList(String PcName, String spamname) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		boolean result = false;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM letter_spam WHERE name = '" + PcName + "' AND spamname = '" + spamname + "'");
			rs = pstm.executeQuery();
			if (rs.next()) {
				result = true;
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return result;
	}
	
	
	/**
	 * 월드 접속시에 불러와서 S패킷을 날려준다.
	 * @param pcName
	 * @param spamname
	 */
	public void loadSpamList(L1PcInstance pc) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		//String spamname = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM letter_spam WHERE name = '" + pc.getName() + "'");
			rs = pstm.executeQuery();
			while(rs.next()){
				//spamname = rs.getString("spamname");
				//pc.sendPackets(new S_PacketBox(S_PacketBox.ADD_EXCLUDE, spamname, 1), true);
			}
		} catch(SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
}

