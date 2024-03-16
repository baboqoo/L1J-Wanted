package l1j.server.configure;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.utils.StringUtil;

public class EnchantConfigure {
	private static final Logger _log = Logger.getLogger(EnchantConfigure.class.getName());
	private static final String ENCHANT_CONFIG_FILE	= "./config/enchant.properties";

	public int WEAPON_LIMIT;
	public int ADVANCED_WEAPON_LIMIT;
	public int ARMOR_ENCHANT_LIMIT;
	public int ADVANCED_ARMOR_ENCHANT_LIMIT;
	
	public int RUMTIS_LIMIT;
	public int SNAPPER_LIMIT;
	public int ENCHANT_SCROLL_LIMIT;
	public int DRAGON_TSHIRT_ENCHANT_SCROLL_LIMIT;
	public int ACCESORY_LIMIT;
	public int SENTENCE_LIMIT;
	public int INSIGNIA_LIMIT;
	public int GARDER_LIMIT;
	public int PENDANT_LIMIT;
	
	public int ANCIENT_WEAPON_LIMIT;
	public int ANCIENT_ARMOR_LIMIT;
	public boolean ENCHANT_MESSAGE;
	
	public int EXPERT_WEAPON_LIMIT;
	public int EXPERT_ARMOR_LIMIT;
	public int BLESSED_SCROLL_PROBABILITY;
	public double ENCHANT_SCROLL_PROBABILITY_1;
	public double ENCHANT_SCROLL_PROBABILITY_2;
	public double ENCHANT_SCROLL_PROBABILITY_3;
	public double ENCHANT_SCROLL_PROBABILITY_4;
	public double ENCHANT_SCROLL_PROBABILITY_5;
	public double ENCHANT_SCROLL_PROBABILITY_6;
	public double ENCHANT_SCROLL_PROBABILITY_7;
	public double ENCHANT_SCROLL_PROBABILITY_8;
	public double ENCHANT_SCROLL_PROBABILITY_9;
	
	public double DRAGON_TSHIRT_ENCHANT_SCROLL_PROBABILITY_5;
	public double DRAGON_TSHIRT_ENCHANT_SCROLL_PROBABILITY_6;
	public double DRAGON_TSHIRT_ENCHANT_SCROLL_PROBABILITY_7;
	public double DRAGON_TSHIRT_ENCHANT_SCROLL_PROBABILITY_8;
	public double DRAGON_TSHIRT_ENCHANT_SCROLL_PROBABILITY_9;
	
	public int ENCHANT_CHANCE_WEAPON_DEFAULT;
	public int ENCHANT_CHANCE_ARMOR_DEFAULT;
	public int ENCHANT_CHANCE_ACCESSORY_DEFAULT;
	
	public double WEAPON_ENCHANT_PROBABILITY_6;
	public double WEAPON_ENCHANT_PROBABILITY_7;
	public double WEAPON_ENCHANT_PROBABILITY_8;
	public double WEAPON_ENCHANT_PROBABILITY_9;

	public double WEAPON_SAFETY_ENCHANT_0;
	public double WEAPON_SAFETY_ENCHANT_1;
	public double WEAPON_SAFETY_ENCHANT_2;
	public double WEAPON_SAFETY_ENCHANT_3;
	public double WEAPON_SAFETY_ENCHANT_4;
	public double WEAPON_SAFETY_ENCHANT_5;
	public double WEAPON_SAFETY_ENCHANT_6;
	public double WEAPON_SAFETY_ENCHANT_7;
	public double WEAPON_SAFETY_ENCHANT_8;
	public double WEAPON_SAFETY_ENCHANT_9;
		
	public double ARMOR_ENCHANT_PROBABILITY_4;
	public double ARMOR_ENCHANT_PROBABILITY_5;
	public double ARMOR_ENCHANT_PROBABILITY_6;
	public double ARMOR_ENCHANT_PROBABILITY_7;
	public double ARMOR_ENCHANT_PROBABILITY_8;
	public double ARMOR_ENCHANT_PROBABILITY_9;
	
