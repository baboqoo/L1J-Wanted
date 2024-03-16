package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillTimer;
import l1j.server.server.utils.SQLUtil;

/**
 * 캐릭터 버프 정보
 * @author LinOffice
 */
public class CharBuffTable {
	private static Logger _log = Logger.getLogger(CharBuffTable.class.getName());
	
	private static class newInstance {
		public static final CharBuffTable INSTANCE = new CharBuffTable();
	}
	public static CharBuffTable getInstance(){
		return newInstance.INSTANCE;
	}
	private CharBuffTable() {}
	
	public class BuffInfo {
		private int skillId, remainTime, polyId;
		public BuffInfo(ResultSet rs) throws SQLException {
			this.skillId	= rs.getInt("skill_id");
			this.remainTime	= rs.getInt("remaining_time");
			this.polyId		= rs.getInt("poly_id");
		}
		public int getSkillId() {
			return skillId;
		}
		public int getRemainTime() {
			return remainTime;
		}
		public int getPolyId() {
			return polyId;
		}
	}
	
	public List<BuffInfo> loadBuff(L1PcInstance pc) {
		List<BuffInfo> buffList	= new ArrayList<BuffInfo>();
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT * FROM character_buff WHERE char_obj_id=?");
			pstm.setInt(1, pc.getId());
			rs		= pstm.executeQuery();
			while(rs.next()){
				buffList.add(new BuffInfo(rs));
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return buffList;
	}
	
	// 케릭터 logout시 db업데이트
	public void storeBuff(L1PcInstance pc) {
		Connection con			= null;
		PreparedStatement pstm	= null;
		ArrayList<L1SkillTimer> skillTimerList = pc.getSkill().getSaveSkillTimerList();// 저장 시킬 스킬들
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			
			pstm = con.prepareStatement("DELETE FROM character_buff WHERE char_obj_id=?");
			pstm.setInt(1, pc.getId());
			pstm.execute();
			SQLUtil.close(pstm);
			
			if (skillTimerList.isEmpty()) {
				return;// 저장할 스킬들이 없다면 중단
			}
			con.setAutoCommit(false);
			pstm = con.prepareStatement("INSERT INTO character_buff SET char_obj_id=?, skill_id=?, remaining_time=?, poly_id=?");
			for (L1SkillTimer timer : skillTimerList) {
				pstm.setInt(1, pc.getId());
				pstm.setInt(2, timer.getSkillId());
				pstm.setInt(3, timer.getRemainTime());
				pstm.setInt(4, timer.getSkillId() == L1SkillId.SHAPE_CHANGE || timer.getSkillId() == L1SkillId.SHAPE_CHANGE_DOMINATION || timer.getSkillId() == L1SkillId.SHAPE_CHANGE_100LEVEL ? pc.getSpriteId() : 0);
				pstm.addBatch();
				pstm.clearParameters();
			}
			pstm.executeBatch();
			pstm.clearBatch();
			con.commit();
		} catch (SQLException e) {
			try {
				con.rollback();
			} catch(SQLException sqle){
				_log.log(Level.SEVERE, sqle.getLocalizedMessage(), sqle);
			}
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (Exception e) {
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
			if (skillTimerList != null) {
				skillTimerList.clear();
				skillTimerList = null;
			}
		}
	}
}

