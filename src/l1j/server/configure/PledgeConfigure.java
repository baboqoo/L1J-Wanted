package l1j.server.configure;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.utils.StringUtil;

public class PledgeConfigure {
	private static final Logger _log = Logger.getLogger(PledgeConfigure.class.getName());
	private static final String PLEDGE_CONFIG_FILE	= "./config/pledge.properties";
	
	public int PLEDGE_CREATE_MIN_LEVEL;
	public int NOT_PLEDGE_SHOP_ENABLE_LEVEL;
	public int CLAN_BUFF_ACTIVE_MEMBER_COUNT;
	public int PLEDGE_LIMIT_MEMBER_COUNT;
	public boolean CROWN_OTHER_PLEDGE_JOIN_ENABLE;
	public int HOUSE_TAX_INTERVAL_DAY;
	public int BEGINNER_PLEDGE_ID;
	public int BEGINNER_PLEDGE_LEAVE_LEVEL;
	public boolean BEGINNER_PLEDGE_PVP_TYPE;
	public int PLEDGE_SHOP_NPC_ID;
	public boolean PLEDGE_CONTRIBUTION_EINHASAD;
	public boolean PLEDGE_CHANGE_TITLE_BY_SELF;
	
	public int PLEDGE_CONTRIBUTION_RATE;
	public int PLEDGE_CONTRIBUTION_MAX_COUNT;
	
	public int RANK_NORMAL_PRINCE_COUNT;
	public int RANK_NORMAL_KNIGHT_COUNT;
	public int RANK_NORMAL_ELITE_KNIGHT_COUNT;
	public int RANK_NORMAL_JUNIOR_KNIGHT;
	public int RANK_UPDATE_ROLL_DEFUALT_HOUR;
	public int RANK_UPDATE_ROLL_NEXT_HOUR;
	public int PLEDGE_STORE_ALLOW_MAX_COUNT;
	public int PLEDGE_JOIN_LIMIT_MAX_COUNT;
	
	public int WAR_DECLARE_LEVEL;
	public int WAR_DECLARE_USER_COUNT;
	public boolean WAR_TIME_FIX;
	
	public int WAR_TIME;
	public int WAR_TIME_UNIT;
	public int WAR_INTERVAL;
	public int WAR_INTERVAL_UNIT;
	public String WAR_REWARD_ITEMID;
	public String WAR_REWARD_NUMBER;
	
