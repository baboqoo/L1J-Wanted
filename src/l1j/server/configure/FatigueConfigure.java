package l1j.server.configure;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.utils.StringUtil;

public class FatigueConfigure {
	private static final Logger _log = Logger.getLogger(FatigueConfigure.class.getName());
	private static final String FATIGUE_CONFIG_FILE	= "./config/fatigue.properties";
	
	public boolean FATIGUE_ACTIVE;
	public boolean FATIGUE_EINHASAD;
	public int FATIGUE_FIRST_VALUE;
	public int FATIGUE_NEXT_VALUE;
	public int FATIGUE_DURATION;
	
	public void load(){
		try {
			Properties setting		= new Properties();
			InputStream is			= new FileInputStream(new File(FATIGUE_CONFIG_FILE));
			setting.load(is);
			is.close();
			
			FATIGUE_ACTIVE			= Boolean.parseBoolean(setting.getProperty("FATIGUE_ACTIVE", StringUtil.TrueString));
			FATIGUE_EINHASAD		= Boolean.parseBoolean(setting.getProperty("FATIGUE_EINHASAD", StringUtil.TrueString));
			FATIGUE_FIRST_VALUE		= Integer.parseInt(setting.getProperty("FATIGUE_FIRST_VALUE", "600"));
			FATIGUE_NEXT_VALUE		= Integer.parseInt(setting.getProperty("FATIGUE_NEXT_VALUE", "60"));
			FATIGUE_DURATION		= Integer.parseInt(setting.getProperty("FATIGUE_DURATION", "10"));
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw new Error("Failed to Load " + FATIGUE_CONFIG_FILE + " File.");
		}
	}

}

