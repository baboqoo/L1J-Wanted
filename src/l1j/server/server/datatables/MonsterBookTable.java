package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

public class MonsterBookTable {
	
	private class Mbl {
		private HashMap<Integer, Integer> _monlist = new HashMap<Integer, Integer>();
		private HashMap<Integer, Integer> _monquest = new HashMap<Integer, Integer>();	
	}
	
	public HashMap<Integer, Integer> getMonBookList(int id) {
		Mbl mbl = _monsterBookLists.get(id);
		if(mbl == null)return null;
		return mbl._monlist;
	}
	
	public HashMap<Integer, Integer> getMonQuest(int id) {
		Mbl mbl = _monsterBookLists.get(id);
		if(mbl == null)return null;
		return mbl._monquest;
	}
	
	public static void reload() {
		MonsterBookTable oldInstance = _instance;
		_instance = new MonsterBookTable();
		oldInstance._monsterBookLists.clear();
	}
	
	private class Mblt {
		private int _monNum = 0;
		private int _monNpcid= 0;
		private int _locX= 0;
		private int _locY= 0;
		private int _mapId= 0;
		private int _typE= 0;
		private int _marteriaL= 0;
		private int _bookStepFirst = 0;
		private int _bookStepSecond = 0;
		private int _bookStepThird = 0;
	}

	public int getMonNum(int num) {
		Mblt mblt = _monBookTellList.get(num);
		if(mblt == null)return 0;
		return mblt._monNum;
	}
	public int getMonsterId(int num) {
		Mblt mblt = _monBookTellList.get(num);
		if(mblt == null)return 0;
		return mblt._monNpcid;
	}
	public int getLocX(int num) {
		Mblt mblt = _monBookTellList.get(num);
		if(mblt == null)return 0;
		return mblt._locX;
	}
	public int getLocY(int num) {
		Mblt mblt = _monBookTellList.get(num);
		if(mblt == null)return 0;
		return mblt._locY;
	}
	public int getMapId(int num) {
		Mblt mblt = _monBookTellList.get(num);
		if(mblt == null)return 0;
		return mblt._mapId;
	}
	public int getType(int num) {
		Mblt mblt = _monBookTellList.get(num);
		if(mblt == null)return 0;
		return mblt._typE;
	}
	public int getMarterial(int num) {
		Mblt mblt = _monBookTellList.get(num);
		if(mblt == null)return 0;
		return mblt._marteriaL;
	}
	
	public int getQuest1(int num) {
		Mblt mblt = _monBookTellList.get(num);
		if(mblt == null)return 0;
		return mblt._bookStepFirst;
	}
	
	public int getQuest2(int num) {
		Mblt mblt = _monBookTellList.get(num);
		if(mblt == null)return 0;
		return mblt._bookStepSecond;
  }
	
	public int getQuest3(int num) {
		Mblt mblt = _monBookTellList.get(num);
		if(mblt == null)return 0;
		return mblt._bookStepThird;
 }
	
	public void addMonCounter(int id, int num) {
		Mbl mbl = _monsterBookLists.get(id);
		if(mbl._monlist.get(num) != null){
			int mon = mbl._monlist.get(num);
			if(mon > 100000)
				mon = 100000;
			mbl._monlist.put(num, ++mon);
		}else//최초등록.
			mbl._monlist.put(num, 1); 			
	}
	public int getMonConter(int id, int num) {
		Mbl mbl = _monsterBookLists.get(id);		
		return mbl._monlist.get(num);
	}
	
	public void setMonQuest(int id, int quest, int value) {
		Mbl mbl = _monsterBookLists.get(id);
		mbl._monquest.put(quest, value);
	}
	
	public int getMonQuest(int id, int quest) {
		Mbl mbl = _monsterBookLists.get(id);
		if(mbl._monquest.get(id) == null)return 0;
		return mbl._monquest.get(quest);
	}
	
	private static Logger _log = Logger.getLogger(MonsterBookTable.class.getName());
	private static MonsterBookTable _instance;
	
	private ConcurrentHashMap<Integer, Mbl> _monsterBookLists = new ConcurrentHashMap<Integer, Mbl>();
	private ConcurrentHashMap<Integer, Mblt> _monBookTellList = new ConcurrentHashMap<Integer, Mblt>();
	private ConcurrentHashMap<Integer, Integer> _monsterList = new ConcurrentHashMap<Integer, Integer>();
	
	public int getMonsterList(int npcid) {
		Integer result = _monsterList.get(npcid);
		if(result == null)return 0;
		return result;
	}
	
	
	public static MonsterBookTable getInstace() {
		if(_instance == null)_instance = new MonsterBookTable();
		return _instance;
	}
	
