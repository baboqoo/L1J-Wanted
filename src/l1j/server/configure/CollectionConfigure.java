package l1j.server.configure;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.utils.StringUtil;

public class CollectionConfigure {
	private static final Logger _log = Logger.getLogger(CollectionConfigure.class.getName());
	private static final String COLLECTION_CONFIG_FILE	= "./config/collection.properties";
	
	public boolean FAVOR_BOOK_ACTIVE;
	public boolean TIME_COLLECTION_ACTIVE;
	public int TIME_COLLECTION_SHOP_NPC_ID;
	public int TIME_COLLECTION_CRAFT_NPC_ID;
	
	public void load(){
		try {
			Properties 	settings 			= new Properties();
			InputStream is					= new FileInputStream(new File(COLLECTION_CONFIG_FILE));
			settings.load(is);
			is.close();
			
			FAVOR_BOOK_ACTIVE				= Boolean.parseBoolean(settings.getProperty("FAVOR_BOOK_ACTIVE", StringUtil.TrueString));
			TIME_COLLECTION_ACTIVE			= Boolean.parseBoolean(settings.getProperty("TIME_COLLECTION_ACTIVE", StringUtil.TrueString));
			TIME_COLLECTION_SHOP_NPC_ID		= Integer.parseInt(settings.getProperty("TIME_COLLECTION_SHOP_NPC_ID", StringUtil.ZeroString));
			TIME_COLLECTION_CRAFT_NPC_ID	= Integer.parseInt(settings.getProperty("TIME_COLLECTION_CRAFT_NPC_ID", StringUtil.ZeroString));
		} catch(Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw new Error("Failed to Load " + COLLECTION_CONFIG_FILE + " File.");
		}
	}
}

