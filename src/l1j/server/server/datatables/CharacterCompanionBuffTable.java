package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.companion.S_CompanionBuffNoti;
import l1j.server.server.utils.SQLUtil;

/**
 * 캐릭터 펫 버프 정보
 * @author LinOffice
 */
public class CharacterCompanionBuffTable {
	private static Logger _log = Logger.getLogger(CharacterCompanionBuffTable.class.getName());
	
	public static final int[] PET_SKILLS = { 
		L1SkillId.PET_BUFF_GROW, L1SkillId.PET_BUFF_EIN, L1SkillId.PET_BUFF_SKY, L1SkillId.PET_BUFF_YEGABAM, L1SkillId.PET_BUFF_BLOOD
	};

	public static void SaveBuff(L1PetInstance companion) {
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM character_companion_buff WHERE objid=?");
			pstm.setInt(1, companion.getId());
			pstm.executeUpdate();
			SQLUtil.close(pstm);
			
			java.util.LinkedHashMap<Integer, Integer> save_map = null;
			for (int skillId : PET_SKILLS) {
				int timeSec = companion.getSkill().getSkillEffectTimeSec(skillId);
				if (timeSec > 0) {
					if (save_map == null) {
						save_map = new java.util.LinkedHashMap<Integer, Integer>();
					}
					save_map.put(skillId, timeSec);
				}
			}
			if (save_map == null) {
				return;
			}
			con.setAutoCommit(false);
			pstm = con.prepareStatement("INSERT INTO character_companion_buff SET objid=?, buff_id=?, duration=?");
			for (Map.Entry<Integer, Integer> entry : save_map.entrySet()) {
				pstm.setInt(1, companion.getId());
				pstm.setInt(2, entry.getKey());
				pstm.setInt(3, entry.getValue());
				
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
		}
	}

	public static void LoadBuff(L1PetInstance companion) {
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT buff_id, duration FROM character_companion_buff WHERE objid=?");
			pstm.setInt(1, companion.getId());
			rs = pstm.executeQuery();
			java.util.LinkedList<S_CompanionBuffNoti.Buff> buff_list = null;
			while(rs.next()){
				if (buff_list == null) {
					buff_list = new java.util.LinkedList<S_CompanionBuffNoti.Buff>();
				}
				int buff_id = rs.getInt("buff_id"), duration = rs.getInt("duration");
				companion.getSkill().setSkillEffect(buff_id, duration * 1000);
				buff_list.add(new S_CompanionBuffNoti.Buff(buff_id, duration));
			}
			
			if (buff_list != null) {
				L1PcInstance pc = (L1PcInstance) companion.getMaster();
				pc.sendPackets(new S_CompanionBuffNoti(buff_list), true);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public static void deleteBuff(int objId) {
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM character_companion_buff WHERE objid=?");
			pstm.setInt(1, objId);
			pstm.executeUpdate();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
}