	private MonsterBookTable() {
		loadCharacterList();
		loadMonsterBookList();
	}
	
	public void loadCharacterList() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM character_monsterbooklist");
			rs = pstm.executeQuery();
			Mbl mbl = null;
			while(rs.next()){
				mbl = new Mbl();
				int id = rs.getInt("id");			
				StringTokenizer ml = new StringTokenizer(rs.getString("monsterlist"), "|");
				while(ml.hasMoreTokens()){
					String monster = ml.nextToken();
					StringTokenizer ml1 = new StringTokenizer(monster, StringUtil.CommaString);
					while(ml1.hasMoreTokens()){
						int monsterNumber = Integer.parseInt(ml1.nextToken(), 10);
						int monsterKillCount = Integer.parseInt(ml1.nextToken(), 10);
						mbl._monlist.put(monsterNumber, monsterKillCount);
					}
				}
				StringTokenizer mlq = new StringTokenizer(rs.getString("monquest"), "|");
				while(mlq.hasMoreTokens()){
					String monquest = mlq.nextToken();
					StringTokenizer ml2 = new StringTokenizer(monquest, StringUtil.CommaString);
					while(ml2.hasMoreTokens()){
						int questNum = Integer.parseInt(ml2.nextToken(), 10);
						int value = Integer.parseInt(ml2.nextToken(), 10);
						mbl._monquest.put(questNum, value);
					}
				}
				_monsterBookLists.put(id, mbl);			
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	
	private void loadMonsterBookList() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM monster_book");
			rs = pstm.executeQuery();
			Mblt mblt = null;
			while(rs.next()){
				mblt = new Mblt();
				int monsternumber = rs.getInt("monsternumber");
				mblt._monNum = monsternumber;
				int monsterid = rs.getInt("monster_id");
				mblt._monNpcid = monsterid;
				mblt._locX = rs.getInt("locx");
				mblt._locY = rs.getInt("locy");
				mblt._mapId = rs.getInt("mapid");
				mblt._typE = rs.getInt("type");
				mblt._marteriaL = rs.getInt("marterial");
				mblt._bookStepFirst = rs.getInt("book_step_first");
				mblt._bookStepSecond = rs.getInt("book_step_second");
				mblt._bookStepThird = rs.getInt("book_step_third");
				_monsterList.put(monsterid, monsternumber);
				_monBookTellList.put(monsternumber, mblt);
				
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public void createMonsterBookList(int id) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO character_monsterbooklist SET id=?, monsterList=?, monquest=?");
			Mbl mbl = new Mbl();
			pstm.setInt(1, id);
			pstm.setString(2, StringUtil.EmptyString);
			pstm.setString(3, StringUtil.EmptyString);
			pstm.execute();
			_monsterBookLists.put(id, mbl);			
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	public void deleteMonsterBookList(int id) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM character_monsterbooklist WHERE id=?");
			pstm.setInt(1, id);		
			pstm.execute();
			_monsterBookLists.remove(id);			
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	public void saveMonsterBookList(int id) {
		StringBuffer monsterlist = new StringBuffer();
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE character_monsterbooklist SET monsterList=? WHERE id=?");
			HashMap<Integer, Integer> mbl = getMonBookList(id);
			if(mbl != null){
				TreeMap<Integer, Integer> tree = new TreeMap<Integer, Integer>(mbl);
				Iterator<Integer> iter = tree.keySet().iterator();
				while(iter.hasNext()){
					int monstertnumber = iter.next();
					int monsterkillcount = mbl.get(monstertnumber);
					monsterlist.append(monstertnumber + StringUtil.CommaString + monsterkillcount + "|");
				}
			}
			pstm.setString(1, monsterlist.toString());
			pstm.setInt(2, id);
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	public void saveMonsterQuest(int id) {
		StringBuffer monsterlist = new StringBuffer();
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE character_monsterbooklist SET monquest=? WHERE id=?");
			HashMap<Integer, Integer> mbl = getMonQuest(id);
			if(mbl != null){
				TreeMap<Integer, Integer> tree = new TreeMap<Integer, Integer>(mbl);
				Iterator<Integer> iter = tree.keySet().iterator();
				while(iter.hasNext()){
					int questNum = iter.next();
					int value = mbl.get(questNum);
					monsterlist.append(questNum + StringUtil.CommaString + value + "|");
				}
			}
			pstm.setString(1, monsterlist.toString());
			pstm.setInt(2, id);
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
}

