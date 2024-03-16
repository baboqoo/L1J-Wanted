package l1j.server.QuestSystem.Loader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.QuestSystem.UserWeekQuest;
import l1j.server.QuestSystem.Templates.UserWeekQuestProgress;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.SQLUtil;

public class UserWeekQuestLoader {
	private static Logger _log = Logger.getLogger(UserWeekQuestLoader.class.getName());
	
	public static void load(L1PcInstance pc){
		UserWeekQuest wq 					= new UserWeekQuest(pc);
		Connection con						= null;
		PreparedStatement pstm 				= null;
		ResultSet rs 						= null;
		try {
			con 	= L1DatabaseFactory.getInstance().getConnection();
			pstm 	= con.prepareStatement("select * from tb_user_week_quest where char_id=?");
			pstm.setInt(1, pc.getId());
			rs 		= pstm.executeQuery();
			wq.setWeekQuestInformation(rs);
			pc.getQuest().setWeekQuest(wq);
			wq.sendList();
		} catch (Exception e){
			StringBuilder sb = new StringBuilder();
			sb.append("[ERROR - UserWeekQuestLoader] Load()...").append(pc.getId()).append(" read error. \r\n").append(e.getLocalizedMessage());
			_log.log(Level.SEVERE, sb.toString(), e);
			System.out.println(sb.toString());
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	public static void store(L1PcInstance pc){
		if (pc == null || pc.getQuest().getWeekQuest() == null)
			return;
		
		Connection con							= null;
		PreparedStatement pstm 					= null;
		UserWeekQuestProgress	progress		= null;
		ArrayList<UserWeekQuestProgress> list 	= pc.getQuest().getWeekQuest().getProgressList();
		int size 								= list.size();
		
		try {
			con 	= L1DatabaseFactory.getInstance().getConnection();
			con.setAutoCommit(false);
			pstm 	= con.prepareStatement("insert into tb_user_week_quest set char_id=?, bookId=?, difficulty=?, section=?, step=?, stamp=?, completed=? on duplicate key update bookId=?, section=?, step=?, stamp=?, completed=?");
			int idx = 0;
			for (int i = 0; i < size; i++){
				idx = 0;
				progress = list.get(i);
				pstm.setInt(++idx, pc.getId());
				pstm.setInt(++idx, 	progress.getBookId());
				pstm.setInt(++idx, 	progress.getDifficulty());
				pstm.setInt(++idx,	progress.getSection());
				pstm.setInt(++idx, 	progress.getStep());
				pstm.setTimestamp(++idx, progress.getStamp());
				pstm.setBoolean(++idx, progress.isCompleted());
				pstm.setInt(++idx,	progress.getBookId());
				pstm.setInt(++idx, 	progress.getSection());
				pstm.setInt(++idx, 	progress.getStep());
				pstm.setTimestamp(++idx, progress.getStamp());
				pstm.setBoolean(++idx, progress.isCompleted());
				pstm.addBatch();
				pstm.clearParameters();
			}
			pstm.executeBatch();
			pstm.clearBatch();
			con.commit();
		} catch (Exception e){
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			StringBuilder sb = new StringBuilder();
			sb.append("[ERROR - UserWeekQuestLoader] store()...").append(pc.getId()).append(" read error. \r\n").append(e.getLocalizedMessage());
			_log.log(Level.SEVERE, sb.toString(), e);
			System.out.println(sb.toString());
			e.printStackTrace();
		} finally {
			try {
				con.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			SQLUtil.close(pstm, con);
		}
	}
	
	public static void delete(L1PcInstance pc) {
		java.sql.Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = L1DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement("DELETE FROM tb_user_week_quest WHERE char_id=?");
            pstm.setInt(1, pc.getId());
            pstm.execute();
        } catch (SQLException e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
        	SQLUtil.close(pstm, con);
        }
    }
	
}

