package l1j.server.configure;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.utils.CharsetUtil;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.StringUtil;

public class DungeonConfigure {
	private static final Logger _log = Logger.getLogger(DungeonConfigure.class.getName());
	private static final String DUNGEON_CONFIG_FILE	= "./config/dungeon.properties";
	
	public boolean TIME_CRACK_ACTIVE;
	public int LASTAVAD_LIMIT_LEVEL;
	
	public boolean BATTLE_ZONE_ACTIVE;
	public int BATTLE_ZONE_LIMIT_LEVEL;
	public int BATTLE_ZONE_STAY_MINUT;
	public String BATTLE_ZONE_REWARD_ITEMID;
	public String BATTLE_ZONE_REWARD_COUNT;
	
	public boolean DEVIL_KING_ACTIVE; 
	public int DEVIL_KING_OPEN_HOUR;
	public int DEVIL_KING_CLOSE_HOUR;
	public int DEVIL_KING_LIMIT_LEVEL; 
	
	public int DOMINATION_TOWER_OPEN_HOUR;
	public int DOMINATION_TOWER_CLOSE_HOUR;
	public int OLD_GOD_TRANS_RATE;
	
	public boolean ISLAND_ACTIVE;
	public String ISLAND_DAY_REGEX = "";
	public int ISLAND_OPEN_HOUR;
	public int ISLAND_CLOSE_HOUR;
	
	public boolean ISLAND_LOCAL_ACTIVE;
	public String ISLAND_DAY_LOCAL_REGEX = "";
	public int ISLAND_OPEN_HOUR_LOCAL;
	public int ISLAND_CLOSE_HOUR_LOCAL;
	public int ISLAND_NIGHT_SPAWN_DELAY;
	
	public int BLACK_DRAGON_DUNGEON_OPEN_HOUR;
	public int BLACK_DRAGON_DUNGEON_CLOSE_HOUR;
	
	public boolean ANT_QUEEN_INCLUDE;
	public String ANT_QUEEN_DAY;
	public int ANT_QUEEN_OPEN_HOUR;
	public int ANT_QUEEN_OPEN_MINUT;
	
	public boolean TREASURE_ISLAND_ACTIVE;
	public int TREASURE_ISLAND_OPEN_HOUR;
	public int TREASURE_ISLAND_OPEN_MINUT;
	public int TREASURE_ISLAND_DURATION;
	
	public boolean CLONE_WAR_ACTIVE;
	public int[] CLONE_WAR_TIME;
	
	public boolean OMAN_CRACK;
	public int OMAN_CRACK_DURATION;
	public int OMAN_CRACK_TRANS_RATE;
	
	public boolean RUUN_CASTLE_TIMER_ACTIVE;
	
	public int WHALE_BOSS_ROOM_LIMIT_SECOND;
	public int WHALE_TREASURE_ROOM_LIMIT_SECOND;
	
