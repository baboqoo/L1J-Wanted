package l1j.server.configure;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.utils.StringUtil;

public class DeathPenaltyConfigure {
	private static final Logger _log = Logger.getLogger(DeathPenaltyConfigure.class.getName());
	private static final String DEATH_PENALTY_CONFIG_FILE	= "./config/deathpenalty.properties";

	public boolean PVP_ITEM_DROP;
	public int PVP_ITEM_DROP_PERCENT;
	public boolean PVE_ITEM_DROP;
	public int PVE_ITEM_DROP_PERCENT;
	
	public int REPAIR_STORAGE_LIMIT_SIZE;
	public int REPAIR_LIMIT_TIME_SECOND;
	public boolean EXP_PENALTY_VALUE_FIX;
	public float EXP_PENALTY_VALUE_FIX_PERCENT;
	public double REPAIR_EXP_COST_RATE;
	public boolean ITEM_PENALTY_LOST_DELETE_ACTIVE;
	public boolean ITEM_PENALTY_LOST_REPAIR_ENABLE;
	public int REPAIR_ITEM_DEFAULT_COST_VALUE;
	public int REPAIR_ITEM_LEGEND_COST_VALUE;
	public int REPAIR_ITEM_MYTH_COST_VALUE;
	
	public void load(){
		try {
			Properties 	setting 			= new Properties();
			InputStream is					= new FileInputStream(new File(DEATH_PENALTY_CONFIG_FILE));
			setting.load(is);
			is.close();
			PVP_ITEM_DROP					= Boolean.parseBoolean(setting.getProperty("PVP_ITEM_DROP", StringUtil.FalseString));
			PVP_ITEM_DROP_PERCENT			= Integer.parseInt(setting.getProperty("PVP_ITEM_DROP_PERCENT", "70"));
			PVE_ITEM_DROP					= Boolean.parseBoolean(setting.getProperty("PVE_ITEM_DROP", StringUtil.FalseString));
			PVE_ITEM_DROP_PERCENT			= Integer.parseInt(setting.getProperty("PVE_ITEM_DROP_PERCENT", "70"));
			
			REPAIR_STORAGE_LIMIT_SIZE		= Integer.parseInt(setting.getProperty("REPAIR_STORAGE_LIMIT_SIZE", "20"));
			REPAIR_LIMIT_TIME_SECOND		= Integer.parseInt(setting.getProperty("REPAIR_LIMIT_TIME_SECOND", "86400"));
			EXP_PENALTY_VALUE_FIX			= Boolean.parseBoolean(setting.getProperty("EXP_PENALTY_VALUE_FIX", StringUtil.FalseString));
			EXP_PENALTY_VALUE_FIX_PERCENT	= Float.parseFloat(setting.getProperty("EXP_PENALTY_VALUE_FIX_PERCENT", "10.0"));
			REPAIR_EXP_COST_RATE			= Double.parseDouble(setting.getProperty("REPAIR_EXP_COST_RATE", "1.0"));
			ITEM_PENALTY_LOST_DELETE_ACTIVE	= Boolean.parseBoolean(setting.getProperty("ITEM_PENALTY_LOST_DELETE_ACTIVE", StringUtil.FalseString));
			ITEM_PENALTY_LOST_REPAIR_ENABLE	= Boolean.parseBoolean(setting.getProperty("ITEM_PENALTY_LOST_REPAIR_ENABLE", StringUtil.FalseString));
			REPAIR_ITEM_DEFAULT_COST_VALUE	= Integer.parseInt(setting.getProperty("REPAIR_ITEM_DEFAULT_COST_VALUE", "2000000"));
			REPAIR_ITEM_LEGEND_COST_VALUE	= Integer.parseInt(setting.getProperty("REPAIR_ITEM_LEGEND_COST_VALUE", "100000000"));
			REPAIR_ITEM_MYTH_COST_VALUE		= Integer.parseInt(setting.getProperty("REPAIR_ITEM_MYTH_COST_VALUE", "200000000"));
		} catch(Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw new Error("Failed to Load " + DEATH_PENALTY_CONFIG_FILE + " File.");
		}
	}
}

