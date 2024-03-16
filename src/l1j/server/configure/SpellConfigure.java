package l1j.server.configure;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.utils.StringUtil;

public class SpellConfigure {
	private static final Logger _log = Logger.getLogger(SpellConfigure.class.getName());
	private static final String SPELL_CONFIG_FILE	= "./config/spell.properties";
	
	public int TYRANT_EXCUTION_PROB;
	public int TYRANT_EXCUTION_MOVE_SPEED_RATE;
	public int CONQUEROR_PROB;
	public int CONQUEROR_STUN_PROB;

	public int SHOCK_STUN_CHANCE;
	public int FORCE_STUN_CHANCE;
	public int STUN_LEVEL_RATE;
	public int COUNTER_BARRIER_CHANCE;
	public int COUNTER_BARRIER_MASTER_HP;
	public int SHOCK_ATTACK_CHANCE;
	public int SHOCK_ATTACK_TELEPORT;
	public int SHOCK_ATTACK_MOVE_SPEED_RATE;
	public int RAIGING_WEAPONE_ATTACK_SPEED_RATE;
	
	public int ARMOR_BRAKE_PROB;
	public int AVENGER_PROB;
	public int SHADOWSTEP_PROB;
	public int BLIND_HIDING_ASSASSIN_MOVE_SPEED_RATE;

	public int AREA_OF_SILENCE_PROB;
	public int INFERNO_PROB;
	public int ERASE_MAGIC_PROB;	
	public int EARTH_BIND_PROB;
	public int POLLUTE_WATER_PROB;
	public int STRIKER_GALE_PROB;
	public int WIND_SHACKLE_PROB;
	
	public int SLOW_PROB;
	public int DISEASE_PROB;
	public int WEAKNESS_PROB;
	public int WEAPON_BREAK_PROB;
	public int CURSE_BLIND_PROB;
	public int FOG_OF_SLEEPING_PROB;
	public int DARKNESS_PROB;
	public int DEATH_HEAL_PROB;
	public int TURN_UNDEAD_PROB;
	public int MANA_DRAIN_PROB;	
	public int CANCELLATION_PROB;
	public int ELF_CANCELLATION_PROB;
	public int SHAPE_CHANGE_PROB;
	public int CURSE_PARALYZE_PROB;
	public int ETERNITY_PROB;
	public int ARTERIALCIRCLE_PROB;
	public int NEMESIS_STUN_PROB;
	public int PATAL_POTION_PROB;
	public int PATAL_POTION_CHANCE;
	public short DIVINE_PROTECTION_HP_VALUE;
	
	public int HALPHAS_PROB;
	public int FOUSLAYER_STUN_PROB;
	public int FOUSLAYER_FORCE_STUN_PROB;
	public int EXPOSE_WEAKNESS_PROB;
	public int CHAIN_REACTION_PROB;
	public int BEHEMOTH_DEBUFF_PROB;
	public int BEHEMOTH_MOVE_SPEED_RATE;
			
	public int BONE_BREAK_PROB;
	public int PHANTASM_PROB;
	public int ENSNARE_CHANCE;
	public int ENSNARE_MOVE_SPEED_RATE;
	public int OSIRIS_CHANCE;
	public int OSIRIS_TRANSITION_COUNT;
	
	public int DESPERADO_CHANCE;
	public int DESPERADO_LEVEL_RATE;
	public int POWER_GRIP;
	public int TITAN_LOCK;
	public int TITAN_BLICK;
	public int TITAN_MAGIC;
	public int FURY;
	public int TEMPEST;
	public int TITAN_BEAST_MILLISECOND;
	public int TOMAHAWK_HUNT_HP_FIX_VALUE;
	public int TOMAHAWK_HUNT_HP_RANDOM_VALUE;
	public int SLAYER_ATTACK_SPEED_RATE;
	
	public int PANTERA_PROB;
	public int PHANTOM_PROB;
	public int JUDGEMENT_PROB;
	public int SURVIVE_PROB;
	public int PARADOX_PROB;
	public int RAGE_PROB;
	public double PHANTOM_REQUIEM_POTION_RATE;
	public double PHANTOM_DEATH_POTION_RATE;
	
	public int PRESSURE_PROB;
	public int CRUEL_PROB;
	public int MAELSTROM_PROB;
	public int VANGUARD_MOVE_SPEED_RATE;
	public int VANGUARD_ATTACK_SPEED_RATE;
	
	public int DIS_LOCK_DMG;
	public boolean STUN_CONTINUE;
	
