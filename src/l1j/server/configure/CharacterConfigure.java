package l1j.server.configure;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.utils.StringUtil;

public class CharacterConfigure {
	private static final Logger _log = Logger.getLogger(CharacterConfigure.class.getName());
	private static final String CHAR_SETTINGS_CONFIG_FILE	= "./config/charsettings.properties";

	public int PRINCE_MAX_HP;
	public int PRINCE_MAX_MP;
	public int KNIGHT_MAX_HP;
	public int KNIGHT_MAX_MP;
	public int ELF_MAX_HP;
	public int ELF_MAX_MP;
	public int WIZARD_MAX_HP;
	public int WIZARD_MAX_MP;
	public int DARKELF_MAX_HP;
	public int DARKELF_MAX_MP;
	public int DRAGONKNIGHT_MAX_HP;
	public int DRAGONKNIGHT_MAX_MP;
	public int ILLUSIONIST_MAX_HP;
	public int ILLUSIONIST_MAX_MP;	
	public int WARRIOR_MAX_HP;
	public int WARRIOR_MAX_MP;
	public int FENCER_MAX_HP;
	public int FENCER_MAX_MP;
	public int LANCER_MAX_HP;
	public int LANCER_MAX_MP;
	
	public int PRINCE_ADD_DAMAGEPC;
	public int KNIGHT_ADD_DAMAGEPC;
	public int ELF_ADD_DAMAGEPC;
	public int WIZARD_ADD_DAMAGEPC;
	public int DARKELF_ADD_DAMAGEPC;
	public int DRAGONKNIGHT_ADD_DAMAGEPC;
	public int ILLUSIONIST_ADD_DAMAGEPC;
	public int WARRIOR_ADD_DAMAGEPC;
	public int FENCER_ADD_DAMAGEPC;
	public int LANCER_ADD_DAMAGEPC;
	
	public double AC_ER;
	public int LIMIT_LEVEL;
	
	public void load(){
		try {
			Properties charSettings	= new Properties();
			InputStream is			= new FileInputStream(new File(CHAR_SETTINGS_CONFIG_FILE));
			charSettings.load(is);
			is.close();
			PRINCE_MAX_HP				= Integer.parseInt(charSettings.getProperty("PrinceMaxHP", "1000"));
			PRINCE_MAX_MP				= Integer.parseInt(charSettings.getProperty("PrinceMaxMP", "800"));
			KNIGHT_MAX_HP				= Integer.parseInt(charSettings.getProperty("KnightMaxHP", "1400"));
			KNIGHT_MAX_MP				= Integer.parseInt(charSettings.getProperty("KnightMaxMP", "600"));
			ELF_MAX_HP					= Integer.parseInt(charSettings.getProperty("ElfMaxHP", "1000"));
			ELF_MAX_MP					= Integer.parseInt(charSettings.getProperty("ElfMaxMP", "900"));
			WIZARD_MAX_HP				= Integer.parseInt(charSettings.getProperty("WizardMaxHP", "800"));
			WIZARD_MAX_MP				= Integer.parseInt(charSettings.getProperty("WizardMaxMP", "1200"));
			DARKELF_MAX_HP				= Integer.parseInt(charSettings.getProperty("DarkelfMaxHP", "1000"));
			DARKELF_MAX_MP				= Integer.parseInt(charSettings.getProperty("DarkelfMaxMP", "900"));
			DRAGONKNIGHT_MAX_HP			= Integer.parseInt(charSettings.getProperty("DragonknightMaxHP", "1000"));
			DRAGONKNIGHT_MAX_MP			= Integer.parseInt(charSettings.getProperty("DragonknightMaxMP", "900"));
			ILLUSIONIST_MAX_HP			= Integer.parseInt(charSettings.getProperty("IllusionistMaxHP", "900"));
			ILLUSIONIST_MAX_MP			= Integer.parseInt(charSettings.getProperty("IllusionistMaxMP", "1100"));
			WARRIOR_MAX_HP				= Integer.parseInt(charSettings.getProperty("WarriorMaxHP", "1400"));
			WARRIOR_MAX_MP				= Integer.parseInt(charSettings.getProperty("WarriorMaxMP", "600"));
			FENCER_MAX_HP				= Integer.parseInt(charSettings.getProperty("FencerMaxHP", "1400"));
			FENCER_MAX_MP				= Integer.parseInt(charSettings.getProperty("FencerMaxMP", "600"));
			LANCER_MAX_HP				= Integer.parseInt(charSettings.getProperty("LancerMaxHP", "1600"));
			LANCER_MAX_MP				= Integer.parseInt(charSettings.getProperty("LancerMaxMP", "600"));
			
			PRINCE_ADD_DAMAGEPC			= Integer.parseInt(charSettings.getProperty("PrinceAddDamagePc", StringUtil.ZeroString));
			KNIGHT_ADD_DAMAGEPC			= Integer.parseInt(charSettings.getProperty("KnightAddDamagePc", StringUtil.ZeroString));
			ELF_ADD_DAMAGEPC			= Integer.parseInt(charSettings.getProperty("ElfAddDamagePc", StringUtil.ZeroString));
			WIZARD_ADD_DAMAGEPC			= Integer.parseInt(charSettings.getProperty("WizardAddDamagePc", StringUtil.ZeroString));
			DARKELF_ADD_DAMAGEPC		= Integer.parseInt(charSettings.getProperty("DarkelfAddDamagePc", StringUtil.ZeroString));
			DRAGONKNIGHT_ADD_DAMAGEPC	= Integer.parseInt(charSettings.getProperty("DragonknightAddDamagePc", StringUtil.ZeroString));
			ILLUSIONIST_ADD_DAMAGEPC	= Integer.parseInt(charSettings.getProperty("IllusionistAddDamagePc", StringUtil.ZeroString));
			WARRIOR_ADD_DAMAGEPC		= Integer.parseInt(charSettings.getProperty("WarriorAddDamagePc", StringUtil.ZeroString));
			FENCER_ADD_DAMAGEPC			= Integer.parseInt(charSettings.getProperty("FencerAddDamagePc", StringUtil.ZeroString));
			LANCER_ADD_DAMAGEPC			= Integer.parseInt(charSettings.getProperty("LancerAddDamagePc", StringUtil.ZeroString));
			
			AC_ER						= Double.parseDouble(charSettings.getProperty("ACdefend", "1.0"));
			LIMIT_LEVEL					= Integer.parseInt(charSettings.getProperty("LIMIT_LEVEL", "127"));
			
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw new Error("Failed to Load " + CHAR_SETTINGS_CONFIG_FILE + " File.");
		}
	}
}

