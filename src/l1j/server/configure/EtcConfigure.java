package l1j.server.configure;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.utils.StringUtil;

public class EtcConfigure {
	private static final Logger _log = Logger.getLogger(EtcConfigure.class.getName());
	private static final String ETC_SETTINGS_CONFIG_FILE	= "./config/etc.properties";
	
	public boolean EXP_POTION_MAX_LIMIT;
	public boolean FISH_LOC_TOWN;
	public int N_BUFF_RUN_DAY;
	public boolean WANTED_ACTIVE;
	public int WANTED_COST;
	
	public boolean ANOLD_EVENT_ACTIVE;
	public int ANOLD_EVENT_TIME;
	
	public boolean SCARECROW_ADENA;
	public int SCARECROW_ADENA_VALUE;
	
	public boolean T_WHITE_DAY_LIMIT;
    
    public int DRAGON_RAID_LIMIT_TIME;
  	
    // -- 몬스터 레벨별 차등데미지 외부화
 	public double MONSTER_DAMAGE_ONE;	// 몹렙이 10 ~ 19
 	public double MONSTER_DAMAGE_TWO;	// 몹렙이 20 ~ 29
 	public double MONSTER_DAMAGE_THREE;	// 몹렙이 30 ~ 39
 	public double MONSTER_DAMAGE_FOUR;	// 몹렙이 40 ~ 49
 	public double MONSTER_DAMAGE_FIVE;	// 몹렙이 50 ~ 59
 	public double MONSTER_DAMAGE_SIX;	// 몹렙이 60 ~ 69
 	public double MONSTER_DAMAGE_SEVEN;	// 몹렙이 70 ~ 79
 	public double MONSTER_DAMAGE_EIGHT;	// 몹렙이 80 ~ 87
 	public double MONSTER_DAMAGE_NINE;	// 몹렙이 87 ~
 	
 	// 몬스터 ac당 대미지 감소
 	public double MONSTER_AC_ONE;	// 몹 AC -10 ~ -19
 	public double MONSTER_AC_TWO;	// 몹 AC -20 ~ -29
 	public double MONSTER_AC_THREE;	// 몹 AC -30 ~ -39
 	public double MONSTER_AC_FOUR;	// 몹 AC -40 ~ -49
 	public double MONSTER_AC_FIVE;	// 몹 AC -50 ~ -59
 	public double MONSTER_AC_SIX;	// 몹 AC -60 ~ -69
 	public double MONSTER_AC_SEVEN;	// 몹 AC -70 ~ -79
 	public double MONSTER_AC_EIGHT;	// 몹 AC -80 ~ -89
 	public double MONSTER_AC_NINE;	// 몹 AC -90 ~ -99
 	public double MONSTER_AC_TEN;	// 몹 AC -100 ~
 	
 	public double MONSTER_AC_DEFEND;
 	
 	/** NPC 물리데미지/ 마법데미지 **/
	public int MONSTER_DMG;
	public int MONSTER_MAGIC_DMG;
	
	public boolean BOSS_RECALL;
	
	public boolean EVENT_LEVEL_100;
	public boolean FREE_PVP_REGION_ENABLE;
	
