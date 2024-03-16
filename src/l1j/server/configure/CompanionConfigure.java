package l1j.server.configure;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CompanionConfigure {
	private static final Logger _log = Logger.getLogger(CompanionConfigure.class.getName());
	private static final String COMPANION_SETTINGS_CONFIG_FILE	= "./config/companion.properties";
	
	public int MAX_LEVEL;
	public double FRIENDSHIP_GUAGE_RATE;
	public int FRIENDSHIP_MARBLE_VALUE;
	public int COMBO_ENABLE_POINT;
	public double COMBO_DELAY_REDUCE_RATE;
	public int COMBO_DURATION_SECOND;
	public int STAT_MAX_VALUE;
	public int ELIXIR_USE_LEVEL;
	public int ELIXIR_MAX_VALUE;
	
	public void load(){
		try {
			Properties etc	= new Properties();
			InputStream is			= new FileInputStream(new File(COMPANION_SETTINGS_CONFIG_FILE));
			etc.load(is);
			is.close();

			MAX_LEVEL				= Integer.parseInt(etc.getProperty("MAX_LEVEL", "80"));
			FRIENDSHIP_GUAGE_RATE	= Double.parseDouble(etc.getProperty("FRIENDSHIP_GUAGE_RATE", "1.0"));
			FRIENDSHIP_MARBLE_VALUE	= Integer.parseInt(etc.getProperty("FRIENDSHIP_MARBLE_VALUE", "1"));
			COMBO_ENABLE_POINT		= Integer.parseInt(etc.getProperty("COMBO_ENABLE_POINT", "100000"));
			COMBO_DELAY_REDUCE_RATE	= Double.parseDouble(etc.getProperty("COMBO_DELAY_REDUCE_RATE", "46.4"));
			COMBO_DURATION_SECOND	= Integer.parseInt(etc.getProperty("COMBO_DURATION_SECOND", "60"));
			STAT_MAX_VALUE			= Integer.parseInt(etc.getProperty("STAT_MAX_VALUE", "30"));
			ELIXIR_USE_LEVEL		= Integer.parseInt(etc.getProperty("ELIXIR_USE_LEVEL", "50"));
			ELIXIR_MAX_VALUE		= Integer.parseInt(etc.getProperty("ELIXIR_MAX_VALUE", "6"));
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw new Error("Failed to Load " + COMPANION_SETTINGS_CONFIG_FILE + " File.");
		}
	}
}

