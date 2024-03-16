package l1j.server.configure;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.utils.StringUtil;

public class AttendanceConfigure {
	private static final Logger _log = Logger.getLogger(AttendanceConfigure.class.getName());
	private static final String ATTENDANCE_CONFIG_FILE	= "./config/attendance.properties";
	
	public boolean ATTENDANCE_ACTIVE;
	public int ATTENDANCE_DAILY_MINUTE;
	public int ATTENDANCE_REST_PERIOD_HOUR;
	public int ATTENDANCE_DAILY_MAX_COUNT;
	public int ATTENDANCE_WEEKEND_MAX_COUNT;
	public boolean ATTENDANCE_PCROOM_USE;
	public boolean ATTENDANCE_PREMIUM_USE;
	public boolean ATTENDANCE_SPECIAL_USE;
	public boolean ATTENDANCE_GROW_USE;
	public boolean ATTENDANCE_DOMINATION_USE;
	public boolean ATTENDANCE_BRAVE_USE;
	public boolean ATTENDANCE_EXPLORER_USE;
	public int ATTENDANCE_PREMIUM_OPEN_ITEM_ID;
	public int ATTENDANCE_PREMIUM_OPEN_ITEM_COUNT;
	public int ATTENDANCE_SPECIAL_OPEN_ITEM_ID;
	public int ATTENDANCE_SPECIAL_OPEN_ITEM_COUNT;
	public int ATTENDANCE_GROW_OPEN_ITEM_ID;
	public int ATTENDANCE_GROW_OPEN_ITEM_COUNT;
	public int ATTENDANCE_DOMINATION_OPEN_ITEM_ID;
	public int ATTENDANCE_DOMINATION_OPEN_ITEM_COUNT;
	public int ATTENDANCE_BRAVE_OPEN_ITEM_ID;
	public int ATTENDANCE_BRAVE_OPEN_ITEM_COUNT;
	public int ATTENDANCE_EXPLORER_OPEN_ITEM_ID;
	public int ATTENDANCE_EXPLORER_OPEN_ITEM_COUNT;
	public int ATTENDANCE_NORMAL_EINHASAD;
	public int ATTENDANCE_PCROOM_EINHASAD;
	public int ATTENDANCE_PREMIUM_EINHASAD;
	public int ATTENDANCE_SPECIAL_EINHASAD;
	public int ATTENDANCE_GROW_EINHASAD;
	public int ATTENDANCE_DOMINATION_EINHASAD;
	public int ATTENDANCE_BRAVE_EINHASAD;
	public int ATTENDANCE_EXPLORER_EINHASAD;
	public int ATTENDANCE_RANDOM_REWARD_LEVEL_1_PROB;
	public int ATTENDANCE_RANDOM_REWARD_LEVEL_2_PROB;
	public int ATTENDANCE_RANDOM_REWARD_LEVEL_3_PROB;
	public int ATTENDANCE_RANDOM_REWARD_LEVEL_4_PROB;
	public int ATTENDANCE_RANDOM_REWARD_LEVEL_5_PROB;
	
