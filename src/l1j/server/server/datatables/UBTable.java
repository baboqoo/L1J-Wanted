package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.L1UltimateBattle;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.SQLUtil;

public class UBTable {
	private static Logger _log = Logger.getLogger(UBTable.class.getName());

	private static UBTable _instance = new UBTable();

	private HashMap<Integer, L1UltimateBattle> _ub = new HashMap<Integer, L1UltimateBattle>();

	public static UBTable getInstance() {
		return _instance;
	}

	private UBTable() {
		loadTable();
	}
	
	public static void reload() {
		synchronized (_instance) {
			UBTable oldInstance = _instance;
			_instance = new UBTable();
			oldInstance._ub.clear();
		}
	}

	private void loadTable() {

		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			try {
				pstm = con.prepareStatement("SELECT * FROM ub_settings");
				rs = pstm.executeQuery();
				L1UltimateBattle ub = null;
				while(rs.next()){
					ub = new L1UltimateBattle();
					ub.setUbId(rs.getInt("ub_id"));
					ub.setMapId(rs.getShort("ub_mapid"));
					ub.setLocX1(rs.getInt("ub_area_x1"));
					ub.setLocY1(rs.getInt("ub_area_y1"));
					ub.setLocX2(rs.getInt("ub_area_x2"));
					ub.setLocY2(rs.getInt("ub_area_y2"));
					ub.setMinLevel(rs.getInt("min_lvl"));
					ub.setMaxLevel(rs.getInt("max_lvl"));
					ub.setMaxPlayer(rs.getInt("max_player"));
					ub.setEnterRoyal(rs.getBoolean("enter_royal"));
					ub.setEnterKnight(rs.getBoolean("enter_knight"));
					ub.setEnterMage(rs.getBoolean("enter_mage"));
					ub.setEnterElf(rs.getBoolean("enter_elf"));
					ub.setEnterDarkelf(rs.getBoolean("enter_darkelf"));
					ub.setEnterDragonknight(rs.getBoolean("enter_dragonknight"));
					ub.setEnterIllusionist(rs.getBoolean("enter_illusionist"));
					ub.setEnterWarrior(rs.getBoolean("enter_Warrior"));
					ub.setEnterFencer(rs.getBoolean("enter_Fencer"));
					ub.setEnterLancer(rs.getBoolean("enter_Lancer"));
					ub.setEnterMale(rs.getBoolean("enter_male"));
					ub.setEnterFemale(rs.getBoolean("enter_female"));
					ub.setUsePot(rs.getBoolean("use_pot"));
					ub.setHpr(rs.getInt("hpr_bonus"));
					ub.setMpr(rs.getInt("mpr_bonus"));
					ub.resetLoc();
					_ub.put(ub.getUbId(), ub);
				}
			} catch (SQLException e) {
				_log.warning("ubsettings couldnt be initialized:" + e);
			} finally {
				SQLUtil.close(rs, pstm);
			}

			// ub_managers load
			try {
				pstm = con.prepareStatement("SELECT * FROM ub_managers");
				rs = pstm.executeQuery();
				L1UltimateBattle ub = null;
				while(rs.next()){
					ub = getUb(rs.getInt("ub_id"));
					if (ub != null) {
						ub.addManager(rs.getInt("ub_manager_npc_id"));
					}
				}
			} catch (SQLException e) {
				_log.warning("ub_managers couldnt be initialized:" + e);
			} finally {
				SQLUtil.close(rs, pstm);
			}

			// ub_times load
			try {
				pstm = con.prepareStatement("SELECT * FROM ub_times");
				rs = pstm.executeQuery();
				L1UltimateBattle ub = null;
				while(rs.next()){
					ub = getUb(rs.getInt("ub_id"));
					if (ub != null) {
						ub.addUbTime(rs.getInt("ub_time"));
					}
				}
			} catch (SQLException e) {
				_log.warning("ub_times couldnt be initialized:" + e);
			} finally {
				SQLUtil.close(rs, pstm);
			}
			
		} catch(Exception e){
			_log.warning("ub_load Exception " + e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		
		//_log.config("UB리스트 " + _ub.size() + "건 로드");
		_log.config("UB list " + _ub.size() + " entries loaded");
	}

	public L1UltimateBattle getUb(int ubId) {
		return _ub.get(ubId);
	}

	public Collection<L1UltimateBattle> getAllUb() {
		return Collections.unmodifiableCollection(_ub.values());
	}

	public L1UltimateBattle getUbForNpcId(int npcId) {
		for (L1UltimateBattle ub : _ub.values()) {
			if (ub.containsManager(npcId)) {
				return ub;
			}
		}
		return null;
	}

	/**
	 * 지정된 UBID에 대한 패턴의 최대수를 돌려준다.
	 * 
	 * @param ubId
	 *            조사하는 UBID.
	 * @return 패턴의 최대수.
	 */
	public int getMaxPattern(int ubId) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT MAX(pattern) FROM spawnlist_ub WHERE ub_id=?");
			pstm.setInt(1, ubId);
			rs = pstm.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return 0;
	}
	

	/**
	 * 디비에 UB의 랭킹등록
	 */
	public void writeUbScore(int ubId, L1PcInstance[] pcArray) {
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			con.setAutoCommit(false);
			pstm = con.prepareStatement("INSERT INTO ub_rank SET ub_id=?, char_name=?, score=? ON DUPLICATE KEY UPDATE score=(SELECT A.cnt FROM (SELECT score AS CNT FROM ub_rank WHERE ub_id=? AND char_name=?) A)+?");
			for (L1PcInstance pc : pcArray) {
				pstm.setInt(1, ubId);
				pstm.setString(2, pc.getName());
				pstm.setInt(3, pc.getUbScore());
				pstm.setInt(4, ubId);
				pstm.setString(5, pc.getName());
				pstm.setInt(6, pc.getUbScore());
				pstm.addBatch();
				pstm.clearParameters();
				pc.setUbScore(0);
			}
			pstm.executeBatch();
			pstm.clearBatch();
			con.commit();
		} catch (SQLException e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			try {
				con.setAutoCommit(true);
			} catch (SQLException e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
			SQLUtil.close(rs, pstm, con);
		}
	}
}
