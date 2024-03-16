package l1j.server.configure;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.utils.StringUtil;

public class RankingConfigure {
	private static final Logger _log = Logger.getLogger(RankingConfigure.class.getName());
	private static final String RANKING_CONFIG_FILE	= "./config/ranking.properties";
	
	public boolean RANKING_SYSTEM_ACTIVE;
	public int RANKING_UPDATE_HOUR;
	public int RANK_GRACE_ITEM_REUSE_INTERVAL;
	public int RANKING_INCLUDE_LEVEL;
	public int RANKING_TOTAL_CALC_RANGE;
	public int RANKING_CLASS_CALC_RANGE;
	
	public void load(){
		try {
			Properties setting				= new Properties();
			InputStream is					= new FileInputStream(new File(RANKING_CONFIG_FILE));
			setting.load(is);
			is.close();
			
			RANKING_SYSTEM_ACTIVE			= Boolean.parseBoolean(setting.getProperty("RANKING_SYSTEM_ACTIVE", StringUtil.TrueString));
			RANKING_UPDATE_HOUR				= Integer.parseInt(setting.getProperty("RANK_UPDATE_HOUR", "6"));
			RANK_GRACE_ITEM_REUSE_INTERVAL	= Integer.parseInt(setting.getProperty("RANK_GRACE_ITEM_REUSE_INTERVAL", "60"));
			RANKING_INCLUDE_LEVEL			= Integer.parseInt(setting.getProperty("RANKING_INCLUDE_LEVEL", "80"));
			RANKING_TOTAL_CALC_RANGE		= Integer.parseInt(setting.getProperty("RANKING_TOTAL_CALC_RANGE", "100"));
			RANKING_CLASS_CALC_RANGE		= Integer.parseInt(setting.getProperty("RANKING_CLASS_CALC_RANGE", "100"));
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw new Error("Failed to Load " + RANKING_CONFIG_FILE + " File.");
		}
	}
}

