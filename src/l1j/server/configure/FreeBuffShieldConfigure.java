package l1j.server.configure;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.utils.StringUtil;

public class FreeBuffShieldConfigure {
	private static final Logger _log = Logger.getLogger(FreeBuffShieldConfigure.class.getName());
	private static final String CONFIG_FILE	= "./config/freebuffshield.properties";
	
	public boolean FREE_BUFF_SHIELD_ENABLE;
	public int FAVOR_LOCKED_TIME_SECOND;
	
	public boolean FEATHER_ACTIVE;
	public int FEATHER_TIME;
	public int FEATHER_COUNT;
	public int GOLD_FEATHER_TIME;
	public int GOLD_FEATHER_COUNT;
	public int GOLD_FEATHER_INVEN_MAX_VALUE;
	public int GOLD_FEATHER_DAILY_MAX_VALUE;
	public int PCCAFE_FAVOR_DAILY_COUNT;
	
	public int FREE_FAVOR_DAILY_ARCA_1_COUNT;
	public int FREE_FAVOR_DAILY_ARCA_3_COUNT;
	
	public int EVENT_FAVOR_DAILY_COUNT;
	
	public void load(){
		try {
			Properties 	settings 			= new Properties();
			InputStream is					= new FileInputStream(new File(CONFIG_FILE));
			settings.load(is);
			is.close();
			
			FREE_BUFF_SHIELD_ENABLE			= Boolean.parseBoolean(settings.getProperty("FREE_BUFF_SHIELD_ENABLE", StringUtil.TrueString));
			FAVOR_LOCKED_TIME_SECOND		= Integer.parseInt(settings.getProperty("FAVOR_LOCKED_TIME_SECOND", "1800"));
			
			FEATHER_ACTIVE					= Boolean.parseBoolean(settings.getProperty("FEATHER_ACTIVE", StringUtil.TrueString));
			FEATHER_TIME					= Integer.parseInt(settings.getProperty("FEATHER_TIME", "15"));
			FEATHER_COUNT					= Integer.parseInt(settings.getProperty("FEATHER_COUNT", "6"));
			GOLD_FEATHER_TIME				= Integer.parseInt(settings.getProperty("GOLD_FEATHER_TIME", "12"));
			GOLD_FEATHER_COUNT				= Integer.parseInt(settings.getProperty("GOLD_FEATHER_COUNT", "2"));
			GOLD_FEATHER_INVEN_MAX_VALUE	= Integer.parseInt(settings.getProperty("GOLD_FEATHER_INVEN_MAX_VALUE", "300"));
			GOLD_FEATHER_DAILY_MAX_VALUE	= Integer.parseInt(settings.getProperty("GOLD_FEATHER_DAILY_MAX_VALUE", "30"));
			PCCAFE_FAVOR_DAILY_COUNT		= Integer.parseInt(settings.getProperty("PCCAFE_FAVOR_DAILY_COUNT", "3"));
			
			FREE_FAVOR_DAILY_ARCA_1_COUNT	= Integer.parseInt(settings.getProperty("FREE_FAVOR_DAILY_ARCA_1_COUNT", "1"));
			FREE_FAVOR_DAILY_ARCA_3_COUNT	= Integer.parseInt(settings.getProperty("FREE_FAVOR_DAILY_ARCA_3_COUNT", "2"));
			
			EVENT_FAVOR_DAILY_COUNT			= Integer.parseInt(settings.getProperty("EVENT_FAVOR_DAILY_COUNT", "0"));
		} catch(Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw new Error("Failed to Load " + CONFIG_FILE + " File.");
		}
	}
}

