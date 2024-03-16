package l1j.server.GameSystem.freebuffshield;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.common.bin.PCMasterCommonBinLoader;
import l1j.server.common.bin.pcmaster.PCMasterInfoForClient;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.system.DISABLE_FREE_BUFF_SHIELD;
import l1j.server.server.serverpackets.system.FREE_BUFF_SHIELD_INFO;
import l1j.server.server.utils.SQLUtil;

/**
 * 가호(버프) 데이터 로더
 * @author LinOffice
 */
public class FreeBuffShieldLoader {
	private static Logger _log = Logger.getLogger(FreeBuffShieldLoader.class.getName());
	private static FreeBuffShieldLoader _instance;
	public static FreeBuffShieldLoader getInstance() {
		if (_instance == null) {
			_instance = new FreeBuffShieldLoader();
		}
		return _instance;
	}
	
	/**
	 * 싱글톤
	 */
	private FreeBuffShieldLoader() {	
	}
	
	/**
	 * DB 데이터를 가져온다
	 * 데이터가 없으면 생성한다.
	 * @param pc
	 * @return PCPlayMaster
	 */
	public FreeBuffShieldHandler load(L1PcInstance pc) {
		Connection con				= null;
		PreparedStatement pstm		= null;
		ResultSet rs				= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT * FROM accounts_free_buff_shield WHERE account_name = ? LIMIT 1");
			pstm.setString(1, pc.getAccountName());
			rs		= pstm.executeQuery();
			if (rs.next()) {
				// 취득
				FreeBuffShieldHandler handler = new FreeBuffShieldHandler(pc, rs);
				handler.set_golden_buff_infos(loadGolden(con, pc));
				return handler;
			}
		} catch(SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch(Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		// 생성
		return new FreeBuffShieldHandler(pc);
	}
	
	/**
	 * DB 데이터를 가져온다(금빛 버프)
	 * @param con
	 * @param pc
	 * @return java.util.LinkedList<GoldenBuffInfo>
	 */
	java.util.LinkedList<GoldenBuffInfo> loadGolden(Connection con, L1PcInstance pc) {
		java.util.LinkedList<PCMasterInfoForClient.BuffBonusT> bonus = PCMasterCommonBinLoader.getData().get_buff_bonus();
		java.util.LinkedList<GoldenBuffInfo> list = null;
		GoldenBuffInfo info			= null;
		PreparedStatement pstm		= null;
		ResultSet rs				= null;
		try {
			pstm	= con.prepareStatement("SELECT * FROM accounts_pcmaster_golden WHERE account_name = ? ORDER BY index_id ASC LIMIT ?");
			pstm.setString(1, pc.getAccountName());
			pstm.setInt(2, bonus.size());
			rs		= pstm.executeQuery();
			while (rs.next()) {
				// 취득
				int index = rs.getInt("index_id");
				info = new GoldenBuffInfo(pc, index, rs.getInt("type"), 
						parseLoadeGrade(rs.getBytes("grade")), 
						rs.getInt("remain_time"), bonus.get(index));
				if (list == null) {
					list = new java.util.LinkedList<GoldenBuffInfo>();
				}
				list.add(info);
			}
			if (list == null) {
				list = new java.util.LinkedList<GoldenBuffInfo>();
				for (int i=0; i<bonus.size(); i++) {
					list.add(new GoldenBuffInfo(pc, i, GoldenBuffInfo.DEFAULT_TYPE, bonus.get(i)));
				}
			}
		} catch(SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch(Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm);
		}
		return list;
	}
	
	private static final String UPSERT_QUERY = "INSERT INTO accounts_free_buff_shield SET account_name=?, "
			+ "favor_locked_time=?, "
			+ "pccafe_favor_remain_count=?, free_favor_remain_count=?, event_favor_remain_count=?, "
			+ "pccafe_reward_item_count=?, reset_time=? "
			+ "ON DUPLICATE KEY UPDATE "
			+ "favor_locked_time=?, "
			+ "pccafe_favor_remain_count=?, free_favor_remain_count=?, event_favor_remain_count=?, "
			+ "pccafe_reward_item_count=?, reset_time=?";
	
	/**
	 * DB 데이터 적재(로그아웃시 데이터 처리)
	 * 이미 등록되어 있으면 기본키 기준으로 업데이트한다(동시 진행)
	 * @param handler
	 */
	public void upsert(FreeBuffShieldHandler handler) {
		if (handler == null) {
			return;
		}
		handler.stop_favor_locked_timer();
		
		DISABLE_FREE_BUFF_SHIELD disable_state	= handler._disable_state;
		FREE_BUFF_SHIELD_INFO pccafe_info = null, free_info = null, event_info = null;
		
		for (FREE_BUFF_SHIELD_INFO info : handler.get_free_buff_shield_info()) {
			switch (info.get_favor_type()) {
			case PC_CAFE_SHIELD:
				pccafe_info	= info;
				break;
			case FREE_BUFF_SHIELD:
				free_info	= info;
				break;
			case EVENT_BUFF_SHIELD:
				event_info	= info;
				break;
			}
		}
		
		int favor_locked_time		= disable_state == null ? 0 : disable_state.get_favor_locked_time();
		int pccafe_remain_count		= pccafe_info == null ? 0 : pccafe_info.get_favor_remain_count();
		int free_remain_count		= free_info == null ? 0 : free_info.get_favor_remain_count();
		int event_remain_count		= event_info == null ? 0 : event_info.get_favor_remain_count();
		
		Connection con				= null;
		PreparedStatement pstm		= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			
			if (!upsertGolden(con, handler._owner.getAccountName(), handler._golden_buff_infos)) {
				return;
			}
			
			pstm	= con.prepareStatement(UPSERT_QUERY);
			int index = 0;
			pstm.setString(++index, handler._owner.getAccountName());
			
			pstm.setInt(++index, favor_locked_time);
			pstm.setInt(++index, pccafe_remain_count);
			pstm.setInt(++index, free_remain_count);
			pstm.setInt(++index, event_remain_count);
			pstm.setInt(++index, handler._pccafe_reward_item_count);
			pstm.setTimestamp(++index, handler._reset_time);
			
			pstm.setInt(++index, favor_locked_time);
			pstm.setInt(++index, pccafe_remain_count);
			pstm.setInt(++index, free_remain_count);
			pstm.setInt(++index, event_remain_count);
			pstm.setInt(++index, handler._pccafe_reward_item_count);
			pstm.setTimestamp(++index, handler._reset_time);
			
			pstm.execute();
		} catch(SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch(Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	private static final String UPSERT_GOLDEN_QUERY = "INSERT INTO accounts_pcmaster_golden SET account_name=?, "
			+ "index_id=?, type=?, grade=?, remain_time=? "
			+ "ON DUPLICATE KEY UPDATE "
			+ "type=?, grade=?, remain_time=?";
	
	/**
	 * DB 금빛 버프 데이터 적재(로그아웃시 데이터 처리)
	 * @param con
	 * @param account_name
	 * @param infos
	 * @return boolean
	 */
	boolean upsertGolden(Connection con, String account_name, java.util.LinkedList<GoldenBuffInfo> infos) {
		PreparedStatement pstm	= null;
		try {
			con.setAutoCommit(false);
			pstm	= con.prepareStatement(UPSERT_GOLDEN_QUERY);
			for (GoldenBuffInfo info : infos) {
				info.disableTimer();
				byte[] grade = parseInsertGrade(info.grade);
				int index = 0;
				pstm.setString(++index, account_name);
				pstm.setInt(++index, info.index);
				pstm.setInt(++index, info.type);
				pstm.setBytes(++index, grade);
				pstm.setInt(++index, info.remain_time);
				pstm.setInt(++index, info.type);
				pstm.setBytes(++index, grade);
				pstm.setInt(++index, info.remain_time);
				pstm.addBatch();
				pstm.clearParameters();
			}
			pstm.executeBatch();
			pstm.clearBatch();
			con.commit();
			return true;
		} catch(SQLException e) {
			try {
				con.rollback();
			} catch(SQLException sqle){
				_log.log(Level.SEVERE, sqle.getLocalizedMessage(), sqle);
			}
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			return false;
		} catch(Exception e) {
			try {
				con.rollback();
			} catch(SQLException sqle){
				_log.log(Level.SEVERE, sqle.getLocalizedMessage(), sqle);
			}
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			return false;
		} finally {
			try {
				con.setAutoCommit(true);
			} catch (SQLException e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
			SQLUtil.close(pstm);
		}
	}
	
	/**
	 * 보너스 단계 파싱
	 * @param first
	 * @param second
	 * @param third
	 * @return java.util.LinkedList<Integer>
	 */
	protected static java.util.LinkedList<Integer> parseLoadeGrade(byte[] array) {
		java.util.LinkedList<Integer> result = new java.util.LinkedList<Integer>();
		for (byte val : array) {
			result.add(val & 0xFF);
		}
		return result;
	}
	
	/**
	 * 보너스 단계 파싱
	 * @param grade
	 * @return byte[]
	 */
	protected static byte[] parseInsertGrade(java.util.LinkedList<Integer> grade) {
		byte[] result = new byte[grade.size()];
		for (int i=0; i<result.length; i++) {
			result[i] = (byte)((int)grade.get(i));
		}
		return result;
	}
}

