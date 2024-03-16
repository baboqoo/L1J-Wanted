package l1j.server.configure;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.utils.CharsetUtil;
import l1j.server.server.utils.StringUtil;

public class ServerConfigure {
	private static final Logger _log = Logger.getLogger(ServerConfigure.class.getName());
	private static final String SERVER_CONFIG_FILE	= "./config/server.properties";
	
	public final String LOCALHOST = "127.0.0.1";
	
    public boolean OPCODES_PRINT_C;
    public boolean OPCODES_PRINT_S;
    public boolean PROTO_CLIENT_CODE_FIND;
    public boolean CLIENT_FUNCTION_LOG_PRINT;
	public boolean DEBUG_SERVER;
	public boolean OPCODES_ONLY_HEADER;
    
    public int RECEIVE_PACKET_LIMIT_SIZE;
    public int RECEIVE_PACKET_OVER_COUNT;
    public int ACCEPT_OVER_MONITOR_INTERVAL;
    public int ACCEPT_OVER_LIMIT_COUNT;
	
	/** Thread pools size */
	public int THREAD_P_EFFECTS;
	public int THREAD_P_GENERAL;
	public int AI_MAX_THREAD;
	public int THREAD_P_SIZE_GENERAL;
	public int THREAD_P_TYPE_GENERAL;
	public int SELECT_THREAD_COUNT;
	public int SCHEDULED_CORE_POOLSIZE;
	
	/** Server control */
	public String GAME_SERVER_NAME;
	public int LOGIN_SERVER_PORT;
	public String LOGIN_SERVER_ADDRESS;
	public int WELLKNOWN_PORT;
	
	public String DB_DRIVER;
	public String DB_URL;
	public String DB_LOGIN;
	public String DB_PASSWORD;
	public int DB_POOL_MIN;
	public int DB_POOL_MAX;
	
	public String TIME_ZONE;
	public int CLIENT_LANGUAGE;
	public boolean HOSTNAME_LOOKUPS;
	public int AUTOMATIC_KICK;
	public boolean AUTO_CREATE_ACCOUNTS;
	public short MAX_ONLINE_USERS;
	public boolean CACHE_MAP_FILES;
	public boolean CHECK_MOVE_INTERVAL;
	public boolean CHECK_ATTACK_INTERVAL;
	public boolean CHECK_SPELL_INTERVAL;
	public byte LOGGING_WEAPON_ENCHANT;
	public byte LOGGING_ARMOR_ENCHANT;
	public boolean LOGGING_CHAT_NORMAL;
	public boolean LOGGING_CHAT_WHISPER;
	public boolean LOGGING_CHAT_SHOUT;
	public boolean LOGGING_CHAT_WORLD;
	public boolean LOGGING_CHAT_CLAN;
	public boolean LOGGING_CHAT_PARTY;
	public boolean LOGGING_CHAT_COMBINED;
	public boolean LOGGING_CHAT_CHAT_PARTY;

	public int PC_RECOGNIZE_RANGE;
	public boolean CHARACTER_CONFIG_IN_SERVER_SIDE;
	
	public boolean ALLOW_2PC;
	public int ALLOW_2PC_IP_COUNT;
	public int ALLOW_2PC_HDD_COUNT;
	
	public int LEVEL_DOWN_RANGE;
	public boolean DETECT_DB_RESOURCE_LEAKS;
	public int CHARACTER_SLOT_MAX_COUNT;
	
	public int CREATE_IP_ACCOUNT_COUNT;
	public int CREATE_HDD_ACCOUNT_COUNT;
	
	public boolean CONNECT_DEVELOP_LOCK;
	public boolean PROFIT_SERVER_ACTIVE;
	
	public boolean SECOND_PASSWORD_USE;
	public int AUTO_CHAR_SAVE_TIME;
	
	public boolean STANDBY_SERVER;
	
	public boolean LOGIN_TYPE;
	public boolean IP_PROTECT;
	public String IP_INFORMATION_API_KEY;
	public int RESTART_ENTER_WORLD_INTERVAL;
	
	public boolean ACCESS_STANBY;

	public boolean PALADIN_ACTIVE;
	