	public void load(){
		try{
			Properties 	dungeon 			= new Properties();
			InputStream is					= new FileInputStream(new File(DUNGEON_CONFIG_FILE));
			dungeon.load(is);
			is.close();
			TIME_CRACK_ACTIVE				= Boolean.parseBoolean(dungeon.getProperty("TIME_CRACK_ACTIVE", StringUtil.TrueString));
			LASTAVAD_LIMIT_LEVEL			= Integer.parseInt(dungeon.getProperty("LASTAVAD_LIMIT_LEVEL", "80"));
			
			BATTLE_ZONE_LIMIT_LEVEL			= Integer.parseInt(dungeon.getProperty("BATTLE_ZONE_LIMIT_LEVEL", "55"));
			BATTLE_ZONE_STAY_MINUT			= Integer.parseInt(dungeon.getProperty("BATTLE_ZONE_STAY_MINUT", "10"));
			BATTLE_ZONE_ACTIVE				= Boolean.parseBoolean(dungeon.getProperty("BATTLE_ZONE_ACTIVE", StringUtil.TrueString));
			BATTLE_ZONE_REWARD_ITEMID		= dungeon.getProperty("BATTLE_ZONE_REWARD_ITEMID", StringUtil.EmptyString);
			BATTLE_ZONE_REWARD_COUNT		= dungeon.getProperty("BATTLE_ZONE_REWARD_COUNT", StringUtil.EmptyString);
			
			DEVIL_KING_ACTIVE				= Boolean.parseBoolean(dungeon.getProperty("DEVIL_KING_ACTIVE", StringUtil.TrueString));
			DEVIL_KING_OPEN_HOUR			= Integer.parseInt(dungeon.getProperty("DEVIL_KING_OPEN_HOUR", "22"));
			DEVIL_KING_CLOSE_HOUR			= Integer.parseInt(dungeon.getProperty("DEVIL_KING_CLOSE_HOUR", "23"));
			DEVIL_KING_LIMIT_LEVEL			= Integer.parseInt(dungeon.getProperty("DEVIL_KING_LIMIT_LEVEL", "55"));
			
			DOMINATION_TOWER_OPEN_HOUR		= Integer.parseInt(dungeon.getProperty("DOMINATION_TOWER_OPEN_HOUR", "14"));
			DOMINATION_TOWER_CLOSE_HOUR		= Integer.parseInt(dungeon.getProperty("DOMINATION_TOWER_CLOSE_HOUR", "1"));
			OLD_GOD_TRANS_RATE				= Integer.parseInt(dungeon.getProperty("OLD_GOD_TRANS_RATE", "1"));
			
			ISLAND_ACTIVE					= Boolean.parseBoolean(dungeon.getProperty("ISLAND_ACTIVE", StringUtil.TrueString));
			String[] isl					= dungeon.getProperty("ISLAND_DAY", "0, 6").split(StringUtil.CommaString);
			for (int i=0; i<isl.length; i++) {
				int day = Integer.parseInt(isl[i].trim());
				if (i > 0) {
					ISLAND_DAY_REGEX += "|";
				}
				ISLAND_DAY_REGEX += CommonUtil.WEEK_DAY_ARRAY[day];
			}
			ISLAND_OPEN_HOUR				= Integer.parseInt(dungeon.getProperty("ISLAND_OPEN_HOUR", "20"));
			ISLAND_CLOSE_HOUR				= Integer.parseInt(dungeon.getProperty("ISLAND_CLOSE_HOUR", "20"));
			
			ISLAND_LOCAL_ACTIVE				= Boolean.parseBoolean(dungeon.getProperty("ISLAND_LOCAL_ACTIVE", StringUtil.TrueString));
			String[] islLocal				= dungeon.getProperty("ISLAND_DAY_LOCAL", "0, 6").split(StringUtil.CommaString);
			for (int i=0; i<islLocal.length; i++) {
				int day = Integer.parseInt(islLocal[i].trim());
				if (i > 0) {
					ISLAND_DAY_LOCAL_REGEX += "|";
				}
				ISLAND_DAY_LOCAL_REGEX += CommonUtil.WEEK_DAY_ARRAY[day];
			}
			ISLAND_OPEN_HOUR_LOCAL			= Integer.parseInt(dungeon.getProperty("ISLAND_OPEN_HOUR_LOCAL", "20"));
			ISLAND_CLOSE_HOUR_LOCAL			= Integer.parseInt(dungeon.getProperty("ISLAND_CLOSE_HOUR_LOCAL", "20"));
			ISLAND_NIGHT_SPAWN_DELAY		= Integer.parseInt(dungeon.getProperty("ISLAND_NIGHT_SPAWN_DELAY", "12"));
			
			BLACK_DRAGON_DUNGEON_OPEN_HOUR	= Integer.parseInt(dungeon.getProperty("BLACK_DRAGON_DUNGEON_OPEN_HOUR", "9"));
			BLACK_DRAGON_DUNGEON_CLOSE_HOUR	= Integer.parseInt(dungeon.getProperty("BLACK_DRAGON_DUNGEON_CLOSE_HOUR", "1"));
			
			ANT_QUEEN_INCLUDE				= Boolean.parseBoolean(dungeon.getProperty("ANT_QUEEN_INCLUDE", StringUtil.FalseString));
			ANT_QUEEN_DAY					= new String(dungeon.getProperty("ANT_QUEEN_DAY", StringUtil.EmptyString).getBytes(CharsetUtil.ISO_8859_1_STR), CharsetUtil.EUC_KR_STR);
			ANT_QUEEN_OPEN_HOUR				= Integer.parseInt(dungeon.getProperty("ANT_QUEEN_OPEN_HOUR", StringUtil.ZeroString));
			ANT_QUEEN_OPEN_MINUT			= Integer.parseInt(dungeon.getProperty("ANT_QUEEN_OPEN_MINUT", StringUtil.ZeroString));
			
			TREASURE_ISLAND_ACTIVE			= Boolean.parseBoolean(dungeon.getProperty("TREASURE_ISLAND_ACTIVE", StringUtil.TrueString));
			TREASURE_ISLAND_OPEN_HOUR		= Integer.parseInt(dungeon.getProperty("TREASURE_ISLAND_OPEN_HOUR", "21"));
			TREASURE_ISLAND_OPEN_MINUT		= Integer.parseInt(dungeon.getProperty("TREASURE_ISLAND_OPEN_MINUT", "30"));
			TREASURE_ISLAND_DURATION		= Integer.parseInt(dungeon.getProperty("TREASURE_ISLAND_DURATION", "30"));
			
			CLONE_WAR_ACTIVE				= Boolean.parseBoolean(dungeon.getProperty("CLONE_WAR_ACTIVE", StringUtil.TrueString));
			String[] cloneTime				= new String(dungeon.getProperty("CLONE_WAR_HOUR", "18-22")).replaceAll(StringUtil.EmptyOneString, StringUtil.EmptyString).split(StringUtil.MinusString);
			CLONE_WAR_TIME					= new int[2];
			CLONE_WAR_TIME[0]				= Integer.parseInt(cloneTime[0]);
			CLONE_WAR_TIME[1]				= Integer.parseInt(cloneTime[1]) - CLONE_WAR_TIME[0];
			
			OMAN_CRACK						= Boolean.parseBoolean(dungeon.getProperty("OMAN_CRACK", StringUtil.TrueString));
			OMAN_CRACK_DURATION				= Integer.parseInt(dungeon.getProperty("OMAN_CRACK_DURATION", "40"));
			OMAN_CRACK_TRANS_RATE			= Integer.parseInt(dungeon.getProperty("OMAN_CRACK_TRANS_RATE", "5"));
			
			RUUN_CASTLE_TIMER_ACTIVE		= Boolean.parseBoolean(dungeon.getProperty("RUUN_CASTLE_TIMER_ACTIVE", StringUtil.TrueString));
			
			WHALE_BOSS_ROOM_LIMIT_SECOND 	= Integer.parseInt(dungeon.getProperty("WHALE_BOSS_ROOM_LIMIT_SECOND", "600"));
			WHALE_TREASURE_ROOM_LIMIT_SECOND = Integer.parseInt(dungeon.getProperty("WHALE_TREASURE_ROOM_LIMIT_SECOND", "300"));
		} catch(Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw new Error("Failed to Load " + DUNGEON_CONFIG_FILE + " File.");
		}
	}

}

