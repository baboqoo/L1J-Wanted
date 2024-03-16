package l1j.server.configure;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.utils.StringUtil;

public class EventpushConfigure {
	private static final Logger _log = Logger.getLogger(EventpushConfigure.class.getName());
	private static final String EVENTPUSH_CONFIG_FILE	= "./config/eventpush.properties";
	
	public int LIMIT_SIZE;
	public int ENABLE_SECOND;
	public boolean HOT_TIME_ENABLE;
	public ArrayList<Integer> HOT_TIME_HOUR = new ArrayList<Integer>();
	public int[] HOT_TIME_ITEM_ID;
	public int[] HOT_TIME_ITEM_COUNT;
	
	public void load(){
		HOT_TIME_HOUR.clear();
		HOT_TIME_ITEM_ID = HOT_TIME_ITEM_COUNT = null;
		try {
			Properties setting		= new Properties();
			InputStream is			= new FileInputStream(new File(EVENTPUSH_CONFIG_FILE));
			setting.load(is);
			is.close();
			
			LIMIT_SIZE				= Integer.parseInt(setting.getProperty("LIMIT_SIZE", "30"));
			ENABLE_SECOND			= Integer.parseInt(setting.getProperty("ENABLE_SECOND", "7200"));
			HOT_TIME_ENABLE			= Boolean.parseBoolean(setting.getProperty("HOT_TIME_ENABLE", StringUtil.FalseString));
			String hot_time_hour	= setting.getProperty("HOT_TIME_HOUR", StringUtil.EmptyString);
			if (!StringUtil.isNullOrEmpty(hot_time_hour)) {
				String[] array = hot_time_hour.split(StringUtil.CommaString);
				for (int i=0; i<array.length; i++) {
					HOT_TIME_HOUR.add(Integer.parseInt(array[i].trim()));
				}
			}
			String hot_time_item_id		= setting.getProperty("HOT_TIME_ITEM_ID", StringUtil.EmptyString);
			String hot_time_item_count	= setting.getProperty("HOT_TIME_ITEM_COUNT", StringUtil.EmptyString);
			if (!StringUtil.isNullOrEmpty(hot_time_item_id)) {
				String[] id_array		= hot_time_item_id.split(StringUtil.CommaString);
				String[] count_array	= hot_time_item_count.split(StringUtil.CommaString);
				if (id_array.length != count_array.length) {
					throw new Error("Failed to Load " + EVENTPUSH_CONFIG_FILE + " File. HOT_TIME_ITEM_LENGTH");
				}
				HOT_TIME_ITEM_ID		= new int[id_array.length];
				HOT_TIME_ITEM_COUNT		= new int[count_array.length];
				for (int i=0; i<id_array.length; i++) {
					HOT_TIME_ITEM_ID[i]		= Integer.parseInt(id_array[i].trim());
					HOT_TIME_ITEM_COUNT[i]	= Integer.parseInt(count_array[i].trim());
				}
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw new Error("Failed to Load " + EVENTPUSH_CONFIG_FILE + " File.");
		}
	}

}

