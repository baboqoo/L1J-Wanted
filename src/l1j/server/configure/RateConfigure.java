package l1j.server.configure;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RateConfigure {
	private static final Logger _log = Logger.getLogger(RateConfigure.class.getName());
	private static final String RATES_CONFIG_FILE	= "./config/rates.properties";
	
	public double RATE_XP;
	public double RATE_ALIGNMENT;
	public double RATE_KARMA;
	public double RATE_DROP_ADENA;
	public double RATE_DROP_KNIGHT_COIN;
	public double RATE_DROP_ITEMS;
	public double RATE_DROP_RABBIT;//신묘년 이벤트
	public double RATE_WEIGHT_LIMIT;
	public double RATE_WEIGHT_LIMIT_PET;
	public double RATE_SHOP_SELLING_PRICE;
	public double RATE_SHOP_PURCHASING_PRICE;
	
	public void load(){
		try {
			Properties settings					= new Properties();
			FileReader is						= new FileReader(new File(RATES_CONFIG_FILE));
			settings.load(is);
			is.close();
			
			RATE_XP								= Double.parseDouble(settings.getProperty("RateXp", "1.0"));
			RATE_ALIGNMENT						= Double.parseDouble(settings.getProperty("RateAlignment", "1.0"));
			RATE_KARMA							= Double.parseDouble(settings.getProperty("RateKarma", "1.0"));
			RATE_DROP_ADENA						= Double.parseDouble(settings.getProperty("RateDropAdena", "1.0"));
			RATE_DROP_KNIGHT_COIN				= Double.parseDouble(settings.getProperty("RateDropKnightCoin", "1.0"));
			RATE_DROP_ITEMS						= Double.parseDouble(settings.getProperty("RateDropItems", "1.0"));
			RATE_DROP_RABBIT					= Double.parseDouble(settings.getProperty("RateDropRabbit", "10.0"));
			RATE_WEIGHT_LIMIT					= Double.parseDouble(settings.getProperty("RateWeightLimit", "1"));
			RATE_WEIGHT_LIMIT_PET				= Double.parseDouble(settings.getProperty("RateWeightLimitforPet", "1"));
			RATE_SHOP_SELLING_PRICE				= Double.parseDouble(settings.getProperty("RateShopSellingPrice", "1.0"));
			RATE_SHOP_PURCHASING_PRICE			= Double.parseDouble(settings.getProperty("RateShopPurchasingPrice", "1.0"));
		} catch (Exception e) {		
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw new Error("Failed to Load " + RATES_CONFIG_FILE + " File.");
		}
	}
}