	public void load(){
		try {
			Properties settings				= new Properties();
			InputStream is					= new FileInputStream(new File(SERVER_CONFIG_FILE));
			settings.load(is);
			is.close();
			
			// 서버 IP(공유기 사용시)
			/*Cmd cmd = new Cmd();
			String query_result = cmd.execCommand("nslookup myip.opendns.com resolver1.opendns.com").split(StringUtil.LineString)[4];
			SERVER_ADDREES = query_result.replace("Address:", StringUtil.EmptyString).trim();*/
			
			// 서버 IP
			/*try {
				GAME_SERVER_ADDRESS			= InetAddress.getLocalHost().getHostAddress();
			} catch (UnknownHostException e) {
				e.printStackTrace();
				System.out.println("CONFIG SERVER_ADDREES SETTING FAILURE");
			}*/
			
			SCHEDULED_CORE_POOLSIZE			= Integer.parseInt(settings.getProperty("SCHEDULED_CORE_POOLSIZE", "512"));
			
			GAME_SERVER_NAME				= new String(settings.getProperty("GAME_SERVER_NAME", "*").getBytes(CharsetUtil.ISO_8859_1_STR), CharsetUtil.EUC_KR_STR);
			LOGIN_SERVER_PORT				= Integer.parseInt(settings.getProperty("LOGIN_SERVER_PORT", "2000"));
			LOGIN_SERVER_ADDRESS			= settings.getProperty("LOGIN_SERVER_ADDRESS", LOCALHOST);
			
			DB_DRIVER						= settings.getProperty("DB_DRIVER", "com.mysql.jdbc.Driver");
			DB_URL							= settings.getProperty("DB_URL", "jdbc:mysql://localhost/l1jdb?useUnicode=true&characterEncoding=euckr");
			DB_LOGIN						= settings.getProperty("DB_LOGIN", "root");
			DB_PASSWORD						= settings.getProperty("DB_PASSWORD", StringUtil.EmptyString);
			DB_POOL_MIN						= Integer.parseInt(settings.getProperty("DB_POOL_MIN", "32"));
			DB_POOL_MAX						= Integer.parseInt(settings.getProperty("DB_POOL_MAX", "126"));
			
			THREAD_P_TYPE_GENERAL			= Integer.parseInt(settings.getProperty("GeneralThreadPoolType", StringUtil.ZeroString), 10);
			THREAD_P_SIZE_GENERAL			= Integer.parseInt(settings.getProperty("GeneralThreadPoolSize", StringUtil.ZeroString), 10);
			SELECT_THREAD_COUNT				= Integer.parseInt(settings.getProperty("IoThreadPoolSize", "4"));
			CLIENT_LANGUAGE					= Integer.parseInt(settings.getProperty("CLIENT_LANGUAGE", "4"));
			TIME_ZONE						= settings.getProperty("TIME_ZONE", "KST");
			HOSTNAME_LOOKUPS				= Boolean.parseBoolean(settings.getProperty("HOSTNAME_LOOKUPS", StringUtil.FalseString));
			AUTOMATIC_KICK					= Integer.parseInt(settings.getProperty("AUTOMATIC_KICK", "10"));
			AUTO_CREATE_ACCOUNTS			= Boolean.parseBoolean(settings.getProperty("AUTO_CREATE_ACCOUNTS", StringUtil.TrueString));
			MAX_ONLINE_USERS				= Short.parseShort(settings.getProperty("MAX_ONLINE_USERS", "30"));
			CACHE_MAP_FILES					= Boolean.parseBoolean(settings.getProperty("CACHE_MAP_FILES", StringUtil.FalseString));
			LOGGING_WEAPON_ENCHANT			= Byte.parseByte(settings.getProperty("LOGGING_WEAPON_ENCHANT", StringUtil.ZeroString));
			LOGGING_ARMOR_ENCHANT			= Byte.parseByte(settings.getProperty("LOGGING_ARMOR_ENCHANT", StringUtil.ZeroString));
			LOGGING_CHAT_NORMAL				= Boolean.parseBoolean(settings.getProperty("LOGGING_CHAT_NORMAL", StringUtil.FalseString));
			LOGGING_CHAT_WHISPER			= Boolean.parseBoolean(settings.getProperty("LOGGING_CHAT_WHISPER", StringUtil.FalseString));
			LOGGING_CHAT_SHOUT				= Boolean.parseBoolean(settings.getProperty("LOGGING_CHAT_SHOUT", StringUtil.FalseString));
			LOGGING_CHAT_WORLD				= Boolean.parseBoolean(settings.getProperty("LOGGING_CHAT_WORLD", StringUtil.FalseString));
			LOGGING_CHAT_CLAN				= Boolean.parseBoolean(settings.getProperty("LOGGING_CHAT_CLAN", StringUtil.FalseString));
			LOGGING_CHAT_PARTY				= Boolean.parseBoolean(settings.getProperty("LOGGING_CHAT_PARTY", StringUtil.FalseString));
			LOGGING_CHAT_COMBINED			= Boolean.parseBoolean(settings.getProperty("LOGGING_CHAT_COMBINED", StringUtil.FalseString));
			LOGGING_CHAT_CHAT_PARTY			= Boolean.parseBoolean(settings.getProperty("LOGGING_CHAT_CHAT_PARTY", StringUtil.FalseString));
			PC_RECOGNIZE_RANGE				= Integer.parseInt(settings.getProperty("PC_RECOGNIZE_RANGE", "-1"));
			
			CHARACTER_CONFIG_IN_SERVER_SIDE	= Boolean.parseBoolean(settings.getProperty("CHARACTER_CONFIG_IN_SERVER_SIDE", StringUtil.TrueString));
			
			ALLOW_2PC						= Boolean.parseBoolean(settings.getProperty("ALLOW_2PC", StringUtil.TrueString));
			ALLOW_2PC_IP_COUNT				= Integer.parseInt(settings.getProperty("ALLOW_2PC_IP_COUNT", "2"));
			ALLOW_2PC_HDD_COUNT				= Integer.parseInt(settings.getProperty("ALLOW_2PC_HDD_COUNT", "2"));
			
			LEVEL_DOWN_RANGE				= Integer.parseInt(settings.getProperty("LEVEL_DOWN_RANGE", StringUtil.ZeroString));
			DETECT_DB_RESOURCE_LEAKS		= Boolean.parseBoolean(settings.getProperty("DETECT_DB_RESOURCE_LEAKS", StringUtil.FalseString));
			CHARACTER_SLOT_MAX_COUNT		= Integer.parseInt(settings.getProperty("CHARACTER_SLOT_MAX_COUNT", "10"));
			
			CREATE_IP_ACCOUNT_COUNT			= Integer.parseInt(settings.getProperty("CREATE_IP_ACCOUNT_COUNT", "2"));
			CREATE_HDD_ACCOUNT_COUNT		= Integer.parseInt(settings.getProperty("CREATE_HDD_ACCOUNT_COUNT", "2"));
			
			SECOND_PASSWORD_USE				= Boolean.parseBoolean(settings.getProperty("SECOND_PASSWORD_USE"));
			AUTO_CHAR_SAVE_TIME				= Integer.parseInt(settings.getProperty("AUTO_CHAR_SAVE_TIME", "10"));
			
			WELLKNOWN_PORT					= Integer.parseInt(settings.getProperty("WELLKNOWN_PORT", "1023"));
			RECEIVE_PACKET_LIMIT_SIZE		= Integer.parseInt(settings.getProperty("RECEIVE_PACKET_LIMIT_SIZE", "5120"));
			RECEIVE_PACKET_OVER_COUNT		= Integer.parseInt(settings.getProperty("RECEIVE_PACKET_OVER_COUNT", "3"));
			ACCEPT_OVER_MONITOR_INTERVAL	= Integer.parseInt(settings.getProperty("ACCEPT_OVER_MONITOR_INTERVAL", "10"));
			ACCEPT_OVER_LIMIT_COUNT			= Integer.parseInt(settings.getProperty("ACCEPT_OVER_LIMIT_COUNT", "10"));
			
			OPCODES_PRINT_C					= Boolean.parseBoolean(settings.getProperty("OPCODES_PRINT_C", StringUtil.FalseString));
			OPCODES_PRINT_S					= Boolean.parseBoolean(settings.getProperty("OPCODES_PRINT_S", StringUtil.FalseString));
			PROTO_CLIENT_CODE_FIND			= Boolean.parseBoolean(settings.getProperty("PROTO_CLIENT_CODE_FIND", StringUtil.FalseString));
			CLIENT_FUNCTION_LOG_PRINT		= Boolean.parseBoolean(settings.getProperty("CLIENT_FUNCTION_LOG_PRINT", StringUtil.FalseString));
			DEBUG_SERVER					= Boolean.parseBoolean(settings.getProperty("DEBUG_SERVER", StringUtil.FalseString));
			OPCODES_ONLY_HEADER				= Boolean.parseBoolean(settings.getProperty("OPCODES_ONLY_HEADER", StringUtil.FalseString));
			
			STANDBY_SERVER					= Boolean.parseBoolean(settings.getProperty("STANDBY_SERVER", StringUtil.FalseString));
			
			LOGIN_TYPE						= Boolean.parseBoolean(settings.getProperty("LOGIN_TYPE", StringUtil.FalseString));
			IP_PROTECT						= Boolean.parseBoolean(settings.getProperty("IP_PROTECT", StringUtil.FalseString));
			IP_INFORMATION_API_KEY			= new String(settings.getProperty("IP_INFORMATION_API_KEY", StringUtil.EmptyString).getBytes(CharsetUtil.ISO_8859_1_STR), CharsetUtil.EUC_KR_STR);
			
			RESTART_ENTER_WORLD_INTERVAL	= Integer.parseInt(settings.getProperty("RESTART_ENTER_WORLD_INTERVAL", "3000"));
			
			ACCESS_STANBY 					= Boolean.parseBoolean(settings.getProperty("ACCESS_STANBY", StringUtil.FalseString));
			CONNECT_DEVELOP_LOCK			= Boolean.parseBoolean(settings.getProperty("CONNECT_DEVELOP_LOCK", StringUtil.FalseString));
		
			PROFIT_SERVER_ACTIVE			= Boolean.parseBoolean(settings.getProperty("PROFIT_SERVER_ACTIVE"));
			PALADIN_ACTIVE					= Boolean.parseBoolean(settings.getProperty("PALADIN_ACTIVE"));
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw new Error("Failed to Load " + SERVER_CONFIG_FILE + " File.");
		}
	}
}