	public double WIZARD_MAGIC_DMG_PVP;// 마법사 대미지
	public double WIZARD_MAGIC_DMG_PVE;// 마법사 대미지
	public int WIZARD_STACK_TIME;// 마법사 스택 시간
	public int WIZARD_STACK_COUNT;// 마법사 스택 횟수
	public double WIZARD_STACK_VALUE;// 마법사 스택 수치
	public int KEYRINK_DMG;	
	public double TRIPLE_DMG;
    public double FOW_SLAYER_DMG;
    public int TRIPLE_STUN_CHANCE;
    
    public void load(){
		try {
			Properties skill	= new Properties();
			InputStream is		= new FileInputStream(new File(SPELL_CONFIG_FILE));
			skill.load(is);
			is.close();
			
			TYRANT_EXCUTION_PROB			= Integer.parseInt(skill.getProperty("TYRANT_EXCUTION_PROB", "60"));
			TYRANT_EXCUTION_MOVE_SPEED_RATE	= Integer.parseInt(skill.getProperty("TYRANT_EXCUTION_MOVE_SPEED_RATE", "35"));
			CONQUEROR_PROB					= Integer.parseInt(skill.getProperty("CONQUEROR", "30"));
			CONQUEROR_STUN_PROB				= Integer.parseInt(skill.getProperty("CONQUEROR_STUN_PROB", "20"));
			
			SHOCK_STUN_CHANCE				= Integer.parseInt(skill.getProperty("SHOCK_STUN_CHANCE", "60"));
			FORCE_STUN_CHANCE				= Integer.parseInt(skill.getProperty("FORCE_STUN_CHANCE", "60"));
			STUN_LEVEL_RATE					= Integer.parseInt(skill.getProperty("STUN_LEVEL_RATE", "2"));
			COUNTER_BARRIER_CHANCE			= Integer.parseInt(skill.getProperty("COUNTER_BARRIER_CHANCE", "20"));
			COUNTER_BARRIER_MASTER_HP		= Integer.parseInt(skill.getProperty("COUNTER_BARRIER_MASTER_HP", "20"));
			SHOCK_ATTACK_CHANCE				= Integer.parseInt(skill.getProperty("SHOCK_ATTACK_CHANCE", "20"));
			SHOCK_ATTACK_TELEPORT			= Integer.parseInt(skill.getProperty("SHOCK_ATTACK_TELEPORT", "20"));
			SHOCK_ATTACK_MOVE_SPEED_RATE	= Integer.parseInt(skill.getProperty("SHOCK_ATTACK_MOVE_SPEED_RATE", "35"));
			RAIGING_WEAPONE_ATTACK_SPEED_RATE = Integer.parseInt(skill.getProperty("RAIGING_WEAPONE_ATTACK_SPEED_RATE", "10"));
			
			ARMOR_BRAKE_PROB				= Integer.parseInt(skill.getProperty("ARMOR_BRAKE_PROB", "38"));
			AVENGER_PROB					= Integer.parseInt(skill.getProperty("AVENGER_PROB", "38"));
			SHADOWSTEP_PROB					= Integer.parseInt(skill.getProperty("SHADOWSTEP_PROB", "40"));
			BLIND_HIDING_ASSASSIN_MOVE_SPEED_RATE = Integer.parseInt(skill.getProperty("BLIND_HIDING_ASSASSIN_MOVE_SPEED_RATE", "35"));
		
			AREA_OF_SILENCE_PROB			= Integer.parseInt(skill.getProperty("AREA_OF_SILENCE_PROB", "40"));
			INFERNO_PROB					= Integer.parseInt(skill.getProperty("INFERNO_PROB", "15"));
			ERASE_MAGIC_PROB				= Integer.parseInt(skill.getProperty("ERASE_MAGIC_PROB", "40"));
			EARTH_BIND_PROB					= Integer.parseInt(skill.getProperty("EARTH_BIND_PROB", "40"));
			POLLUTE_WATER_PROB				= Integer.parseInt(skill.getProperty("POLLUTE_WATER_PROB", "30"));
			STRIKER_GALE_PROB				= Integer.parseInt(skill.getProperty("STRIKER_GALE_PROB", "30"));
			WIND_SHACKLE_PROB				= Integer.parseInt(skill.getProperty("WIND_SHACKLE_PROB", "30"));
			
			DIVINE_PROTECTION_HP_VALUE		= Short.parseShort(skill.getProperty("DIVINE_PROTECTION_HP_VALUE", "500"));
			PATAL_POTION_CHANCE				= Integer.parseInt(skill.getProperty("PATAL_POTION_CHANCE", "50"));
			PATAL_POTION_PROB				= Integer.parseInt(skill.getProperty("PATAL_POTION_PROB", StringUtil.ZeroString));
			SLOW_PROB						= Integer.parseInt(skill.getProperty("SLOW_PROB", StringUtil.ZeroString));
			DISEASE_PROB					= Integer.parseInt(skill.getProperty("DISEASE_PROB", StringUtil.ZeroString));
			WEAKNESS_PROB					= Integer.parseInt(skill.getProperty("WEAKNESS_PROB", StringUtil.ZeroString));
			WEAPON_BREAK_PROB				= Integer.parseInt(skill.getProperty("WEAPON_BREAK_PROB", StringUtil.ZeroString));
			CURSE_BLIND_PROB				= Integer.parseInt(skill.getProperty("CURSE_BLIND_PROB", StringUtil.ZeroString));			
			FOG_OF_SLEEPING_PROB			= Integer.parseInt(skill.getProperty("FOG_OF_SLEEPING_PROB", StringUtil.ZeroString));
			DARKNESS_PROB					= Integer.parseInt(skill.getProperty("DARKNESS_PROB", StringUtil.ZeroString));			
			SHAPE_CHANGE_PROB				= Integer.parseInt(skill.getProperty("SHAPE_CHANGE_PROB", StringUtil.ZeroString));
			CURSE_PARALYZE_PROB				= Integer.parseInt(skill.getProperty("CURSE_PARALYZE_PROB", StringUtil.ZeroString));
			DEATH_HEAL_PROB					= Integer.parseInt(skill.getProperty("DEATH_HEAL_PROB", "30"));
			TURN_UNDEAD_PROB				= Integer.parseInt(skill.getProperty("TURN_UNDEAD_PROB", StringUtil.ZeroString));			
			MANA_DRAIN_PROB					= Integer.parseInt(skill.getProperty("MANA_DRAIN_PROB", StringUtil.ZeroString));
			CANCELLATION_PROB				= Integer.parseInt(skill.getProperty("CANCELLATION_PROB", StringUtil.ZeroString));
			ETERNITY_PROB					= Integer.parseInt(skill.getProperty("ETERNITY_PROB", "40"));	
			ARTERIALCIRCLE_PROB				= Integer.parseInt(skill.getProperty("ARTERIALCIRCLE_PROB", "20"));
			NEMESIS_STUN_PROB				= Integer.parseInt(skill.getProperty("NEMESIS_STUN_PROB", "60"));
			ELF_CANCELLATION_PROB			= Integer.parseInt(skill.getProperty("ELF_CANCELLATION_PROB", StringUtil.ZeroString));
			
			HALPHAS_PROB					= Integer.parseInt(skill.getProperty("HALPHAS_PROB", "15"));
			FOUSLAYER_STUN_PROB				= Integer.parseInt(skill.getProperty("FOUSLAYER_STUN_PROB", "20"));
			FOUSLAYER_FORCE_STUN_PROB		= Integer.parseInt(skill.getProperty("FOUSLAYER_FORCE_STUN_PROB", "5"));
			EXPOSE_WEAKNESS_PROB			= Integer.parseInt(skill.getProperty("EXPOSE_WEAKNESS_PROB", "5"));
			CHAIN_REACTION_PROB				= Integer.parseInt(skill.getProperty("CHAIN_REACTION_PROB", "40"));
			BEHEMOTH_DEBUFF_PROB			= Integer.parseInt(skill.getProperty("BEHEMOTH_DEBUFF_PROB", "40"));
			BEHEMOTH_MOVE_SPEED_RATE		= Integer.parseInt(skill.getProperty("BEHEMOTH_MOVE_SPEED_RATE", "35"));
					
			BONE_BREAK_PROB					= Integer.parseInt(skill.getProperty("BONE_BREAK_PROB", "40"));
			PHANTASM_PROB					= Integer.parseInt(skill.getProperty("PHANTASM_PROB", "30"));
			ENSNARE_CHANCE					= Integer.parseInt(skill.getProperty("ENSNARE_CHANCE", "30"));
			ENSNARE_MOVE_SPEED_RATE			= Integer.parseInt(skill.getProperty("ENSNARE_MOVE_SPEED_RATE", "35"));
			OSIRIS_CHANCE					= Integer.parseInt(skill.getProperty("OSIRIS_CHANCE", "30"));
			OSIRIS_TRANSITION_COUNT			= Integer.parseInt(skill.getProperty("OSIRIS_TRANSITION_COUNT", "2"));
			
			DESPERADO_CHANCE				= Integer.parseInt(skill.getProperty("DESPERADO_CHANCE", "40"));			
			DESPERADO_LEVEL_RATE			= Integer.parseInt(skill.getProperty("DESPERADO_LEVEL_RATE", "2"));
			POWER_GRIP						= Integer.parseInt(skill.getProperty("POWER_GRIP", "40"));	
			TITAN_LOCK						= Integer.parseInt(skill.getProperty("TITAN_LOCK", "30"));
			TITAN_BLICK						= Integer.parseInt(skill.getProperty("TITAN_BLICK", "30"));
			TITAN_MAGIC						= Integer.parseInt(skill.getProperty("TITAN_MAGIC", "30"));
			FURY							= Integer.parseInt(skill.getProperty("FURY", "5"));
			TEMPEST							= Integer.parseInt(skill.getProperty("TEMPEST", "40"));
			TITAN_BEAST_MILLISECOND			= Integer.parseInt(skill.getProperty("TITAN_BEAST_MILLISECOND", "50"));
			TOMAHAWK_HUNT_HP_FIX_VALUE		= Integer.parseInt(skill.getProperty("TOMAHAWK_HUNT_HP_FIX_VALUE", "10"));
			TOMAHAWK_HUNT_HP_RANDOM_VALUE	= Integer.parseInt(skill.getProperty("TOMAHAWK_HUNT_HP_RANDOM_VALUE", "15"));
			SLAYER_ATTACK_SPEED_RATE		= Integer.parseInt(skill.getProperty("SLAYER_ATTACK_SPEED_RATE", "10"));
			
			PANTERA_PROB					= Integer.parseInt(skill.getProperty("PANTERA_PROB", "40"));			
			PHANTOM_PROB					= Integer.parseInt(skill.getProperty("PHANTOM_PROB", "40"));	
			JUDGEMENT_PROB					= Integer.parseInt(skill.getProperty("JUDGEMENT_PROB", "40"));
			SURVIVE_PROB					= Integer.parseInt(skill.getProperty("SURVIVE_PROB", "15"));
			PARADOX_PROB					= Integer.parseInt(skill.getProperty("PARADOX_PROB", "20"));
			RAGE_PROB						= Integer.parseInt(skill.getProperty("RAGE_PROB", "10"));
			PHANTOM_REQUIEM_POTION_RATE		= Double.parseDouble(skill.getProperty("PHANTOM_REQUIEM_POTION_RATE", "0.5"));
			PHANTOM_DEATH_POTION_RATE		= Double.parseDouble(skill.getProperty("PHANTOM_DEATH_POTION_RATE", "1.0"));
			
			PRESSURE_PROB					= Integer.parseInt(skill.getProperty("PRESSURE_PROB", "40"));
			CRUEL_PROB						= Integer.parseInt(skill.getProperty("CRUEL_PROB", "40"));
			MAELSTROM_PROB					= Integer.parseInt(skill.getProperty("MAELSTROM_PROB", "20"));
			VANGUARD_MOVE_SPEED_RATE		= Integer.parseInt(skill.getProperty("VANGUARD_MOVE_SPEED_RATE", "35"));
			VANGUARD_ATTACK_SPEED_RATE		= Integer.parseInt(skill.getProperty("VANGUARD_ATTACK_SPEED_RATE", "10"));
			
			DIS_LOCK_DMG					= Integer.parseInt(skill.getProperty("DIS_LOCK_DMG", "0"));
			STUN_CONTINUE					= Boolean.parseBoolean(skill.getProperty("STUN_CONTINUE", StringUtil.TrueString));
			
			WIZARD_MAGIC_DMG_PVP			= Double.parseDouble(skill.getProperty("WIZARD_MAGIC_DMG_PVP", "1.0"));			
			WIZARD_MAGIC_DMG_PVE			= Double.parseDouble(skill.getProperty("WIZARD_MAGIC_DMG_PVE", "1.0"));
			WIZARD_STACK_TIME				= Integer.parseInt(skill.getProperty("WIZARD_STACK_TIME", "2000"));
			WIZARD_STACK_COUNT				= Integer.parseInt(skill.getProperty("WIZARD_STACK_COUNT", "5"));
			WIZARD_STACK_VALUE				= Double.parseDouble(skill.getProperty("WIZARD_STACK_VALUE", "0.05"));
			
			KEYRINK_DMG						= Integer.parseInt(skill.getProperty("KEYRINK_DMG", "15"));//키링크
			TRIPLE_DMG						= Double.parseDouble(skill.getProperty("TRIPLE_DMG", "1.0"));
			FOW_SLAYER_DMG					= Double.parseDouble(skill.getProperty("FOW_SLAYER_DMG", "1.0"));
			TRIPLE_STUN_CHANCE				= Integer.parseInt(skill.getProperty("TRIPLE_STUN_CHANCE", "10"));
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw new Error("Failed to Load " + SPELL_CONFIG_FILE + " File.");
		}
	}
}