	public double ARMOR_SAFETY_ENCHANT0;
	public double ARMOR_SAFETY_ENCHANT1;
	public double ARMOR_SAFETY_ENCHANT2;
	public double ARMOR_SAFETY_ENCHANT3;
	public double ARMOR_SAFETY_ENCHANT4;
	public double ARMOR_SAFETY_ENCHANT5;
	public double ARMOR_SAFETY_ENCHANT6;
	public double ARMOR_SAFETY_ENCHANT7;
	public double ARMOR_SAFETY_ENCHANT8;
	public double ARMOR_SAFETY_ENCHANT9;
	
	public double ACCESORY_ENCHANT_PROBABILITY0;
	public double ACCESORY_ENCHANT_PROBABILITY1;
	public double ACCESORY_ENCHANT_PROBABILITY2;
	public double ACCESORY_ENCHANT_PROBABILITY3;
	public double ACCESORY_ENCHANT_PROBABILITY4;
	public double ACCESORY_ENCHANT_PROBABILITY5;
	public double ACCESORY_ENCHANT_PROBABILITY6;
	public double ACCESORY_ENCHANT_PROBABILITY7;
	public double ACCESORY_ENCHANT_PROBABILITY8;
	public double ACCESORY_ENCHANT_PROBABILITY9;
	
