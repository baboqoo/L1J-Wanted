package l1j.server.QuestSystem.Loader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.QuestSystem.MonsterBook;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.SQLUtil;

public class MonsterBookLoader {
	private static Logger _log = Logger.getLogger(MonsterBookLoader.class.getName());
	private static MonsterBookLoader _instance;
	public static MonsterBookLoader getInstance() {
    	if (_instance == null)
    		_instance = new MonsterBookLoader();
    	return _instance;
    }  
	
	public static void reload(){
		MonsterBookLoader tmp = _instance;
		_instance = new MonsterBookLoader();
		if (tmp != null){
			tmp.clear();
			tmp = null;
		}
	}
	
	/** 도감 정보를 담을 맵 **/
	private Map<Integer, MonsterBook> _books = new HashMap<Integer, MonsterBook>();
	
	/** 주간 퀘스트 별로 도감 퀘스트를 담을 맵 **/
	private Map<Integer, ArrayList<MonsterBook>> _weekCategory = new HashMap<Integer, ArrayList<MonsterBook>>(3);
	
	private MonsterBookLoader(){
		Connection con						= null;
		PreparedStatement pstm 				= null;
		ResultSet rs 						= null;
		MonsterBook book					= null;
		String record						= null;
		for (int i = 0; i < 3; i++)
			_weekCategory.put(i, new ArrayList<MonsterBook>(10));
		
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			StringBuilder sbQry = new StringBuilder();
			//sbQry.append("select * from ").append(MonsterBook._table);
			sbQry.append(MonsterBook._table);
			pstm = con.prepareStatement(sbQry.toString());
			rs = pstm.executeQuery();
			while (rs.next()) {
				record = "initializer";
				book = new MonsterBook();
				for(int i = 0; i < MonsterBook._records.length; i++){
					record = MonsterBook._records[i];
					book.set(i, rs.getInt(record));
				}
				_books.put(book.getBookId(), book);
				if (!(book.getWeekDifficulty() < 0 || book.getWeekDifficulty() > 2))
					_weekCategory.get(book.getWeekDifficulty()).add(book);
				
				L1Npc npc = NpcTable.getInstance().getTemplate(book.getNpcId());
				if (npc != null)
					npc.setBookId(book.getBookId());
			}
		}catch (Exception e){
			StringBuilder sb = new StringBuilder();
			sb.append("[ERROR - MonsterBookLoader]").append(record).append(" read error. \r\n").append(e.getLocalizedMessage());
			_log.log(Level.SEVERE, sb.toString(), e);
			System.out.println(sb.toString());
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	/** 입력받은 주간 난이도를 통해 몬스터 북을 반환한다. **/
	public MonsterBook getWeekDiffToMonsterBook(int difficulty){
		ArrayList<MonsterBook> list = _weekCategory.get(difficulty);
		if (list == null)
			return null;
		
		return list.get(CommonUtil.random(list.size()));
	}
	
	/** bookid에 따른 몬스터 북을 반환한다. **/
	public MonsterBook getTemplate(int id){
		if (_books != null)
			return _books.get(id);
		return null;
	}
	
	public void clear(){
		_books.clear();
	}
}

