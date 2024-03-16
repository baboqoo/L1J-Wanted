package l1j.server.QuestSystem.Loader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.QuestSystem.MonsterBook;
import l1j.server.QuestSystem.Templates.WeekQuestDateCalculator;
import l1j.server.server.utils.SQLUtil;

public class WeekQuestLoader {
	private static Logger _log = Logger.getLogger(WeekQuestLoader.class.getName());
	private static WeekQuestLoader _instance;
	public static WeekQuestLoader getInstance() {
    	if (_instance == null)
    		_instance = new WeekQuestLoader();
    	return _instance;
    }  
	
	public synchronized static void reload(){
		WeekQuestLoader tmp = _instance;
		_instance = new WeekQuestLoader();
		if (tmp != null){
			tmp.clear();
			tmp = null;
		}
	}
	
	private MonsterBook[][] _weekMatrix;

	private WeekQuestLoader(){
		load();
	}
	
	private void update(){
		if (_weekMatrix == null)
			return;
		
		Connection con							= null;
		PreparedStatement pstm 					= null;
		Timestamp stamp							= WeekQuestDateCalculator.getInstance().getUpdateStamp();
		try {
			con 	= L1DatabaseFactory.getInstance().getConnection();
			con.setAutoCommit(false);
			pstm 	= con.prepareStatement("insert into tb_weekquest_matrix set difficulty=?, col1=?, col2=?, col3=?, stamp=? on duplicate key update col1=?, col2=?, col3=?, stamp=?");
			int idx = 0;
			for (int i = 0; i < 3; i++){
				idx = 0;
				pstm.setInt(++idx, i);
				for (int j = 0; j < 3; j++)
					pstm.setInt(++idx, _weekMatrix[i][j].getBookId());
				
				pstm.setTimestamp(++idx, stamp);
				
				for (int j = 0; j < 3; j++)
					pstm.setInt(++idx, _weekMatrix[i][j].getBookId());
				pstm.setTimestamp(++idx, stamp);
				
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
			sb.append("[ERROR - WeekQuestLoader]").append(" read error. \r\n").append(e.getLocalizedMessage());
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
	
	private void load(){
		Connection con						= null;
		PreparedStatement pstm 				= null;
		ResultSet rs 						= null;
		MonsterBook book					= null;
		MonsterBookLoader loader			= MonsterBookLoader.getInstance();
		int bid								= 0;
		boolean isLoaded					= false;
		Timestamp stamp						= null;
		if (_weekMatrix != null){
			batchMatrix();
			return;
		}
		
		_weekMatrix							= new MonsterBook[3][3];
		
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("select * from tb_weekquest_matrix");
			rs = pstm.executeQuery();
			for (int i = 0; i < 3; i++){
				if (!rs.next()){
					isLoaded = false;
					break;
				}
				for (int j = 0; j < 3; j++){
					bid 				= rs.getInt(String.format("col%d", j+1));
					stamp				= rs.getTimestamp("stamp");
					book 				= loader.getTemplate(bid);
					_weekMatrix[i][j] 	= book;
				}
				if (stamp == null || WeekQuestDateCalculator.getInstance().isUpdateWeekQuest(stamp)){
					isLoaded = false;
					break;
				}
				if (i == 2)
					isLoaded = true;
			}
			
		} catch (Exception e){
			StringBuilder sb = new StringBuilder();
			sb.append("[ERROR - WeekQuestLoader]").append(" read error. \r\n").append(e.getLocalizedMessage());
			_log.log(Level.SEVERE, sb.toString(), e);
			System.out.println(sb.toString());
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		
		if(!isLoaded)
			batchMatrix();
	}
	
	public void batchMatrix(){
		MonsterBook[][] matrix 		= new MonsterBook[3][3];
		MonsterBookLoader loader	= MonsterBookLoader.getInstance();
		MonsterBook book			= null;
		try {
			for (int row = 0; row < 3; row++){
				for (int col = 0; col < 3; col++){
					// 만약 중복 북id가 할당되었다면, 다른 북 id를 받을때 까지 갱신한다.
					do {
						book = loader.getWeekDiffToMonsterBook(row);
					} while (checkDuplicateBookId(matrix, book, row, col));
					matrix[row][col] = book;
				}
			}
			_weekMatrix = matrix;
			update();
		} catch(Exception e){
			StringBuilder sb = new StringBuilder();
			sb.append("[ERROR - WeekQuestLoader]").append(" read error. \r\n").append(e.getLocalizedMessage());
			_log.log(Level.SEVERE, sb.toString(), e);
			System.out.println(sb.toString());
			e.printStackTrace();
		} 		
	}
	
	private boolean checkDuplicateBookId(MonsterBook[][] matrix, MonsterBook book, int difficulty, int section) throws Exception{
		if (difficulty < 0 || difficulty > 2)
			throw new Exception("invalid difficulty " + difficulty);
		
		for (int i = section - 1; i >= 0; i--){
			if (matrix[difficulty][i].getBookId() == book.getBookId())
				return true;
		}
		return false;
	}
	
	public MonsterBook getBook(int difficulty, int col){
		return _weekMatrix[difficulty][col];
	}
	
	public void clear(){
		_weekMatrix = null;
	}
}

