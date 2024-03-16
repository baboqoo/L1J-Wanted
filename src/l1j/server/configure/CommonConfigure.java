package l1j.server.configure;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.utils.StringUtil;

public class CommonConfigure {
	private static final Logger _log = Logger.getLogger(CommonConfigure.class.getName());
	private static final String COMMON_CONFIG_FILE	= "./config/common.properties";
	
	public boolean COMMON_CRAFT_BIN_UPDATE;
	public boolean COMMON_ITEM_BIN_UPDATE;
	public boolean COMMON_NPC_BIN_UPDATE;
	public boolean COMMON_SPELL_BIN_UPDATE;
	public boolean COMMON_PASSIVE_SPELL_BIN_UPDATE;
	public boolean COMMON_NDL_BIN_UPDATE;
	public boolean COMMON_SHIP_BIN_UPDATE;
	public boolean COMMON_TREASUREBOX_BIN_UPDATE;
	public boolean COMMON_PORTRAIT_BIN_UPDATE;
	public boolean COMMON_COMPANION_BIN_UPDATE;
	public boolean COMMON_INDUN_BIN_UPDATE;
	public boolean COMMON_CATALYST_BIN_UPDATE;
	public boolean COMMON_ELEMENT_ENCHANT_BIN_UPDATE;
	public boolean COMMON_ARMOR_ELEMENT_BIN_UPDATE;
	public boolean COMMON_ENCHANT_TABLE_INFO_BIN_UPDATE;
	public boolean COMMON_ENCHANT_SCROLL_BIN_UPDATE;
	public boolean COMMON_ENTER_MAPS_BIN_UPDATE;
	public boolean COMMON_GENERAL_GOODS_BIN_UPDATE;
	public boolean COMMON_HUNTING_QUEST_BIN_UPDATE;
	public boolean COMMON_POTENTIAL_BIN_UPDATE;
	public boolean COMMON_FAVOR_BOOK_BIN_UPDATE;
	public boolean COMMON_TIME_COLLECTION_BIN_UPDATE;
	public boolean COMMON_PC_MASTER_BIN_UPDATE;
	public boolean COMMON_EINHASAD_POINT_BIN_UPDATE;
	public boolean COMMON_EINHASAD_POINT_FAITH_BIN_UPDATE;
	public boolean COMMON_CHARGED_TIME_MAP_BIN_UPDATE;
	
	public boolean COMMON_QUEST_BIN_LOAD;
	
