package l1j.server.configure;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.utils.StringUtil;

public class EinhasadConfigure {
	private static final Logger _log = Logger.getLogger(EinhasadConfigure.class.getName());
	private static final String EINHASAD_CONFIG_FILE	= "./config/einhasad.properties";

	public int REST_EXP_DEFAULT_RATION;
	public int REST_EXP_LIMIT_CHARGE_VALUE;
	public int REST_EXP_DECREASE_RATE;
	public int REST_EXP_REDUCE_EFFICIENCY_PERCENT;
	public boolean REST_EXP_ITEM_PENALTY;
	
	public int EINHASAD_POINT_RATE;
	public int EINHASAD_POINT_CARD_HYPER_PROB;
	public int EINHASAD_POINT_LIMIT_CHARGE_VALUE;
	
	public void load(){
		try {
			Properties 	settings 				= new Properties();
			InputStream is						= new FileInputStream(new File(EINHASAD_CONFIG_FILE));
			settings.load(is);
			is.close();
			
			REST_EXP_DEFAULT_RATION				= Integer.parseInt(settings.getProperty("REST_EXP_DEFAULT_RATION", "10000"));
			REST_EXP_LIMIT_CHARGE_VALUE			= Integer.parseInt(settings.getProperty("REST_EXP_LIMIT_CHARGE_VALUE", "8000")) * REST_EXP_DEFAULT_RATION;
			REST_EXP_DECREASE_RATE				= Integer.parseInt(settings.getProperty("REST_EXP_DECREASE_RATE", "1"));
			REST_EXP_REDUCE_EFFICIENCY_PERCENT	= Integer.parseInt(settings.getProperty("REST_EXP_REDUCE_EFFICIENCY_PERCENT", "100"));
			REST_EXP_ITEM_PENALTY				= Boolean.parseBoolean(settings.getProperty("REST_EXP_ITEM_PENALTY", StringUtil.TrueString));
			
			EINHASAD_POINT_RATE					= Integer.parseInt(settings.getProperty("EINHASAD_POINT_RATE", "10"));
			EINHASAD_POINT_CARD_HYPER_PROB		= Integer.parseInt(settings.getProperty("EINHASAD_POINT_CARD_HYPER_PROB", "1"));
			EINHASAD_POINT_LIMIT_CHARGE_VALUE	= Integer.parseInt(settings.getProperty("EINHASAD_POINT_LIMIT_CHARGE_VALUE", "100000000"));
		} catch(Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw new Error("Failed to Load " + EINHASAD_CONFIG_FILE + " File.");
		}
	}
}

