package l1j.server.configure;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.utils.CharsetUtil;
import l1j.server.server.utils.StringUtil;

public class PlaySupportConfigure {
	private static final Logger _log = Logger.getLogger(PlaySupportConfigure.class.getName());
	private static final String PSS_CONFIG_FILE	= "./config/playsupport.properties";
	
	public boolean PLAY_SUPPORT_ACTIVE;
	public int PLAY_SUPPORT_AUTH_ITEM_ID;
	public int PLAY_SUPPORT_AUTH_ITEM_DURATION_MINUT;
	public String PLAY_SUPPORT_BUCKET_NAME;
	public boolean PLAY_SUPPORT_TIME_LIMITED;
	public int PLAY_SUPPORT_TIME_DAY_ADD;
	public int PLAY_SUPPORT_TIME_MAX;
	public boolean PLAY_SUPPORT_ALL_ACTIVE;
	
	public void load(){
		try {
			Properties 	settings 					= new Properties();
			InputStream is							= new FileInputStream(new File(PSS_CONFIG_FILE));
			settings.load(is);
			is.close();
			PLAY_SUPPORT_ACTIVE						= Boolean.parseBoolean(settings.getProperty("PLAY_SUPPORT_ACTIVE", StringUtil.FalseString));
			PLAY_SUPPORT_AUTH_ITEM_ID				= Integer.parseInt(settings.getProperty("PLAY_SUPPORT_AUTH_ITEM_ID", StringUtil.ZeroString));
			PLAY_SUPPORT_AUTH_ITEM_DURATION_MINUT	= Integer.parseInt(settings.getProperty("PLAY_SUPPORT_AUTH_ITEM_DURATION_MINUT", StringUtil.ZeroString));
			PLAY_SUPPORT_BUCKET_NAME				= new String(settings.getProperty("PLAY_SUPPORT_BUCKET_NAME", "LIVE_LIN_PSSCONFIG_24003492").getBytes(CharsetUtil.ISO_8859_1_STR), CharsetUtil.EUC_KR_STR);
			PLAY_SUPPORT_TIME_LIMITED				= Boolean.parseBoolean(settings.getProperty("PLAY_SUPPORT_TIME_LIMITED", StringUtil.FalseString));
			PLAY_SUPPORT_TIME_DAY_ADD				= Integer.parseInt(settings.getProperty("PLAY_SUPPORT_TIME_DAY_ADD", StringUtil.ZeroString));
			PLAY_SUPPORT_TIME_MAX					= Integer.parseInt(settings.getProperty("PLAY_SUPPORT_TIME_MAX", StringUtil.ZeroString));
			PLAY_SUPPORT_ALL_ACTIVE					= Boolean.parseBoolean(settings.getProperty("PLAY_SUPPORT_ALL_ACTIVE", StringUtil.FalseString));
		} catch(Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw new Error("Failed to Load " + PSS_CONFIG_FILE + " File.");
		}
	}
}

