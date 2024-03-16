package l1j.server.configure;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.utils.StringUtil;

public class InterServerConfigure {
	private static final Logger _log = Logger.getLogger(InterServerConfigure.class.getName());
	private static final String INTER_SERVER_CONFIG_FILE	= "./config/interserver.properties";
	
	public boolean INTER_SERVER_ACTIVE;
	public boolean INTER_SERVER_INNER_SETTING;
	public String INTER_SERVER_OUTER_IP;
	public int INTER_SERVER_OUTER_PORT;
	
	public void load(){
		try {
			Properties 	settings 				= new Properties();
			InputStream is						= new FileInputStream(new File(INTER_SERVER_CONFIG_FILE));
			settings.load(is);
			is.close();
			
			INTER_SERVER_ACTIVE					= Boolean.parseBoolean(settings.getProperty("INTER_SERVER_ACTIVE", StringUtil.FalseString));
			INTER_SERVER_INNER_SETTING			= Boolean.parseBoolean(settings.getProperty("INTER_SERVER_INNER_SETTING", StringUtil.FalseString));
			INTER_SERVER_OUTER_IP				= settings.getProperty("INTER_SERVER_OUTER_IP", "127.0.0.1");
			INTER_SERVER_OUTER_PORT				= Integer.parseInt(settings.getProperty("INTER_SERVER_OUTER_PORT", "2000"));
		} catch(Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw new Error("Failed to Load " + INTER_SERVER_CONFIG_FILE + " File.");
		}
	}
}