	public void load(){
		try {
			Properties setting				= new Properties();
			InputStream is					= new FileInputStream(new File(PLEDGE_CONFIG_FILE));
			setting.load(is);
			is.close();
			
			PLEDGE_CREATE_MIN_LEVEL			= Integer.parseInt(setting.getProperty("PLEDGE_CREATE_MIN_LEVEL", "30"));
			NOT_PLEDGE_SHOP_ENABLE_LEVEL	= Integer.parseInt(setting.getProperty("NOT_PLEDGE_SHOP_ENABLE_LEVEL", "100"));
			CLAN_BUFF_ACTIVE_MEMBER_COUNT	= Integer.parseInt(setting.getProperty("CLAN_BUFF_ACTIVE_MEMBER_COUNT", "5"));
			PLEDGE_LIMIT_MEMBER_COUNT		= Integer.parseInt(setting.getProperty("PLEDGE_LIMIT_MEMBER_COUNT", StringUtil.ZeroString));
			CROWN_OTHER_PLEDGE_JOIN_ENABLE	= Boolean.parseBoolean(setting.getProperty("CROWN_OTHER_PLEDGE_JOIN_ENABLE", StringUtil.TrueString));
			HOUSE_TAX_INTERVAL_DAY			= Integer.parseInt(setting.getProperty("HOUSE_TAX_INTERVAL_DAY", "10"));
			PLEDGE_CHANGE_TITLE_BY_SELF		= Boolean.parseBoolean(setting.getProperty("PLEDGE_CHANGE_TITLE_BY_SELF", StringUtil.FalseString));
			
			RANK_NORMAL_PRINCE_COUNT		= Integer.parseInt(setting.getProperty("RANK_NORMAL_PRINCE_COUNT", "1"));
			RANK_NORMAL_KNIGHT_COUNT		= Integer.parseInt(setting.getProperty("RANK_NORMAL_KNIGHT_COUNT", "3"));
			RANK_NORMAL_ELITE_KNIGHT_COUNT	= Integer.parseInt(setting.getProperty("RANK_NORMAL_ELITE_KNIGHT_COUNT", "7"));
			RANK_NORMAL_JUNIOR_KNIGHT		= Integer.parseInt(setting.getProperty("RANK_NORMAL_JUNIOR_KNIGHT", "28"));
			RANK_UPDATE_ROLL_DEFUALT_HOUR	= Integer.parseInt(setting.getProperty("RANK_UPDATE_ROLL_DEFUALT_HOUR", "168"));
			RANK_UPDATE_ROLL_NEXT_HOUR		= Integer.parseInt(setting.getProperty("RANK_UPDATE_ROLL_NEXT_HOUR", "72"));
			
			PLEDGE_STORE_ALLOW_MAX_COUNT	= Integer.parseInt(setting.getProperty("PLEDGE_STORE_ALLOW_MAX_COUNT", "50"));
			PLEDGE_JOIN_LIMIT_MAX_COUNT		= Integer.parseInt(setting.getProperty("PLEDGE_JOIN_LIMIT_MAX_COUNT", "50"));
			
			WAR_DECLARE_LEVEL				= Integer.parseInt(setting.getProperty("WAR_DECLARE_LEVEL", "60"));
			WAR_DECLARE_USER_COUNT			= Integer.parseInt(setting.getProperty("WAR_DECLARE_USER_COUNT", StringUtil.ZeroString));
			WAR_TIME_FIX		 			= Boolean.parseBoolean(setting.getProperty("WAR_TIME_FIX", StringUtil.TrueString));
			String strWar;
			strWar = setting.getProperty("WAR_TIME", "1h");
			if (strWar.indexOf("d") >= 0) {
				WAR_TIME_UNIT = Calendar.DATE;
				strWar = strWar.replace("d", StringUtil.EmptyString);
			} else if (strWar.indexOf("h") >= 0) {
				WAR_TIME_UNIT = Calendar.HOUR_OF_DAY;
				strWar = strWar.replace("h", StringUtil.EmptyString);
			} else if (strWar.indexOf("m") >= 0) {
				WAR_TIME_UNIT = Calendar.MINUTE;
				strWar = strWar.replace("m", StringUtil.EmptyString);
			}
			WAR_TIME = Integer.parseInt(strWar);
			strWar = setting.getProperty("WAR_INTERVAL", "2d");
			if (strWar.indexOf("d") >= 0) {
				WAR_INTERVAL_UNIT = Calendar.DATE;
				strWar = strWar.replace("d", StringUtil.EmptyString);
			} else if (strWar.indexOf("h") >= 0) {
				WAR_INTERVAL_UNIT = Calendar.HOUR_OF_DAY;
				strWar = strWar.replace("h", StringUtil.EmptyString);
			} else if (strWar.indexOf("m") >= 0) {
				WAR_INTERVAL_UNIT = Calendar.MINUTE;
				strWar = strWar.replace("m", StringUtil.EmptyString);
			}
			WAR_INTERVAL					= Integer.parseInt(strWar);
			WAR_REWARD_ITEMID				= setting.getProperty("WAR_REWARD_ITEMID", StringUtil.EmptyString);
			WAR_REWARD_NUMBER				= setting.getProperty("WAR_REWARD_NUMBER", StringUtil.EmptyString);
			
			BEGINNER_PLEDGE_ID				= Integer.parseInt(setting.getProperty("BEGINNER_PLEDGE_ID", "1"));
			BEGINNER_PLEDGE_LEAVE_LEVEL		= Integer.parseInt(setting.getProperty("BEGINNER_PLEDGE_LEAVE_LEVEL", "60"));
			BEGINNER_PLEDGE_PVP_TYPE		= Boolean.parseBoolean(setting.getProperty("BEGINNER_PLEDGE_PVP_TYPE", StringUtil.TrueString));
			
			PLEDGE_SHOP_NPC_ID				= Integer.parseInt(setting.getProperty("PLEDGE_SHOP_NPC_ID", "4"));
			PLEDGE_CONTRIBUTION_RATE		= Integer.parseInt(setting.getProperty("PLEDGE_CONTRIBUTION_RATE", "10"));
			PLEDGE_CONTRIBUTION_MAX_COUNT	= Integer.parseInt(setting.getProperty("PLEDGE_CONTRIBUTION_MAX_COUNT", "20000000"));
			PLEDGE_CONTRIBUTION_EINHASAD	= Boolean.parseBoolean(setting.getProperty("PLEDGE_CONTRIBUTION_EINHASAD", StringUtil.TrueString));
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw new Error("Failed to Load " + PLEDGE_CONFIG_FILE + " File.");
		}
	}
}

