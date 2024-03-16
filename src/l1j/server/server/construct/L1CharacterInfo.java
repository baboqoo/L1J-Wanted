package l1j.server.server.construct;

import javolution.util.FastMap;
import l1j.server.common.data.Gender;

public interface L1CharacterInfo {
	
	public static enum L1Class {
		NORMAL(			99,	"normal",		"없음",		"Not exists"),
		CROWN(			0,	"crown",		"군주",		"Monarch"),
		KNIGHT(			1,	"knight",		"기사",		"Knight"),
		ELF(			2,	"elf",			"요정",		"Elf"),
		WIZARD(			3,	"wizard",		"마법사",	"Mage"),
		DARKELF(		4,	"darkelf",		"다크엘프",	"Dark Elf"),
		DRAGONKNIGHT(	5,	"dragonknight",	"용기사",	"Dragon Knight"),
		ILLUSIONIST(	6,	"illusionist",	"환술사",	"Illusionist"),
		WARRIOR(		7,	"warrior",		"전사",		"Warrior"),
		FENCER(			8,	"fencer",		"검사", 	"Fencer"),
		LANCER(			9,	"lancer",		"창기사", 	"Lancer"),
		NONE(			-1,	"none",			"없음", 	"None")
		;
		private int _type;
		private String _className;
		private String _classNameKr;
		private String _classNameEn;
		L1Class(int type, String className, String classNameKr, String classNameEn) {
			_type			= type;
			_className		= className;
			_classNameKr	= classNameKr;
			_classNameEn	= classNameEn;
		}
		public int getType(){
			return _type;
		}
		public String getClassName(){
			return _className;
		}
		public String getClassNameKr(){
			return _classNameKr;
		}
		public String getClassNameEn(){
			return _classNameEn;
		}
		
		private static final FastMap<Integer, L1Class> CLASS_DATA;
		private static final FastMap<Integer, String> CLASS_DATA_TYPE;
		private static final FastMap<String, Integer> CLASS_DATA_NAME;
		private static final FastMap<Integer, String> CLASS_DATA_NAME_KR;
		private static final FastMap<Integer, String> CLASS_DATA_NAME_EN;
		static {
			L1Class[] array		= L1Class.values();
			CLASS_DATA			= new FastMap<Integer, L1Class>();
			CLASS_DATA_TYPE		= new FastMap<Integer, String>(array.length);
			CLASS_DATA_NAME		= new FastMap<String, Integer>(array.length);
			CLASS_DATA_NAME_KR	= new FastMap<Integer, String>(array.length);
			CLASS_DATA_NAME_EN	= new FastMap<Integer, String>(array.length);
			for (L1Class value : array) {
				CLASS_DATA.put(value._type, value);
				CLASS_DATA_TYPE.put(value._type, value._className);
				CLASS_DATA_NAME.put(value._className, value._type);
				CLASS_DATA_NAME_KR.put(value._type, value._classNameKr);
				CLASS_DATA_NAME_EN.put(value._type, value._classNameEn);
			}
		}
		public static L1Class fromInt(int type){
			return CLASS_DATA.get(type);
		}
		public static int getType(String name){
			return CLASS_DATA_NAME.containsKey(name) ? CLASS_DATA_NAME.get(name) : NONE._type;
		}
		public static String getName(int type){
			return CLASS_DATA_TYPE.containsKey(type) ? CLASS_DATA_TYPE.get(type) : NONE._className;
		}
		public static String getNameKr(int type){
			return CLASS_DATA_NAME_KR.containsKey(type) ? CLASS_DATA_NAME_KR.get(type) : NONE._classNameKr;
		}
		public static String getNameEn(int type){
			return CLASS_DATA_NAME_EN.containsKey(type) ? CLASS_DATA_NAME_EN.get(type) : NONE._classNameEn;
		}
	}
	