	public void load(){
		try {
			Properties attend	= new Properties();
			InputStream is		= new FileInputStream(new File(ATTENDANCE_CONFIG_FILE));
			attend.load(is);
			is.close();
			ATTENDANCE_ACTIVE						= Boolean.parseBoolean(attend.getProperty("ATTENDANCE_ACTIVE", StringUtil.TrueString));
			ATTENDANCE_DAILY_MINUTE					= Integer.parseInt(attend.getProperty("ATTENDANCE_DAILY_MINUTE", "60"));
			ATTENDANCE_REST_PERIOD_HOUR				= Integer.parseInt(attend.getProperty("ATTENDANCE_REST_PERIOD_HOUR", "24"));
			ATTENDANCE_DAILY_MAX_COUNT				= Integer.parseInt(attend.getProperty("ATTENDANCE_DAILY_MAX_COUNT", "1"));
			ATTENDANCE_WEEKEND_MAX_COUNT			= Integer.parseInt(attend.getProperty("ATTENDANCE_WEEKEND_MAX_COUNT", "1"));
			
			ATTENDANCE_PCROOM_USE					= Boolean.parseBoolean(attend.getProperty("ATTENDANCE_PCROOM_USE", StringUtil.TrueString));
			ATTENDANCE_PREMIUM_USE					= Boolean.parseBoolean(attend.getProperty("ATTENDANCE_PREMIUM_USE", StringUtil.TrueString));
			ATTENDANCE_SPECIAL_USE					= Boolean.parseBoolean(attend.getProperty("ATTENDANCE_SPECIAL_USE", StringUtil.TrueString));
			ATTENDANCE_GROW_USE						= Boolean.parseBoolean(attend.getProperty("ATTENDANCE_GROW_USE", StringUtil.TrueString));
			ATTENDANCE_DOMINATION_USE				= Boolean.parseBoolean(attend.getProperty("ATTENDANCE_DOMINATION_USE", StringUtil.TrueString));
			ATTENDANCE_BRAVE_USE					= Boolean.parseBoolean(attend.getProperty("ATTENDANCE_BRAVE_USE", StringUtil.TrueString));
			ATTENDANCE_EXPLORER_USE					= Boolean.parseBoolean(attend.getProperty("ATTENDANCE_EXPLORER_USE", StringUtil.TrueString));
			
			ATTENDANCE_PREMIUM_OPEN_ITEM_ID			= Integer.parseInt(attend.getProperty("ATTENDANCE_PREMIUM_OPEN_ITEM_ID", "1000007"));
			ATTENDANCE_PREMIUM_OPEN_ITEM_COUNT		= Integer.parseInt(attend.getProperty("ATTENDANCE_PREMIUM_OPEN_ITEM_COUNT", "30"));
			ATTENDANCE_SPECIAL_OPEN_ITEM_ID			= Integer.parseInt(attend.getProperty("ATTENDANCE_SPECIAL_OPEN_ITEM_ID", "40308"));
			ATTENDANCE_SPECIAL_OPEN_ITEM_COUNT		= Integer.parseInt(attend.getProperty("ATTENDANCE_SPECIAL_OPEN_ITEM_COUNT", "5000000"));
			ATTENDANCE_GROW_OPEN_ITEM_ID			= Integer.parseInt(attend.getProperty("ATTENDANCE_GROW_OPEN_ITEM_ID", "43300"));
			ATTENDANCE_GROW_OPEN_ITEM_COUNT			= Integer.parseInt(attend.getProperty("ATTENDANCE_GROW_OPEN_ITEM_COUNT", "1"));
			ATTENDANCE_DOMINATION_OPEN_ITEM_ID		= Integer.parseInt(attend.getProperty("ATTENDANCE_DOMINATION_OPEN_ITEM_ID", "43300"));
			ATTENDANCE_DOMINATION_OPEN_ITEM_COUNT	= Integer.parseInt(attend.getProperty("ATTENDANCE_DOMINATION_OPEN_ITEM_COUNT", "1"));
			ATTENDANCE_BRAVE_OPEN_ITEM_ID			= Integer.parseInt(attend.getProperty("ATTENDANCE_BRAVE_OPEN_ITEM_ID", "43300"));
			ATTENDANCE_BRAVE_OPEN_ITEM_COUNT		= Integer.parseInt(attend.getProperty("ATTENDANCE_BRAVE_OPEN_ITEM_COUNT", "500"));
			ATTENDANCE_EXPLORER_OPEN_ITEM_ID		= Integer.parseInt(attend.getProperty("ATTENDANCE_EXPLORER_OPEN_ITEM_ID", "43300"));
			ATTENDANCE_EXPLORER_OPEN_ITEM_COUNT		= Integer.parseInt(attend.getProperty("ATTENDANCE_EXPLORER_OPEN_ITEM_COUNT", "500"));
			
			ATTENDANCE_NORMAL_EINHASAD				= Integer.parseInt(attend.getProperty("ATTENDANCE_NORMAL_EINHASAD", "50"));
			ATTENDANCE_PCROOM_EINHASAD				= Integer.parseInt(attend.getProperty("ATTENDANCE_PCROOM_EINHASAD", "100"));
			ATTENDANCE_PREMIUM_EINHASAD				= Integer.parseInt(attend.getProperty("ATTENDANCE_PREMIUM_EINHASAD", "100"));
			ATTENDANCE_SPECIAL_EINHASAD				= Integer.parseInt(attend.getProperty("ATTENDANCE_SPECIAL_EINHASAD", "100"));
			ATTENDANCE_GROW_EINHASAD				= Integer.parseInt(attend.getProperty("ATTENDANCE_GROW_EINHASAD", "100"));
			ATTENDANCE_DOMINATION_EINHASAD			= Integer.parseInt(attend.getProperty("ATTENDANCE_DOMINATION_EINHASAD", "100"));
			ATTENDANCE_BRAVE_EINHASAD				= Integer.parseInt(attend.getProperty("ATTENDANCE_BRAVE_EINHASAD", "100"));
			ATTENDANCE_EXPLORER_EINHASAD			= Integer.parseInt(attend.getProperty("ATTENDANCE_EXPLORER_EINHASAD", "100"));
		
			ATTENDANCE_RANDOM_REWARD_LEVEL_1_PROB	= Integer.parseInt(attend.getProperty("ATTENDANCE_RANDOM_REWARD_LEVEL_1_PROB", "50"));
			ATTENDANCE_RANDOM_REWARD_LEVEL_2_PROB	= Integer.parseInt(attend.getProperty("ATTENDANCE_RANDOM_REWARD_LEVEL_2_PROB", "35"));
			ATTENDANCE_RANDOM_REWARD_LEVEL_3_PROB	= Integer.parseInt(attend.getProperty("ATTENDANCE_RANDOM_REWARD_LEVEL_3_PROB", "10"));
			ATTENDANCE_RANDOM_REWARD_LEVEL_4_PROB	= Integer.parseInt(attend.getProperty("ATTENDANCE_RANDOM_REWARD_LEVEL_4_PROB", "4"));
			ATTENDANCE_RANDOM_REWARD_LEVEL_5_PROB	= Integer.parseInt(attend.getProperty("ATTENDANCE_RANDOM_REWARD_LEVEL_5_PROB", "1"));
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw new Error("Failed to Load " + ATTENDANCE_CONFIG_FILE + " File.");
		}
	}

}