	public void load(){
		try {
			Properties enchant	= new Properties();
			FileReader is		= new FileReader(new File(ENCHANT_CONFIG_FILE));
			enchant.load(is);
			is.close();
			ENCHANT_CHANCE_WEAPON_DEFAULT				= Integer.parseInt(enchant.getProperty("EnchantChanceWeapon", "68"));
			ENCHANT_CHANCE_ARMOR_DEFAULT				= Integer.parseInt(enchant.getProperty("EnchantChanceArmor", "52"));
			ENCHANT_CHANCE_ACCESSORY_DEFAULT			= Integer.parseInt(enchant.getProperty("EnchantChanceAccessory", "5"));
			
			ANCIENT_WEAPON_LIMIT						= Integer.parseInt(enchant.getProperty("PopWeapon", "100"));
			ANCIENT_ARMOR_LIMIT							= Integer.parseInt(enchant.getProperty("PopArmor", "100"));
			ENCHANT_MESSAGE								= Boolean.parseBoolean(enchant.getProperty("EntMsg", StringUtil.TrueString));
			
			WEAPON_LIMIT								= Integer.parseInt(enchant.getProperty("LimitWeapon", "13"));// 일반무기
			ADVANCED_WEAPON_LIMIT						= Integer.parseInt(enchant.getProperty("LimitWeapon2", "5"));// 특수무기
			ARMOR_ENCHANT_LIMIT							= Integer.parseInt(enchant.getProperty("LimitArmor", "11"));// 일반갑옷
			ADVANCED_ARMOR_ENCHANT_LIMIT				= Integer.parseInt(enchant.getProperty("LimitArmor2", "7"));// 특수아머
			RUMTIS_LIMIT								= Integer.parseInt(enchant.getProperty("RoomT", "8"));
			SNAPPER_LIMIT								= Integer.parseInt(enchant.getProperty("Snapper", "8"));
			ACCESORY_LIMIT								= Integer.parseInt(enchant.getProperty("Accessory", "9"));			
			SENTENCE_LIMIT								= Integer.parseInt(enchant.getProperty("Sentencer", "8"));
			INSIGNIA_LIMIT								= Integer.parseInt(enchant.getProperty("badge", "8"));
			GARDER_LIMIT								= Integer.parseInt(enchant.getProperty("Gardar", "9"));
			PENDANT_LIMIT								= Integer.parseInt(enchant.getProperty("pendant", "8"));
			
			EXPERT_WEAPON_LIMIT							= Integer.parseInt(enchant.getProperty("WeaponExpert", "10"));
			EXPERT_ARMOR_LIMIT							= Integer.parseInt(enchant.getProperty("ArmorExpert", "10"));
			BLESSED_SCROLL_PROBABILITY					= Integer.parseInt(enchant.getProperty("ALT_HAPP", "15")); 

			ENCHANT_SCROLL_LIMIT						= Integer.parseInt(enchant.getProperty("bohoMax", "7")); 
			ENCHANT_SCROLL_PROBABILITY_1				= Double.parseDouble(enchant.getProperty("bohoChance1", "10.0"));
			ENCHANT_SCROLL_PROBABILITY_2				= Double.parseDouble(enchant.getProperty("bohoChance2", "10.0"));
			ENCHANT_SCROLL_PROBABILITY_3				= Double.parseDouble(enchant.getProperty("bohoChance3", "10.0"));
			ENCHANT_SCROLL_PROBABILITY_4				= Double.parseDouble(enchant.getProperty("bohoChance4", "10.0"));
			ENCHANT_SCROLL_PROBABILITY_5				= Double.parseDouble(enchant.getProperty("bohoChance5", "10.0"));
			ENCHANT_SCROLL_PROBABILITY_6				= Double.parseDouble(enchant.getProperty("bohoChance6", "10.0"));
			ENCHANT_SCROLL_PROBABILITY_7				= Double.parseDouble(enchant.getProperty("bohoChance7", "10.0"));
			ENCHANT_SCROLL_PROBABILITY_8				= Double.parseDouble(enchant.getProperty("bohoChance8", "10.0"));
			ENCHANT_SCROLL_PROBABILITY_9				= Double.parseDouble(enchant.getProperty("bohoChance9", "10.0"));
			
			DRAGON_TSHIRT_ENCHANT_SCROLL_LIMIT			= Integer.parseInt(enchant.getProperty("bohoTshirtsMax", "7")); 
			DRAGON_TSHIRT_ENCHANT_SCROLL_PROBABILITY_5	= Double.parseDouble(enchant.getProperty("bohoTshirtsChance5", "10.0"));
			DRAGON_TSHIRT_ENCHANT_SCROLL_PROBABILITY_6	= Double.parseDouble(enchant.getProperty("bohoTshirtsChance6", "10.0"));
			DRAGON_TSHIRT_ENCHANT_SCROLL_PROBABILITY_7	= Double.parseDouble(enchant.getProperty("bohoTshirtsChance7", "10.0"));
			DRAGON_TSHIRT_ENCHANT_SCROLL_PROBABILITY_8	= Double.parseDouble(enchant.getProperty("bohoTshirtsChance8", "10.0"));
			DRAGON_TSHIRT_ENCHANT_SCROLL_PROBABILITY_9	= Double.parseDouble(enchant.getProperty("bohoTshirtsChance9", "10.0"));
			
			WEAPON_ENCHANT_PROBABILITY_6				= Double.parseDouble(enchant.getProperty("LimWeapon6", "33.0"));
			WEAPON_ENCHANT_PROBABILITY_7				= Double.parseDouble(enchant.getProperty("LimWeapon7", "30.0"));
			WEAPON_ENCHANT_PROBABILITY_8				= Double.parseDouble(enchant.getProperty("LimWeapon8", "25.0"));
			WEAPON_ENCHANT_PROBABILITY_9				= Double.parseDouble(enchant.getProperty("LimWeapon9", "1.0"));
			
			WEAPON_SAFETY_ENCHANT_0						= Double.parseDouble(enchant.getProperty("Lim0Weapon0", "33.0"));
			WEAPON_SAFETY_ENCHANT_1						= Double.parseDouble(enchant.getProperty("Lim0Weapon1", "30.0"));
			WEAPON_SAFETY_ENCHANT_2						= Double.parseDouble(enchant.getProperty("Lim0Weapon2", "25.0"));
			WEAPON_SAFETY_ENCHANT_3						= Double.parseDouble(enchant.getProperty("Lim0Weapon3", "20.0"));
			WEAPON_SAFETY_ENCHANT_4						= Double.parseDouble(enchant.getProperty("Lim0Weapon4", "20.0"));
			WEAPON_SAFETY_ENCHANT_5						= Double.parseDouble(enchant.getProperty("Lim0Weapon5", "15.0"));
			WEAPON_SAFETY_ENCHANT_6						= Double.parseDouble(enchant.getProperty("Lim0Weapon6", "15.0"));
			WEAPON_SAFETY_ENCHANT_7						= Double.parseDouble(enchant.getProperty("Lim0Weapon7", "10.0"));
			WEAPON_SAFETY_ENCHANT_8						= Double.parseDouble(enchant.getProperty("Lim0Weapon8", "5.0"));			
			WEAPON_SAFETY_ENCHANT_9						= Double.parseDouble(enchant.getProperty("Lim0Weapon9", "1.0"));
			
			ARMOR_ENCHANT_PROBABILITY_4					= Double.parseDouble(enchant.getProperty("LimArmor4", "25.0"));
			ARMOR_ENCHANT_PROBABILITY_5					= Double.parseDouble(enchant.getProperty("LimArmor5", "20.0"));
			ARMOR_ENCHANT_PROBABILITY_6					= Double.parseDouble(enchant.getProperty("LimArmor6", "16.0"));			
			ARMOR_ENCHANT_PROBABILITY_7					= Double.parseDouble(enchant.getProperty("LimArmor7", "14.0"));
			ARMOR_ENCHANT_PROBABILITY_8					= Double.parseDouble(enchant.getProperty("LimArmor8", "12.0"));
			ARMOR_ENCHANT_PROBABILITY_9					= Double.parseDouble(enchant.getProperty("LimArmor9", "1.0"));
			
			ARMOR_SAFETY_ENCHANT0						= Double.parseDouble(enchant.getProperty("Lim0Armor0", "50.0"));
			ARMOR_SAFETY_ENCHANT1						= Double.parseDouble(enchant.getProperty("Lim0Armor1", "40.0"));
			ARMOR_SAFETY_ENCHANT2						= Double.parseDouble(enchant.getProperty("Lim0Armor2", "35.0"));
			ARMOR_SAFETY_ENCHANT3						= Double.parseDouble(enchant.getProperty("Lim0Armor3", "30.0"));
			ARMOR_SAFETY_ENCHANT4						= Double.parseDouble(enchant.getProperty("Lim0Armor4", "25.0"));
			ARMOR_SAFETY_ENCHANT5						= Double.parseDouble(enchant.getProperty("Lim0Armor5", "20.0"));
			ARMOR_SAFETY_ENCHANT6						= Double.parseDouble(enchant.getProperty("Lim0Armor6", "16.0"));
			ARMOR_SAFETY_ENCHANT7						= Double.parseDouble(enchant.getProperty("Lim0Armor7", "10.0"));
			ARMOR_SAFETY_ENCHANT8						= Double.parseDouble(enchant.getProperty("Lim0Armor8", "5.0"));
			ARMOR_SAFETY_ENCHANT9						= Double.parseDouble(enchant.getProperty("Lim0Armor9", "1.0"));
					
			ACCESORY_ENCHANT_PROBABILITY0				= Double.parseDouble(enchant.getProperty("Acc0", "70.0"));
			ACCESORY_ENCHANT_PROBABILITY1				= Double.parseDouble(enchant.getProperty("Acc1", "60.0"));
			ACCESORY_ENCHANT_PROBABILITY2				= Double.parseDouble(enchant.getProperty("Acc2", "40.0"));
			ACCESORY_ENCHANT_PROBABILITY3				= Double.parseDouble(enchant.getProperty("Acc3", "30.0"));
			ACCESORY_ENCHANT_PROBABILITY4				= Double.parseDouble(enchant.getProperty("Acc4", "20.0"));
			ACCESORY_ENCHANT_PROBABILITY5				= Double.parseDouble(enchant.getProperty("Acc5", "15.0"));
			ACCESORY_ENCHANT_PROBABILITY6				= Double.parseDouble(enchant.getProperty("Acc6", "10.0"));
			ACCESORY_ENCHANT_PROBABILITY7				= Double.parseDouble(enchant.getProperty("Acc7", "5.0"));
			ACCESORY_ENCHANT_PROBABILITY8				= Double.parseDouble(enchant.getProperty("Acc8", "1.0"));
			ACCESORY_ENCHANT_PROBABILITY9				= Double.parseDouble(enchant.getProperty("Acc9", "0.1"));
		} catch (Exception e) {		
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw new Error("Failed to Load " + ENCHANT_CONFIG_FILE + " File.");
		}
	}
}

