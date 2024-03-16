package l1j.server.configure;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OccupyConfigure {
	private static final Logger _log = Logger.getLogger(OccupyConfigure.class.getName());
	private static final String OCCUPY_CONFIG_FILE	= "./config/occupy.properties";

	public int OCCUPY_ADENA;
	public int OCCUPY_DEFFENS_MISSION_TIME;
	public int OCCUPY_OFFENS_MISSION_TIME;
	public int OCCUPY_PLAY_TIME;
	public int OCCUPY_BOSS_TIME;
	
	public void load(){
		try {
			Properties 	occupy 			= new Properties();
			InputStream is				= new FileInputStream(new File(OCCUPY_CONFIG_FILE));
			occupy.load(is);
			is.close();
			OCCUPY_ADENA				= Integer.parseInt(occupy.getProperty("OCCUPY_ADENA", "1200000000"));
			OCCUPY_DEFFENS_MISSION_TIME	= Integer.parseInt(occupy.getProperty("OCCUPY_DEFFENS_MISSION_TIME", "600"));
			OCCUPY_OFFENS_MISSION_TIME	= Integer.parseInt(occupy.getProperty("OCCUPY_OFFENS_MISSION_TIME", "180"));
			OCCUPY_PLAY_TIME			= Integer.parseInt(occupy.getProperty("OCCUPY_PLAY_TIME", "2400"));
			OCCUPY_BOSS_TIME			= Integer.parseInt(occupy.getProperty("OCCUPY_BOSS_TIME", "1200"));
		} catch(Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw new Error("Failed to Load " + OCCUPY_CONFIG_FILE + " File.");
		}
	}
}