	public void load(){
		try {
			Properties setting						= new Properties();
			InputStream is							= new FileInputStream(new File(COMMON_CONFIG_FILE));
			setting.load(is);
			is.close();
			
			COMMON_CRAFT_BIN_UPDATE					= Boolean.parseBoolean(setting.getProperty("COMMON_CRAFT_BIN_UPDATE", StringUtil.FalseString));
			COMMON_ITEM_BIN_UPDATE					= Boolean.parseBoolean(setting.getProperty("COMMON_ITEM_BIN_UPDATE", StringUtil.FalseString));
			COMMON_NPC_BIN_UPDATE					= Boolean.parseBoolean(setting.getProperty("COMMON_NPC_BIN_UPDATE", StringUtil.FalseString));
			COMMON_SPELL_BIN_UPDATE					= Boolean.parseBoolean(setting.getProperty("COMMON_SPELL_BIN_UPDATE", StringUtil.FalseString));
			COMMON_PASSIVE_SPELL_BIN_UPDATE			= Boolean.parseBoolean(setting.getProperty("COMMON_PASSIVE_SPELL_BIN_UPDATE", StringUtil.FalseString));
			COMMON_NDL_BIN_UPDATE					= Boolean.parseBoolean(setting.getProperty("COMMON_NDL_BIN_UPDATE", StringUtil.FalseString));
			COMMON_SHIP_BIN_UPDATE					= Boolean.parseBoolean(setting.getProperty("COMMON_SHIP_BIN_UPDATE", StringUtil.FalseString));
			COMMON_TREASUREBOX_BIN_UPDATE			= Boolean.parseBoolean(setting.getProperty("COMMON_TREASUREBOX_BIN_UPDATE", StringUtil.FalseString));
			COMMON_PORTRAIT_BIN_UPDATE				= Boolean.parseBoolean(setting.getProperty("COMMON_PORTRAIT_BIN_UPDATE", StringUtil.FalseString));
			COMMON_COMPANION_BIN_UPDATE				= Boolean.parseBoolean(setting.getProperty("COMMON_COMPANION_BIN_UPDATE", StringUtil.FalseString));
			COMMON_INDUN_BIN_UPDATE					= Boolean.parseBoolean(setting.getProperty("COMMON_INDUN_BIN_UPDATE", StringUtil.FalseString));
			COMMON_CATALYST_BIN_UPDATE				= Boolean.parseBoolean(setting.getProperty("COMMON_CATALYST_BIN_UPDATE", StringUtil.FalseString));
			COMMON_ELEMENT_ENCHANT_BIN_UPDATE		= Boolean.parseBoolean(setting.getProperty("COMMON_ELEMENT_ENCHANT_BIN_UPDATE", StringUtil.FalseString));
			COMMON_ARMOR_ELEMENT_BIN_UPDATE			= Boolean.parseBoolean(setting.getProperty("COMMON_ARMOR_ELEMENT_BIN_UPDATE", StringUtil.FalseString));
			COMMON_ENCHANT_TABLE_INFO_BIN_UPDATE	= Boolean.parseBoolean(setting.getProperty("COMMON_ENCHANT_TABLE_INFO_BIN_UPDATE", StringUtil.FalseString));
			COMMON_ENCHANT_SCROLL_BIN_UPDATE		= Boolean.parseBoolean(setting.getProperty("COMMON_ENCHANT_SCROLL_BIN_UPDATE", StringUtil.FalseString));
			COMMON_ENTER_MAPS_BIN_UPDATE			= Boolean.parseBoolean(setting.getProperty("COMMON_ENTER_MAPS_BIN_UPDATE", StringUtil.FalseString));
			COMMON_GENERAL_GOODS_BIN_UPDATE			= Boolean.parseBoolean(setting.getProperty("COMMON_GENERAL_GOODS_BIN_UPDATE", StringUtil.FalseString));
			COMMON_HUNTING_QUEST_BIN_UPDATE			= Boolean.parseBoolean(setting.getProperty("COMMON_HUNTING_QUEST_BIN_UPDATE", StringUtil.FalseString));
			COMMON_POTENTIAL_BIN_UPDATE				= Boolean.parseBoolean(setting.getProperty("COMMON_POTENTIAL_BIN_UPDATE", StringUtil.FalseString));
			COMMON_FAVOR_BOOK_BIN_UPDATE			= Boolean.parseBoolean(setting.getProperty("COMMON_FAVOR_BOOK_BIN_UPDATE", StringUtil.FalseString));
			COMMON_TIME_COLLECTION_BIN_UPDATE		= Boolean.parseBoolean(setting.getProperty("COMMON_TIME_COLLECTION_BIN_UPDATE", StringUtil.FalseString));
			COMMON_PC_MASTER_BIN_UPDATE				= Boolean.parseBoolean(setting.getProperty("COMMON_PC_MASTER_BIN_UPDATE", StringUtil.FalseString));
			COMMON_EINHASAD_POINT_BIN_UPDATE		= Boolean.parseBoolean(setting.getProperty("COMMON_EINHASAD_POINT_BIN_UPDATE", StringUtil.FalseString));
			COMMON_EINHASAD_POINT_FAITH_BIN_UPDATE	= Boolean.parseBoolean(setting.getProperty("COMMON_EINHASAD_POINT_FAITH_BIN_UPDATE", StringUtil.FalseString));
			COMMON_CHARGED_TIME_MAP_BIN_UPDATE		= Boolean.parseBoolean(setting.getProperty("COMMON_CHARGED_TIME_MAP_BIN_UPDATE", StringUtil.FalseString));
			
			COMMON_QUEST_BIN_LOAD					= Boolean.parseBoolean(setting.getProperty("COMMON_QUEST_BIN_LOAD", StringUtil.FalseString));
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw new Error("Failed to Load " + COMMON_CONFIG_FILE + " File.");
		}
	}

}

