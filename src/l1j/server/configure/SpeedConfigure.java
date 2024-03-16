package l1j.server.configure;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.utils.StringUtil;

public class SpeedConfigure {
	private static final Logger _log = Logger.getLogger(SpeedConfigure.class.getName());
	private static final String SPEED_CONFIG_FILE	= "./config/speed.properties";
	
	public int PC_MOVE_DELAY_REDUCE_RATE;
	public int NPC_MOVE_DELAY_REDUCE_RATE;
	public int MOVE_SPEED_SYNCHRONIZED;
	public int ATTACK_SPEED_SYNCHRONIZED;
	public int ATTACK_CONTINUE_SPEED_SYNCHRONIZED;
	public int SPELL_SPEED_SYNCHRONIZED;
	public int SPELL_DELAY_SYNCHRONIZED;
	public int WAND_SPEED_SYNCHRONIZED;
	public boolean SPEED_CHECKER_ACTIVE;
	public int SPEED_CHECKER_CPU_VALUE;
	public int SPEED_CHECKER_OVER_COUNT;
	public int TELEPORT_DELAY_SYNCHRONIZED;
	
	public void load(){
		try {
			Properties settings					= new Properties();
			FileReader is						= new FileReader(new File(SPEED_CONFIG_FILE));
			settings.load(is);
			is.close();
			
			PC_MOVE_DELAY_REDUCE_RATE			= Integer.parseInt(settings.getProperty("PC_MOVE_DELAY_REDUCE_RATE", "100"));
			NPC_MOVE_DELAY_REDUCE_RATE			= Integer.parseInt(settings.getProperty("NPC_MOVE_DELAY_REDUCE_RATE", "100"));

			MOVE_SPEED_SYNCHRONIZED				= Integer.parseInt(settings.getProperty("MOVE_SPEED_SYNCHRONIZED", "-50"));
			ATTACK_SPEED_SYNCHRONIZED			= Integer.parseInt(settings.getProperty("ATTACK_SPEED_SYNCHRONIZED", "50"));
			ATTACK_CONTINUE_SPEED_SYNCHRONIZED	= Integer.parseInt(settings.getProperty("ATTACK_CONTINUE_SPEED_SYNCHRONIZED", "50"));
			SPELL_SPEED_SYNCHRONIZED			= Integer.parseInt(settings.getProperty("SPELL_SPEED_SYNCHRONIZED", "50"));
			SPELL_DELAY_SYNCHRONIZED			= Integer.parseInt(settings.getProperty("SPELL_DELAY_SYNCHRONIZED", "50"));
			WAND_SPEED_SYNCHRONIZED				= Integer.parseInt(settings.getProperty("WAND_SPEED_SYNCHRONIZED", "50"));
			
			SPEED_CHECKER_ACTIVE				= Boolean.parseBoolean(settings.getProperty("SPEED_CHECKER_ACTIVE", StringUtil.TrueString));
			SPEED_CHECKER_CPU_VALUE				= Integer.parseInt(settings.getProperty("SPEED_CHECKER_CPU_VALUE", "70"));
			SPEED_CHECKER_OVER_COUNT			= Integer.parseInt(settings.getProperty("SPEED_CHECKER_OVER_COUNT", "3"));
			
			TELEPORT_DELAY_SYNCHRONIZED			= Integer.parseInt(settings.getProperty("TELEPORT_DELAY_SYNCHRONIZED", "100"));
		} catch (Exception e) {		
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw new Error("Failed to Load " + SPEED_CONFIG_FILE + " File.");
		}
	}
}