	public void load(){
		try {
			Properties etc	= new Properties();
			InputStream is			= new FileInputStream(new File(ETC_SETTINGS_CONFIG_FILE));
			etc.load(is);
			is.close();
			
			FISH_LOC_TOWN			= Boolean.parseBoolean(etc.getProperty("FISH_LOC_TOWN", StringUtil.TrueString));
			
			WANTED_ACTIVE			= Boolean.parseBoolean(etc.getProperty("WANTED_ACTIVE", StringUtil.FalseString));
			WANTED_COST				= Integer.parseInt(etc.getProperty("WANTED_COST", "60000000"));
			
			ANOLD_EVENT_ACTIVE		= Boolean.parseBoolean(etc.getProperty("ANOLD_EVENT_ACTIVE", StringUtil.FalseString));
			ANOLD_EVENT_TIME		= Integer.parseInt(etc.getProperty("ANOLD_EVENT_TIME", "1"));
			
			SCARECROW_ADENA			= Boolean.parseBoolean(etc.getProperty("SCARECROW_ADENA", StringUtil.FalseString));
			SCARECROW_ADENA_VALUE	= Integer.parseInt(etc.getProperty("SCARECROW_ADENA_VALUE", "10"));
			
			MONSTER_DMG				= Integer.parseInt(etc.getProperty("MONSTER_DMG", "10"));
			MONSTER_MAGIC_DMG		= Integer.parseInt(etc.getProperty("MONSTER_MAGIC_DMG", "10"));
			
			MONSTER_DAMAGE_ONE		= Double.parseDouble(etc.getProperty("MONSTER_DAMAGE_ONE", "1.05"));
			MONSTER_DAMAGE_TWO		= Double.parseDouble(etc.getProperty("MONSTER_DAMAGE_TWO", "1.1"));
			MONSTER_DAMAGE_THREE	= Double.parseDouble(etc.getProperty("MONSTER_DAMAGE_THREE", "1.15"));
			MONSTER_DAMAGE_FOUR		= Double.parseDouble(etc.getProperty("MONSTER_DAMAGE_FOUR", "1.2"));
			MONSTER_DAMAGE_FIVE		= Double.parseDouble(etc.getProperty("MONSTER_DAMAGE_FIVE", "1.25"));
			MONSTER_DAMAGE_SIX		= Double.parseDouble(etc.getProperty("MONSTER_DAMAGE_SIX", "1.3"));
			MONSTER_DAMAGE_SEVEN	= Double.parseDouble(etc.getProperty("MONSTER_DAMAGE_SEVEN", "1.35"));
			MONSTER_DAMAGE_EIGHT	= Double.parseDouble(etc.getProperty("MONSTER_DAMAGE_EIGHT", "1.4"));
			MONSTER_DAMAGE_NINE		= Double.parseDouble(etc.getProperty("MONSTER_DAMAGE_NINE", "1.45"));
			
			MONSTER_AC_ONE			= Double.parseDouble(etc.getProperty("MONSTER_AC_ONE", "0.95"));
			MONSTER_AC_TWO			= Double.parseDouble(etc.getProperty("MONSTER_AC_TWO", "0.9"));
			MONSTER_AC_THREE		= Double.parseDouble(etc.getProperty("MONSTER_AC_THREE", "0.85"));
			MONSTER_AC_FOUR			= Double.parseDouble(etc.getProperty("MONSTER_AC_FOUR", "0.8"));
			MONSTER_AC_FIVE			= Double.parseDouble(etc.getProperty("MONSTER_AC_FIVE", "0.75"));
			MONSTER_AC_SIX			= Double.parseDouble(etc.getProperty("MONSTER_AC_SIX", "0.7"));
			MONSTER_AC_SEVEN		= Double.parseDouble(etc.getProperty("MONSTER_AC_SEVEN", "0.65"));
			MONSTER_AC_EIGHT		= Double.parseDouble(etc.getProperty("MONSTER_AC_EIGHT", "0.6"));
			MONSTER_AC_NINE			= Double.parseDouble(etc.getProperty("MONSTER_AC_NINE", "0.55"));
			MONSTER_AC_TEN			= Double.parseDouble(etc.getProperty("MONSTER_AC_TEN", "0.5"));
			
			MONSTER_AC_DEFEND		= Double.parseDouble(etc.getProperty("MONSTER_AC_DEFEND", "1.0"));
			
			T_WHITE_DAY_LIMIT		= Boolean.parseBoolean(etc.getProperty("T_WHITE_DAY_LIMIT", StringUtil.FalseString));
			DRAGON_RAID_LIMIT_TIME	= Integer.parseInt(etc.getProperty("DRAGON_RAID_LIMIT_TIME", "1"));
			N_BUFF_RUN_DAY			= Integer.parseInt(etc.getProperty("N_BUFF_RUN_DAY", "3"));
			EXP_POTION_MAX_LIMIT	= Boolean.parseBoolean(etc.getProperty("EXP_POTION_MAX_LIMIT", StringUtil.TrueString));
			BOSS_RECALL				= Boolean.parseBoolean(etc.getProperty("BOSS_RECALL", StringUtil.TrueString));
			EVENT_LEVEL_100			= Boolean.parseBoolean(etc.getProperty("EVENT_LEVEL_100", StringUtil.FalseString));
			FREE_PVP_REGION_ENABLE	= Boolean.parseBoolean(etc.getProperty("FREE_PVP_REGION_ENABLE", StringUtil.FalseString));
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw new Error("Failed to Load " + ETC_SETTINGS_CONFIG_FILE + " File.");
		}
	}
}