	public static final int CLASSID_PRINCE					= 0;
    public static final int CLASSID_PRINCESS				= 1;
    public static final int CLASSID_KNIGHT_MALE				= 61;
    public static final int CLASSID_KNIGHT_FEMALE			= 48;
    public static final int CLASSID_ELF_MALE				= 138;
    public static final int CLASSID_ELF_FEMALE				= 37;
    public static final int CLASSID_WIZARD_MALE				= 20278;
    public static final int CLASSID_WIZARD_FEMALE			= 20279;
    public static final int CLASSID_DARK_ELF_MALE			= 2786;
    public static final int CLASSID_DARK_ELF_FEMALE			= 2796;
    public static final int CLASSID_DRAGONKNIGHT_MALE		= 6658;
    public static final int CLASSID_DRAGONKNIGHT_FEMALE		= 6661;
    public static final int CLASSID_ILLUSIONIST_MALE		= 6671;
    public static final int CLASSID_ILLUSIONIST_FEMALE		= 6650;
	public static final int CLASSID_WARRIOR_MALE			= 20567;
	public static final int CLASSID_WARRIOR_FEMALE			= 20577;
	public static final int CLASSID_FENCER_MALE				= 18520;
	public static final int CLASSID_FENCER_FEMALE			= 18499;
	public static final int CLASSID_LANCER_MALE				= 19296;
	public static final int CLASSID_LANCER_FEMALE			= 19299;
	
	public static final int[] MALE_LIST = { 
		CLASSID_PRINCE, 
		CLASSID_KNIGHT_MALE, 
		CLASSID_ELF_MALE, 
		CLASSID_WIZARD_MALE, 
		CLASSID_DARK_ELF_MALE, 
		CLASSID_DRAGONKNIGHT_MALE, 
		CLASSID_ILLUSIONIST_MALE, 
		CLASSID_WARRIOR_MALE, 
		CLASSID_FENCER_MALE, 
		CLASSID_LANCER_MALE 
	};
	
	public static final int[] FEMALE_LIST = { 
		CLASSID_PRINCESS, 
		CLASSID_KNIGHT_FEMALE, 
		CLASSID_ELF_FEMALE, 
		CLASSID_WIZARD_FEMALE, 
		CLASSID_DARK_ELF_FEMALE, 
		CLASSID_DRAGONKNIGHT_FEMALE, 
		CLASSID_ILLUSIONIST_FEMALE, 
		CLASSID_WARRIOR_FEMALE, 
		CLASSID_FENCER_FEMALE, 
		CLASSID_LANCER_FEMALE 
	};
	
	public static final int[][] CLASS_GFX_IDS = { 
		{ CLASSID_PRINCE,				CLASSID_PRINCESS }, 
		{ CLASSID_KNIGHT_FEMALE,		CLASSID_KNIGHT_MALE }, 
		{ CLASSID_ELF_FEMALE,			CLASSID_ELF_MALE }, 
		{ CLASSID_WIZARD_MALE,			CLASSID_WIZARD_FEMALE }, 
		{ CLASSID_DARK_ELF_MALE,		CLASSID_DARK_ELF_FEMALE },
		{ CLASSID_DRAGONKNIGHT_MALE,	CLASSID_DRAGONKNIGHT_FEMALE },
		{ CLASSID_ILLUSIONIST_MALE,		CLASSID_ILLUSIONIST_FEMALE },
		{ CLASSID_WARRIOR_MALE,			CLASSID_WARRIOR_FEMALE },
		{ CLASSID_FENCER_MALE,			CLASSID_FENCER_FEMALE },
		{ CLASSID_LANCER_MALE,			CLASSID_LANCER_FEMALE }
	};
	
	public static final java.util.List<Integer> CLASS_POLY_LIST = java.util.Arrays.asList(
		new Integer[]{
			CLASSID_PRINCE, CLASSID_PRINCESS,
			CLASSID_KNIGHT_FEMALE, CLASSID_KNIGHT_MALE,
			CLASSID_ELF_FEMALE, CLASSID_ELF_MALE,
			CLASSID_WIZARD_MALE, CLASSID_WIZARD_FEMALE,
			CLASSID_DARK_ELF_MALE, CLASSID_DARK_ELF_FEMALE,
			CLASSID_DRAGONKNIGHT_MALE, CLASSID_DRAGONKNIGHT_FEMALE,
			CLASSID_ILLUSIONIST_MALE, CLASSID_ILLUSIONIST_FEMALE,
			CLASSID_WARRIOR_MALE, CLASSID_WARRIOR_FEMALE,
			CLASSID_FENCER_MALE, CLASSID_FENCER_FEMALE,
			CLASSID_LANCER_MALE, CLASSID_LANCER_FEMALE
		}
	);
	
