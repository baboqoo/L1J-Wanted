package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastMap;
import javolution.util.FastTable;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.templates.L1Notification;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

/**
 * 보스 스폰 데이터 로더
 * @author LinOffice
 */
public class BossSpawnTable {
	private static Logger _log = Logger.getLogger(BossSpawnTable.class.getName());
	
	private static BossSpawnTable _instance;
	public static BossSpawnTable getInstance() {
		if (_instance == null) {
			_instance = new BossSpawnTable();
		}
		return _instance;
	}

	private static final ArrayList<BossTemp> LIST = new ArrayList<BossTemp>();
	public static ArrayList<BossTemp> getlist() {
		return LIST;
	}
	
	public static BossTemp getBossInfo(int npcId){
		for (BossTemp temp : LIST) {
			if (temp.npcid == npcId) {
				return temp;
			}
		}
		return null;
	}
	
	public static enum BossMentType {
		NONE, WORLD, MAP, SCREEN;
		private static BossMentType fromString(String str){
			switch(str){
			case "MAP":		return MAP;
			case "WORLD":	return WORLD;
			case "SCREEN":	return SCREEN;
			default:		return NONE;
			}
		}
	}
	
	public static enum SpawnType {
		NORMAL, DOMINATION_TOWER, DRAGON_RAID, POISON_FEILD;
		private static SpawnType fromString(String str){
			switch(str){
			case "DOMINATION_TOWER":return DOMINATION_TOWER;
			case "DRAGON_RAID":		return DRAGON_RAID;
			case "POISON_FEILD":	return POISON_FEILD;
			default:				return NORMAL;
			}
		}
	}
	
	public static class BossTemp {
		public int npcid;
		public boolean isSpawn;
		public int[] spawnDay;
		public int[] spawnHour;
		public int[] spawnMinute;
		public int rndMinut;
		public int aliveSecond;
		public int[] spawnLoc;
		public int rndRange;
		public int heading;
		public int groupid;
		public int movementDistance;
		public boolean isYn;
		public BossMentType mentType;
		public String ment;
		public int percent;
		public FastMap<Integer, FastTable<BossSign>> signMap;
		public SpawnType spawnType;
		public L1Notification notification;
	}
	
	public static class BossSign {
		public int bossId;
		public int npcId;
		public int[] spawnLoc;
		public int rndRange;
		public int aliveSecond;
	}
	
	private BossSpawnTable() {
		Connection con			= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			bossLoad(con);
			bossSignLoad(con);
		} catch(SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(con);
		}
	}
	
	private void bossLoad(Connection con){
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		BossTemp temp			= null;
		try {
			pstm	= con.prepareStatement("SELECT * FROM spawnlist_boss");
			rs		= pstm.executeQuery();
			while(rs.next()){
				temp							= new BossTemp();
				temp.npcid						= rs.getInt("npcid");
				temp.spawnDay					= parseDay(rs.getString("spawnDay"));
				String spawnTime				= rs.getString("spawnTime");
				temp.spawnLoc					= new int[3];
				temp.spawnLoc[0]				= rs.getInt("spawnX");
				temp.spawnLoc[1]				= rs.getInt("spawnY");
				temp.spawnLoc[2]				= rs.getInt("spawnMapId");
				temp.rndMinut					= rs.getInt("rndMinut");
				temp.rndRange					= rs.getInt("rndRange");
				temp.heading					= rs.getInt("heading");
				temp.groupid					= rs.getInt("groupid");
				temp.movementDistance			= rs.getInt("movementDistance");
				temp.isYn						= Boolean.parseBoolean(rs.getString("isYN"));
				temp.mentType					= BossMentType.fromString(rs.getString("mentType"));
				temp.ment						= rs.getString("ment");
				temp.percent					= rs.getInt("percent");
				temp.aliveSecond				= rs.getInt("aliveSecond");
				temp.spawnType					= SpawnType.fromString(rs.getString("spawnType"));
				
				StringTokenizer st				= new StringTokenizer(spawnTime, StringUtil.LineString);
				ArrayList<Integer> Hourlist		= new ArrayList<Integer>();
				ArrayList<Integer> Minutelist	= new ArrayList<Integer>();
				while (st.hasMoreElements()) {
					String Times				= st.nextToken().trim();
					//StringTokenizer Hours		= new StringTokenizer(Times, "시");
					StringTokenizer Hours		= new StringTokenizer(Times, "H");
					String Hour					= Hours.nextToken();
					//StringTokenizer Minutes		= new StringTokenizer(Hours.nextToken(), "분");
					StringTokenizer Minutes		= new StringTokenizer(Hours.nextToken(), "M");
					String Minute				= Minutes.nextToken();
					Hourlist.add(Integer.parseInt(Hour.trim()));
					Minutelist.add(Integer.parseInt(Minute.trim()));
				}

				temp.spawnHour					= new int[Hourlist.size()];
				temp.spawnMinute				= new int[Hourlist.size()];
				for (int i = 0; i < Hourlist.size(); i++) {
					temp.spawnHour[i]			= Hourlist.get(i);
					temp.spawnMinute[i]			= Minutelist.get(i);
				}
				Hourlist.clear();
				Minutelist.clear();
				Hourlist = Minutelist = null;
				LIST.add(temp);
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm);
		}
	}
	
	private void bossSignLoad(Connection con){
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		BossSign sign			= null;
		try {
			pstm	= con.prepareStatement("SELECT * FROM spawnlist_boss_sign ORDER BY bossId ASC");
			rs		= pstm.executeQuery();
			while(rs.next()){
				int bossId			= rs.getInt("bossId");
				BossTemp boss		= getBossInfo(bossId);
				if (boss == null) {
					continue;
				}
				if (boss.signMap == null) {
					boss.signMap = new FastMap<Integer, FastTable<BossSign>>();
				}
				sign				= new BossSign();
				sign.bossId			= bossId;
				sign.npcId			= rs.getInt("npcId");
				sign.spawnLoc		= new int[3];
				sign.spawnLoc[0]	= rs.getInt("locX");
				sign.spawnLoc[1]	= rs.getInt("locY");
				sign.spawnLoc[2]	= rs.getInt("locMapId");
				sign.rndRange		= rs.getInt("rndRange");
				sign.aliveSecond	= rs.getInt("aliveSecond");
				
				FastTable<BossSign> list = boss.signMap.get(sign.spawnLoc[2]);
				if (list == null) {
					list = new FastTable<BossSign>();
					boss.signMap.put(sign.spawnLoc[2], list);
				}
				list.add(sign);
			}
		} catch(SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch(Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm);
		}
	}
	
	int[] parseDay(String str) {
		String[] array = str.trim().split(StringUtil.CommaString);
		int[] result = new int[array.length];
		for (int i=0; i<array.length; i++) {
			switch (array[i]) {
			//일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일" 
			// "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT" 
			// "S", "M", "T", "W", "H", "F", "A" 
			//case "일":
			case "S":
				result[i] = 1;
				break;
			//case "월":
			case "M":
				result[i] = 2;
				break;
			//case "화":
			case "T":
				result[i] = 3;
				break;
			//case "수":
			case "W":
				result[i] = 4;
				break;
			//case "목":
			case "H":
				result[i] = 5;
				break;
			//case "금":
			case "F":
				result[i] = 6;
				break;
			//case "토":
			case "A":
				result[i] = 7;
				break;
			}
		}
		return result;
	}
	
	public static void reload() {
		for (BossTemp temp : LIST) {
			if (temp.signMap != null) {
				temp.signMap.clear();
				temp.signMap = null;
			}
		}
		LIST.clear();
		_instance = new BossSpawnTable();
	}
}
