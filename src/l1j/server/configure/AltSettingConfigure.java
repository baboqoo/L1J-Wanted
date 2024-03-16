package l1j.server.configure;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.model.L1GroundInventory.GROUND_ITEM_DELETE_TYPE;
import l1j.server.server.utils.StringUtil;

public class AltSettingConfigure {
	private static final Logger _log = Logger.getLogger(AltSettingConfigure.class.getName());
	private static final String ALT_SETTINGS_FILE	= "./config/altsettings.properties";
	
	public int DAILY_RESET_HOUR;
	public short GLOBAL_CHAT_LEVEL;
	public short WHISPER_CHAT_LEVEL;
	public byte AUTO_LOOT;
	public int LOOTING_RANGE;
	public boolean ALT_NONPVP;
	public boolean ALT_ATKMSG;
	public int MAX_PT;
	public int MAX_CHAT_PT;
	public boolean SIM_WAR_PENALTY;
	public boolean GET_BACK;
	public GROUND_ITEM_DELETE_TYPE ALT_ITEM_DELETION_TYPE;
	public int ALT_ITEM_DELETION_TIME;
	public int ALT_ITEM_DELETION_RANGE;
	public boolean ALT_GMSHOP;
	public int ALT_GMSHOP_MIN_ID;
	public int ALT_GMSHOP_MAX_ID;
	public boolean ALT_BASETOWN;
	public int ALT_BASETOWN_MIN_ID;
	public int ALT_BASETOWN_MAX_ID;
	public boolean ALT_HALLOWEENIVENT;
	public int WHOIS_CONTER; 
	
	public int BEGINNER_SAFE_LEVEL;
	public int BEGINNER_MAP_SIZE;
	public List<Integer> BEGINNER_MAP_LIST = new ArrayList<Integer>();

	public boolean ALT_FANTASYEVENT;
	public boolean ALT_RABBITEVENT;// 신묘이벤트(2011)
	
	public int EXPERIENCE_RETURN_MAX_LEVEL;
	public boolean BAPHOMET_SYSTEM_BONUS;

	public boolean ALT_WHO_COMMAND;
	public boolean ALT_REVIVAL_POTION;
	
	public int ALT_RATE_OF_DUTY;
	public boolean SPAWN_HOME_POINT;
	public int SPAWN_HOME_POINT_RANGE;
	public int SPAWN_HOME_POINT_COUNT;
	public int SPAWN_HOME_POINT_DELAY;
	public int ELEMENTAL_STONE_AMOUNT;
	public int MAX_DOLL_COUNT;
	public boolean RETURN_TO_NATURE;
	public int MAX_NPC_ITEM;
	public int MAX_PERSONAL_WAREHOUSE_ITEM;
	public int MAX_CLAN_WAREHOUSE_ITEM;
	public boolean DELETE_CHARACTER_AFTER_7DAYS;
	
	public int GMCODE;
	public int BAPHOMET_SYSTEM_LEVEL;
	public int ALT_DROPLEVELLIMIT;
	public int TRADABLE_LEVEL;
	
	public boolean Use_Show_Announcecycle; // 추가
	public int Show_Announcecycle_Time; // 추가
		
	public int HELL_TIME;
	public int HELL_LEVEL;
	
	public boolean ADEN_SHOP_ZONE;
	public int ADEN_SHOP_NPC_ID;
	
	public boolean DOLL_RACE_ENABLED;
	public int DOLL_RACE_TIME;
	public int DOLL_RACE_TICKETS_TIME;
	
	public boolean DOG_FIGHT_ENABLED;
	public int DOG_FIGHT_TIME;
	public int DOG_FIGHT_TICKETS_TIME;
	public double DOG_FIGHT_TICKETS_RATE;
	
	public int ARCA_MAX_ACTIVATE_CHARACTERS;
	public int ARCA_TIME;
	public int ARCA_REWARD_COUNT;
	
	public boolean EVENT_MONSTER_ACTIVE;
	public int EVENT_MONSTER_LEVEL;
	public int EVENT_MONSTER_CHANCE;
	public int EVENT_MONSTER_ID;
	public int EVENT_MONSTER_EFFECT;
	
