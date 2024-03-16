package l1j.server.configure;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.utils.StringUtil;

public class TJCouponConfigure {
	private static final Logger _log = Logger.getLogger(TJCouponConfigure.class.getName());
	private static final String TJ_COUPON_CONFIG_FILE	= "./config/tjcoupon.properties";

	public boolean TJ_COUPON_ENABLE;
	public int TJ_COUPON_USE_LEVEL;
	public int TJ_COUPON_ITEMID;
	public int TJ_COUPON_LOG_SIZE;
	public int TJ_COUPON_WEAPON_ENCHANT;
	public int TJ_COUPON_ARMOR_ENCHANT;
	public int TJ_COUPON_ACCESSARY_ENCHANT;
	
	public void load(){
		try {
			Properties 	settings 		= new Properties();
			InputStream is				= new FileInputStream(new File(TJ_COUPON_CONFIG_FILE));
			settings.load(is);
			is.close();
			
			TJ_COUPON_ENABLE			= Boolean.parseBoolean(settings.getProperty("TJ_COUPON_ENABLE", StringUtil.FalseString));
			TJ_COUPON_USE_LEVEL			= Integer.parseInt(settings.getProperty("TJ_COUPON_USE_LEVEL", "80"));
			TJ_COUPON_ITEMID			= Integer.parseInt(settings.getProperty("TJ_COUPON_ITEMID", StringUtil.ZeroString));
			TJ_COUPON_LOG_SIZE			= Integer.parseInt(settings.getProperty("TJ_COUPON_LOG_SIZE", "20"));
			TJ_COUPON_WEAPON_ENCHANT	= Integer.parseInt(settings.getProperty("TJ_COUPON_WEAPON_ENCHANT", StringUtil.ZeroString));
			TJ_COUPON_ARMOR_ENCHANT		= Integer.parseInt(settings.getProperty("TJ_COUPON_ARMOR_ENCHANT", StringUtil.ZeroString));
			TJ_COUPON_ACCESSARY_ENCHANT	= Integer.parseInt(settings.getProperty("TJ_COUPON_ACCESSARY_ENCHANT", StringUtil.ZeroString));
		} catch(Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw new Error("Failed to Load " + TJ_COUPON_CONFIG_FILE + " File.");
		}
	}
}

