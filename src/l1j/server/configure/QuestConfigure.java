package l1j.server.configure;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.utils.StringUtil;

public class QuestConfigure {
	private static final Logger _log = Logger.getLogger(QuestConfigure.class.getName());
	private static final String QUEST_CONFIG_FILE	= "./config/quest.properties";
	
	public int WQ_UPDATE_TYPE;
	public int WQ_UPDATE_WEEK;
	public int WQ_UPDATE_TIME;
	public boolean BEGINNER_QUEST_ACTIVE;
	public int BEGINNER_QUEST_LIMIT_LEVEL;
	public boolean BEGINNER_QUEST_FAST_PROGRESS;
	public boolean HUNTING_QUEST_ACTIVE;
	public int HUNTING_QUEST_REGIST_COUNT;
	public int HUNTING_QUEST_CLEAR_VALUE;
	
	public void load(){
		try {
			Properties quest				= new Properties();
			InputStream is					= new FileInputStream(new File(QUEST_CONFIG_FILE));
			quest.load(is);
			is.close();
			WQ_UPDATE_TYPE					= Integer.parseInt(quest.getProperty("WeekQuest_UpdateType", "1"));
			WQ_UPDATE_WEEK					= Integer.parseInt(quest.getProperty("WeekQuest_UpdateWeek", "4"));
			WQ_UPDATE_TIME					= Integer.parseInt(quest.getProperty("WeekQuest_UpdateTime", "5"));
			BEGINNER_QUEST_ACTIVE			= Boolean.parseBoolean(quest.getProperty("BEGINNER_QUEST_ACTIVE", StringUtil.TrueString));
			BEGINNER_QUEST_LIMIT_LEVEL		= Integer.parseInt(quest.getProperty("BEGINNER_QUEST_LIMIT_LEVEL", "100"));
			BEGINNER_QUEST_FAST_PROGRESS	= Boolean.parseBoolean(quest.getProperty("BEGINNER_QUEST_FAST_PROGRESS", StringUtil.FalseString));
			HUNTING_QUEST_ACTIVE			= Boolean.parseBoolean(quest.getProperty("HUNTING_QUEST_ACTIVE", StringUtil.TrueString));
			HUNTING_QUEST_REGIST_COUNT		= Integer.parseInt(quest.getProperty("HUNTING_QUEST_REGIST_COUNT", "3"));
			HUNTING_QUEST_CLEAR_VALUE		= Integer.parseInt(quest.getProperty("HUNTING_QUEST_CLEAR_VALUE", "150"));
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw new Error("Failed to Load " + QUEST_CONFIG_FILE + " File.");
		}
	}
}

