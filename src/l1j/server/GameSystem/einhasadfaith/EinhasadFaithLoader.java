package l1j.server.GameSystem.einhasadfaith;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.GameSystem.einhasadfaith.bean.EinhasadFaithInfo;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.SQLUtil;

/**
 * 아인하사드의 신의 DB 로더
 * @author LinOffice
 */
public class EinhasadFaithLoader {
	private static Logger _log = Logger.getLogger(EinhasadFaithLoader.class.getName());
	private static EinhasadFaithLoader _instance;
	public static EinhasadFaithLoader getInstance() {
		if (_instance == null) {
			_instance = new EinhasadFaithLoader();
		}
		return _instance;
	}
	
	private EinhasadFaithLoader() {
	}
	
	public HashMap<Integer, EinhasadFaithInfo> load(int objId) {
		HashMap<Integer, EinhasadFaithInfo> result = null;
		EinhasadFaithInfo val		= null;
		Connection con				= null;
		PreparedStatement pstm		= null;
		ResultSet rs				= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT * FROM character_einhasadfaith WHERE objId=?");
			pstm.setInt(1, objId);
			rs		= pstm.executeQuery();
			while (rs.next()) {
				val = new EinhasadFaithInfo(rs);
				if (result == null) {
					result = new HashMap<>();
				}
				result.put(val.getIndexId(), val);
			}
		} catch(SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch(Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return result;
	}
	
	static final String UPSERT_QUERY = "INSERT INTO character_einhasadfaith SET "
			+ "objId=?, groupId=?, indexId=?, spellId=?, expiredTime=? "
			+ "ON DUPLICATE KEY UPDATE "
			+ "expiredTime=?";
	
	public void upsert(L1PcInstance pc, java.util.LinkedList<EinhasadFaithInfo> infos) {
		if (infos == null || infos.isEmpty()) {
			return;
		}
		Connection con				= null;
		PreparedStatement pstm		= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			con.setAutoCommit(false);
			
			pstm	= con.prepareStatement(UPSERT_QUERY);
			for (EinhasadFaithInfo info : infos) {
				if (info.getIndexId() == 0) {
					continue;
				}
				int index = 0;
				pstm.setInt(++index, pc.getId());
				pstm.setInt(++index, info.getGroupId());
				pstm.setInt(++index, info.getIndexId());
				pstm.setInt(++index, info.getSpellId());
				pstm.setTimestamp(++index, info.getExpiredTime());
				pstm.setTimestamp(++index, info.getExpiredTime());
				pstm.addBatch();
				pstm.clearParameters();
			}
 			
			pstm.executeBatch();
			pstm.clearBatch();
			con.commit();
		} catch(SQLException e) {
			try {
				con.rollback();
			} catch(SQLException sqle){
				_log.log(Level.SEVERE, sqle.getLocalizedMessage(), sqle);
			}
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch(Exception e) {
			try {
				con.rollback();
			} catch(SQLException sqle){
				_log.log(Level.SEVERE, sqle.getLocalizedMessage(), sqle);
			}
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			try {
				con.setAutoCommit(true);
			} catch (SQLException e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
			SQLUtil.close(pstm, con);
		}
	}
	
}