	public boolean SHOW_PVP_KILL;	
	public boolean CHAT_ALLOWED_IN_PVP;
	public boolean BROADCAST_KILL_LOG;
	public int BROADCAST_KILL_LOG_LEVEL;
	
	public boolean NEW_CHAR_START_AREA;
	public boolean KARMA_BUFF_ENABLE;
	public boolean SAVE_DB_LOG;

	public static boolean WARP;

	//public int DESC_ORIGINAL_LINES;
	//public int ANNOUNCE_CYCLE_COUNT;
	//public int FIXED_SYSTEM_MESSAGES;
	//public int DESC_SEPARATORS;
	//public int DESC_DB_LINES;
	public int SERVER_MESSAGES_START_LINE;
	public int STRING_SERVER_START_LINE;

	
	public void load(){
		try {
			Properties altSettings	= new Properties();
			InputStream is			= new FileInputStream(new File(ALT_SETTINGS_FILE));
			altSettings.load(is);
			is.close();
			DAILY_RESET_HOUR				= Integer.parseInt(altSettings.getProperty("DAILY_RESET_HOUR", "6"));
			GLOBAL_CHAT_LEVEL				= Short.parseShort(altSettings.getProperty("GlobalChatLevel", "30"));
			WHISPER_CHAT_LEVEL				= Short.parseShort(altSettings.getProperty("WhisperChatLevel", "7"));
			AUTO_LOOT						= Byte.parseByte(altSettings.getProperty("AutoLoot", "2"));
			LOOTING_RANGE					= Integer.parseInt(altSettings.getProperty("LootingRange", "3"));
			ALT_NONPVP						= Boolean.parseBoolean(altSettings.getProperty("NonPvP", StringUtil.TrueString));
			ALT_ATKMSG						= Boolean.parseBoolean(altSettings.getProperty("AttackMessageOn", StringUtil.TrueString));
			MAX_PT							= Integer.parseInt(altSettings.getProperty("MaxPT", "8"));
			MAX_CHAT_PT						= Integer.parseInt(altSettings.getProperty("MaxChatPT", "8"));
			SIM_WAR_PENALTY					= Boolean.parseBoolean(altSettings.getProperty("SimWarPenalty", StringUtil.TrueString));
			GET_BACK						= Boolean.parseBoolean(altSettings.getProperty("GetBack", StringUtil.FalseString));
			ALT_ITEM_DELETION_TYPE			= GROUND_ITEM_DELETE_TYPE.fromString(altSettings.getProperty("ItemDeletionType", "auto"));
			ALT_ITEM_DELETION_TIME			= Integer.parseInt(altSettings.getProperty("ItemDeletionTime", "10"));
			ALT_ITEM_DELETION_RANGE			= Integer.parseInt(altSettings.getProperty("ItemDeletionRange", "5"));
			ALT_GMSHOP						= Boolean.parseBoolean(altSettings.getProperty("GMshop", StringUtil.FalseString));
			ALT_GMSHOP_MIN_ID				= Integer.parseInt(altSettings.getProperty("GMshopMinID", "0xffffffff"));
			ALT_GMSHOP_MAX_ID				= Integer.parseInt(altSettings.getProperty("GMshopMaxID", "0xffffffff"));
			ALT_BASETOWN					= Boolean.parseBoolean(altSettings.getProperty("BaseTown", StringUtil.FalseString));
			ALT_BASETOWN_MIN_ID				= Integer.parseInt(altSettings.getProperty("BaseTownMinID", "0xffffffff"));
			ALT_BASETOWN_MAX_ID				= Integer.parseInt(altSettings.getProperty("BaseTownMaxID", "0xffffffff"));
			ALT_HALLOWEENIVENT				= Boolean.parseBoolean(altSettings.getProperty("HalloweenIvent", StringUtil.TrueString));
			WHOIS_CONTER					= Integer.parseInt(altSettings.getProperty("WhoisConter", StringUtil.ZeroString)); //
			ALT_FANTASYEVENT				= Boolean.parseBoolean(altSettings.getProperty("FantasyEvent", StringUtil.TrueString));
			ALT_WHO_COMMAND					= Boolean.parseBoolean(altSettings.getProperty("WhoCommand", StringUtil.FalseString));
			ALT_REVIVAL_POTION				= Boolean.parseBoolean(altSettings.getProperty("RevivalPotion", StringUtil.FalseString));
			ALT_RABBITEVENT					= Boolean.parseBoolean(altSettings.getProperty("RabbitEvent", StringUtil.FalseString));
			ALT_DROPLEVELLIMIT				= Integer.parseInt(altSettings.getProperty("DropLevelLimit", "90"));

			TRADABLE_LEVEL					= Integer.parseInt(altSettings.getProperty("TradeLevel", "5"));
			
			BAPHOMET_SYSTEM_LEVEL			= Integer.parseInt(altSettings.getProperty("BAPHOMET_SYSTEM_LEVEL", StringUtil.ZeroString));
			
			BEGINNER_SAFE_LEVEL				= Integer.parseInt(altSettings.getProperty("BEGINNER_SAFE_LEVEL", "75"));
			BEGINNER_MAP_SIZE				= Integer.parseInt(altSettings.getProperty("BEGINNER_MAP_SIZE", StringUtil.ZeroString));
			BEGINNER_MAP_LIST.clear();
			if (BEGINNER_MAP_SIZE > 0) {
				for (int i=1; i<=BEGINNER_MAP_SIZE; i++) {
					int beginnerMapId		= Integer.parseInt(altSettings.getProperty("BEGINNER_MAP_ID_" + i, StringUtil.ZeroString));
					if (beginnerMapId > 0) {
						BEGINNER_MAP_LIST.add(beginnerMapId);
					}
				}
			}
			
			HELL_TIME						= Integer.parseInt(altSettings.getProperty("HellTime", "6"));
			HELL_LEVEL						= Integer.parseInt(altSettings.getProperty("HellLevel", "70"));
								
			SPAWN_HOME_POINT				= Boolean.parseBoolean(altSettings.getProperty("SpawnHomePoint", StringUtil.TrueString));
			SPAWN_HOME_POINT_COUNT			= Integer.parseInt(altSettings.getProperty("SpawnHomePointCount", "2"));
			SPAWN_HOME_POINT_DELAY			= Integer.parseInt(altSettings.getProperty("SpawnHomePointDelay", "100"));
			SPAWN_HOME_POINT_RANGE			= Integer.parseInt(altSettings.getProperty("SpawnHomePointRange", "8"));
			ELEMENTAL_STONE_AMOUNT			= Integer.parseInt(altSettings.getProperty("ElementalStoneAmount", "300"));
			MAX_DOLL_COUNT					= Integer.parseInt(altSettings.getProperty("MaxDollCount", "1"));
			RETURN_TO_NATURE				= Boolean.parseBoolean(altSettings.getProperty("ReturnToNature", StringUtil.FalseString));
			
			MAX_NPC_ITEM					= Integer.parseInt(altSettings.getProperty("MaxNpcItem", "8"));
			MAX_PERSONAL_WAREHOUSE_ITEM		= Integer.parseInt(altSettings.getProperty("MaxPersonalWarehouseItem", "100"));
			MAX_CLAN_WAREHOUSE_ITEM			= Integer.parseInt(altSettings.getProperty("MaxClanWarehouseItem", "200"));
			DELETE_CHARACTER_AFTER_7DAYS	= Boolean.parseBoolean(altSettings.getProperty("DeleteCharacterAfter7Days", StringUtil.TrueString));
			
			GMCODE							= Integer.parseInt(altSettings.getProperty("GMCODE", "9999"));
			EXPERIENCE_RETURN_MAX_LEVEL 	= Integer.parseInt(altSettings.getProperty("Expreturn", "75"));
			BAPHOMET_SYSTEM_BONUS			= Boolean.parseBoolean(altSettings.getProperty("BapoZone", StringUtil.FalseString));
			
			ADEN_SHOP_ZONE					= Boolean.parseBoolean(altSettings.getProperty("AdenShopZone", StringUtil.FalseString)); 
			ADEN_SHOP_NPC_ID				= Integer.parseInt(altSettings.getProperty("AdenShopNpc", "5"));
			
			DOLL_RACE_ENABLED				= Boolean.parseBoolean(altSettings.getProperty("DollRace", StringUtil.FalseString)); 
			DOLL_RACE_TIME					= Integer.parseInt(altSettings.getProperty("DollRaceTime", "3"));
			DOLL_RACE_TICKETS_TIME			= Integer.parseInt(altSettings.getProperty("DollRaceStayTime", "3"));
			
			DOG_FIGHT_ENABLED				= Boolean.parseBoolean(altSettings.getProperty("DogFight", StringUtil.FalseString)); 
			DOG_FIGHT_TIME					= Integer.parseInt(altSettings.getProperty("DogFightTime", "3"));
			DOG_FIGHT_TICKETS_TIME			= Integer.parseInt(altSettings.getProperty("DogFightStayTime", "3"));
			DOG_FIGHT_TICKETS_RATE			= Double.parseDouble(altSettings.getProperty("DogFightTiket", "1.95"));
			
			ARCA_MAX_ACTIVATE_CHARACTERS	= Integer.parseInt(altSettings.getProperty("ARCA_MAX_ACTIVATE_CHARACTERS", "5"));
			ARCA_TIME						= Integer.parseInt(altSettings.getProperty("ARCA_TIME", "15"));
			ARCA_REWARD_COUNT				= Integer.parseInt(altSettings.getProperty("ARCA_REWARD_COUNT", "6"));
			
			EVENT_MONSTER_ACTIVE			= Boolean.parseBoolean(altSettings.getProperty("EVENT_MONSTER_ACTIVE", StringUtil.FalseString));
			EVENT_MONSTER_LEVEL				= Integer.parseInt(altSettings.getProperty("EVENT_MONSTER_LEVEL", "3"));
			EVENT_MONSTER_CHANCE			= Integer.parseInt(altSettings.getProperty("EVENT_MONSTER_CHANCE", "3"));
			EVENT_MONSTER_ID				= Integer.parseInt(altSettings.getProperty("EVENT_MONSTER_ID", "5000127"));
			EVENT_MONSTER_EFFECT			= Integer.parseInt(altSettings.getProperty("EVENT_MONSTER_EFFECT", "4784"));
			
			CHAT_ALLOWED_IN_PVP				= Boolean.parseBoolean(altSettings.getProperty("PKChat", StringUtil.FalseString));
			SHOW_PVP_KILL					= Boolean.parseBoolean(altSettings.getProperty("Killdeath", StringUtil.FalseString));
			BROADCAST_KILL_LOG				= Boolean.parseBoolean(altSettings.getProperty("BroadcastKillLog", StringUtil.TrueString));
			BROADCAST_KILL_LOG_LEVEL		= Integer.parseInt(altSettings.getProperty("BroadcastKillLogLevel", "1"));
			
			NEW_CHAR_START_AREA				= Boolean.parseBoolean(altSettings.getProperty("StartZone", StringUtil.TrueString));
			KARMA_BUFF_ENABLE				= Boolean.parseBoolean(altSettings.getProperty("BossAttck", StringUtil.FalseString));
			SAVE_DB_LOG						= Boolean.parseBoolean(altSettings.getProperty("LogTrue", StringUtil.TrueString));

			WARP 							= Boolean.parseBoolean(altSettings.getProperty("Warp", StringUtil.TrueString));
			//DESC_ORIGINAL_LINES			= Integer.parseInt(altSettings.getProperty("DESC_ORIGINAL_LINES", "0"));
			//ANNOUNCE_CYCLE_COUNT			= Integer.parseInt(altSettings.getProperty("ANNOUNCE_CYCLE_COUNT", "0"));
			//FIXED_SYSTEM_MESSAGES			= Integer.parseInt(altSettings.getProperty("FIXED_SYSTEM_MESSAGES", "0"));
			//DESC_DB_LINES					= Integer.parseInt(altSettings.getProperty("DESC_DB_LINES", "0"));
			//DESC_SEPARATORS				= Integer.parseInt(altSettings.getProperty("DESC_SEPARATORS", "0"));
			SERVER_MESSAGES_START_LINE		= Integer.parseInt(altSettings.getProperty("SERVER_MESSAGES_START_LINE", "0"));
			STRING_SERVER_START_LINE		= Integer.parseInt(altSettings.getProperty("STRING_SERVER_START_LINE", "0"));
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw new Error("Failed to Load " + ALT_SETTINGS_FILE + " File.");
		}
	}
}