	public static final int CLASS_SIZE						= MALE_LIST.length;
	
	public static final int RANKING_PRINCE_MALE				= 21817;
	public static final int RANKING_PRINCE_FEMALE			= 21789;
	public static final int RANKING_KNIGHT_MALE				= 15115;
	public static final int RANKING_KNIGHT_FEMALE			= 13721;
	public static final int RANKING_ELF_MALE				= 13723;
	public static final int RANKING_ELF_FEMALE				= 13725;
	public static final int RANKING_WIZARD_MALE				= 20263;
	public static final int RANKING_WIZARD_FEMALE			= 20270;
	public static final int RANKING_DARK_ELF_MALE			= 13731;
	public static final int RANKING_DARK_ELF_FEMALE			= 13733;
	public static final int RANKING_DRAGONKNIGHT_MALE		= 21624;
	public static final int RANKING_DRAGONKNIGHT_FEMALE		= 21653;
	public static final int RANKING_ILLUSIONIST_MALE		= 21098;
	public static final int RANKING_ILLUSIONIST_FEMALE		= 21094;
	public static final int RANKING_WARRIOR_MALE			= 20619;
	public static final int RANKING_WARRIOR_FEMALE			= 20612;
	public static final int RANKING_FENCER_MALE				= 18555;
	public static final int RANKING_FENCER_FEMALE			= 18551;
	public static final int RANKING_LANCER_MALE				= 19824;
	public static final int RANKING_LANCER_FEMALE			= 19825;
	
	public static final int[] RANKING_PRINCE_POLYS			= { RANKING_PRINCE_MALE,		RANKING_PRINCE_FEMALE };
	public static final int[] RANKING_KNIGHT_POLYS			= { RANKING_KNIGHT_MALE,		RANKING_KNIGHT_FEMALE };
	public static final int[] RANKING_ELF_POLYS				= { RANKING_ELF_MALE,			RANKING_ELF_FEMALE };
	public static final int[] RANKING_WIZARD_POLYS			= { RANKING_WIZARD_MALE,		RANKING_WIZARD_FEMALE };
	public static final int[] RANKING_DARK_ELF_POLYS		= { RANKING_DARK_ELF_MALE,		RANKING_DARK_ELF_FEMALE };
	public static final int[] RANKING_DRAGONKNIGHT_POLYS	= { RANKING_DRAGONKNIGHT_MALE,	RANKING_DRAGONKNIGHT_FEMALE };
	public static final int[] RANKING_ILLUSIONIST_POLYS		= { RANKING_ILLUSIONIST_MALE,	RANKING_ILLUSIONIST_FEMALE };
	public static final int[] RANKING_WARRIOR_POLYS			= { RANKING_WARRIOR_MALE,		RANKING_WARRIOR_FEMALE };
	public static final int[] RANKING_FENCER_POLYS			= { RANKING_FENCER_MALE,		RANKING_FENCER_FEMALE };
	public static final int[] RANKING_LANCER_POLYS			= { RANKING_LANCER_MALE,		RANKING_LANCER_FEMALE };
	
	public static final int[][] RANKING_POLYS	= {
		RANKING_PRINCE_POLYS,
		RANKING_KNIGHT_POLYS,
		RANKING_ELF_POLYS,
		RANKING_WIZARD_POLYS,
		RANKING_DARK_ELF_POLYS,
		RANKING_DRAGONKNIGHT_POLYS,
		RANKING_ILLUSIONIST_POLYS,
		RANKING_WARRIOR_POLYS,
		RANKING_FENCER_POLYS,
		RANKING_LANCER_POLYS
	};
	
