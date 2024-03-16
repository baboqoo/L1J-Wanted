package l1j.server.configure;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RevengeConfigure {
	private static final Logger _log = Logger.getLogger(RevengeConfigure.class.getName());
	private static final String REVENGE_CONFIG_FILE	= "./config/revenge.properties";
	
	public int REVENGE_DURATION_SECOND;
	public int REVENGE_PURSUIT_DURATION_SECOND;
	public int REVENGE_ACTION_COST;
	public int REVENGE_PURSUIT_MAX_COUNT;
	public int REVENGE_TAUNT_MAX_COUNT;
	
	public void load(){
		try {
			Properties setting				= new Properties();
			InputStream is					= new FileInputStream(new File(REVENGE_CONFIG_FILE));
			setting.load(is);
			is.close();
			
			REVENGE_DURATION_SECOND			= Integer.parseInt(setting.getProperty("REVENGE_DURATION_SECOND", "7200"));
			REVENGE_PURSUIT_DURATION_SECOND	= Integer.parseInt(setting.getProperty("REVENGE_PURSUIT_DURATION_SECOND", "600"));
			REVENGE_ACTION_COST				= Integer.parseInt(setting.getProperty("REVENGE_ACTION_COST", "10000"));
			REVENGE_PURSUIT_MAX_COUNT		= Integer.parseInt(setting.getProperty("REVENGE_PURSUIT_MAX_COUNT", "3"));
			REVENGE_TAUNT_MAX_COUNT			= Integer.parseInt(setting.getProperty("REVENGE_TAUNT_MAX_COUNT", "1"));
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw new Error("Failed to Load " + REVENGE_CONFIG_FILE + " File.");
		}
	}
}

