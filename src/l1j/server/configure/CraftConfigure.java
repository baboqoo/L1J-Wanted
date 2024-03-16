package l1j.server.configure;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.utils.StringUtil;

public class CraftConfigure {
	private static final Logger _log = Logger.getLogger(CraftConfigure.class.getName());
	private static final String CRAFT_CONFIG_FILE	= "./config/craft.properties";
	
	public boolean CRAFT_DB_PROBABOLITY_MILLION_USE;
	public double CRAFT_PROBABOLITY_MILLION_RATE;
	public double CRAFT_EVENT_SUCCESS_OUTPUT_PROB;
	
	public void load(){
		try {
			Properties setting					= new Properties();
			InputStream is						= new FileInputStream(new File(CRAFT_CONFIG_FILE));
			setting.load(is);
			is.close();
			
			CRAFT_DB_PROBABOLITY_MILLION_USE	= Boolean.parseBoolean(setting.getProperty("CRAFT_DB_PROBABOLITY_MILLION_USE", StringUtil.FalseString));
			CRAFT_PROBABOLITY_MILLION_RATE		= Double.parseDouble(setting.getProperty("CRAFT_PROBABOLITY_MILLION_RATE", "1.0"));
			CRAFT_EVENT_SUCCESS_OUTPUT_PROB		= Integer.parseInt(setting.getProperty("CRAFT_EVENT_SUCCESS_OUTPUT_PROB", "10"));
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw new Error("Failed to Load " + CRAFT_CONFIG_FILE + " File.");
		}
	}
}