	public static final java.util.List<Integer> RANKING_POLY_LIST = java.util.Arrays.asList(
		new Integer[]{
			RANKING_PRINCE_MALE, RANKING_PRINCE_FEMALE,
			RANKING_KNIGHT_MALE, RANKING_KNIGHT_FEMALE,
			RANKING_ELF_MALE, RANKING_ELF_FEMALE,
			RANKING_WIZARD_MALE, RANKING_WIZARD_FEMALE,
			RANKING_DARK_ELF_MALE, RANKING_DARK_ELF_FEMALE,
			RANKING_DRAGONKNIGHT_MALE, RANKING_DRAGONKNIGHT_FEMALE,
			RANKING_ILLUSIONIST_MALE, RANKING_ILLUSIONIST_FEMALE,
			RANKING_WARRIOR_MALE, RANKING_WARRIOR_FEMALE,
			RANKING_FENCER_MALE, RANKING_FENCER_FEMALE,
			RANKING_LANCER_MALE, RANKING_LANCER_FEMALE
		}
	);
	
	public static final java.util.List<String> GAME_MASTER_NAME_LIST = java.util.Arrays.asList(new String[]{
			//"메티스", "미소피아", "카시오페아", "나델만", "클레마티스", "운영자", "부운영자"
			"Metis", "Misopia", "Cassiopeia", "Nadelmann", "Clematis", "Operator", "DeputyOp"
	});

	public static boolean isGmName(String gmName){
		return GAME_MASTER_NAME_LIST.contains(gmName);
	}
	
	// START_LOC
	public static final int[][]	START_LOC_X		= { 
		{ 33434, 33435, 33440, 33424, 33415 },// 기란
		{ 32734, 32734, 32732, 32730, 32731 }// 붉은 기사단 훈련소
	};
	public static final int[][]	START_LOC_Y		= { 
		{ 32815, 32823, 32797, 32813, 32824 },// 기란
		{ 32810, 32812, 32810, 32809, 32812 }// 붉은 기사단 훈련소
	};
	public static final short[]	MAPID_LIST		= { 4, 3 };// map
	
	public static int getClassId(String val, Gender gender){
		//'lancer','fencer','warrior','illusionist','dragonknight','darkelf','wizard','elf','knight','crown'
		boolean isMale = gender.equals(Gender.MALE);
		switch(val){
		case "Monarch":
		case "MONARCH":
		case "monarch":
		case "royal":
		case "Royal":
		case "ROYAL":
		case "Crown":
		case "crown":		
		case "CROWN":
			return isMale ? CLASSID_PRINCE : CLASSID_PRINCESS;
		case "Knight":
		case "knight":
		case "KNIGHT":
			return isMale ? CLASSID_KNIGHT_MALE : CLASSID_KNIGHT_FEMALE;
		case "Elf":
		case "elf":
		case "ELF":
			return isMale ? CLASSID_ELF_MALE : CLASSID_ELF_FEMALE;
		case "Mage":
		case "mage":
		case "MAGE":
		case "Wizard":
		case "wizard":
		case "WIZARD":
			return isMale ? CLASSID_WIZARD_MALE : CLASSID_WIZARD_FEMALE;
		case "Darkelf":
		case "darkelf":
		case "DARKELF":
			return isMale ? CLASSID_DARK_ELF_MALE : CLASSID_DARK_ELF_FEMALE;
		case "Dragonknight":
		case "dragonknight":
		case "DRAGONKNIGHT":
			return isMale ? CLASSID_DRAGONKNIGHT_MALE : CLASSID_DRAGONKNIGHT_FEMALE;
		case "Illusionist":
		case "illusionist":
		case "ILLUSIONIST":
			return isMale ? CLASSID_ILLUSIONIST_MALE : CLASSID_ILLUSIONIST_FEMALE;
		case "Warrior":
		case "warrior":
		case "WARRIOR":
			return isMale ? CLASSID_WARRIOR_MALE : CLASSID_WARRIOR_FEMALE;
		case "Fencer":			
		case "fencer":
		case "FENCER":
			return isMale ? CLASSID_FENCER_MALE : CLASSID_FENCER_FEMALE;
		case "Lancer":			
		case "lancer":
		case "LANCER":
			return isMale ? CLASSID_LANCER_MALE : CLASSID_LANCER_FEMALE;
		default:
			throw new IllegalArgumentException(String.format("invalid arguments CLASS_ID, %s", val));
		}
	}
	
	public static void init(){}
}

