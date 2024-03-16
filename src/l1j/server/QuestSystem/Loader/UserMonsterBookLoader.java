package l1j.server.QuestSystem.Loader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.QuestSystem.UserMonsterBook;
import l1j.server.QuestSystem.Templates.UserMonsterBookProgress;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.SQLUtil;

public class UserMonsterBookLoader {
	private static Logger _log = Logger.getLogger(UserMonsterBookLoader.class.getName());
	
	public static void load(L1PcInstance pc){
		UserMonsterBook mb 					= new UserMonsterBook(pc);
		UserMonsterBookProgress progress	= null;
		Connection con						= null;
		PreparedStatement pstm 				= null;
		ResultSet rs 						= null;
		try {
			con 	= L1DatabaseFactory.getInstance().getConnection();
			pstm 	= con.prepareStatement("select * from tb_user_monster_book where char_id=?");
			pstm.setInt(1, pc.getId());
			rs 		= pstm.executeQuery();
			while (rs.next()) {
				progress = new UserMonsterBookProgress(rs.getInt("book_id"), rs.getInt("difficulty"), rs.getInt("step"), rs.getInt("completed"));
				mb.setMonsterBook(progress.getBookId(), progress);
			}
			pc.getQuest().setMonsterBook(mb);
		} catch (Exception e){
			StringBuilder sb = new StringBuilder();
			sb.append("[ERROR - UserMonsterBookLoader] Load()...").append(pc.getId()).append(" read error. \r\n").append(e.getLocalizedMessage());
			_log.log(Level.SEVERE, sb.toString(), e);
			System.out.println(sb.toString());
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public static void store(L1PcInstance pc){
		if (pc == null || pc.getQuest().getMonsterBook() == null)
			return;
		
		Connection con							= null;
		PreparedStatement pstm 					= null;
		UserMonsterBookProgress	progress		= null;
		ArrayList<UserMonsterBookProgress> list = pc.getQuest().getMonsterBook().getProgressList();
		int size 								= list.size();
		
		try {
			con 	= L1DatabaseFactory.getInstance().getConnection();
			con.setAutoCommit(false);
			pstm 	= con.prepareStatement("insert into tb_user_monster_book set char_id=?, book_id=?, difficulty=?, step=?, completed=? on duplicate key update difficulty=?, step=?, completed=?");
			int idx = 0;
			for (int i=0; i < size; i++){
				idx = 0;
				progress = list.get(i);
				pstm.setInt(++idx, pc.getId());
				pstm.setInt(++idx, progress.getBookId());
				pstm.setInt(++idx, progress.getLevel());
				pstm.setInt(++idx, progress.getStep());
				pstm.setInt(++idx, progress.getCompleted());
				pstm.setInt(++idx, progress.getLevel());
				pstm.setInt(++idx, progress.getStep());
				pstm.setInt(++idx, progress.getCompleted());
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
			sb.append("[ERROR - UserMonsterBookLoader] store()...").append(pc.getId()).append(" read error. \r\n").append(e.getLocalizedMessage());
			_log.log(Level.SEVERE, sb.toString(), e);
			System.out.println(sb.toString());
			e.printStackTrace();
		} finally {
			try {
				con.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	public static void delete(L1PcInstance pc) {
		java.sql.Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = L1DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement("DELETE FROM tb_user_monster_book WHERE char_id=?");
            pstm.setInt(1, pc.getId());
            pstm.execute();
        } catch (SQLException e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }
	
}

