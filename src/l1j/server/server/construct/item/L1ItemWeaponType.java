package l1j.server.server.construct.item;

import java.util.Arrays;
import java.util.List;

import javolution.util.FastMap;

public enum L1ItemWeaponType {
	SWORD(			"SWORD",		1,	4,	0,		13411), // 한손검
	DAGGER(			"DAGGER",		2,	46,	0,		13412), // 단검
	TOHAND_SWORD(	"TOHAND_SWORD",	3,	50,	0,		13410), // 양손검
	BOW(			"BOW",			4,	20,	0,		13392), // 활
	SPEAR(			"SPEAR",		5,	24,	19278,	13402), // 창
	BLUNT(			"BLUNT",		6,	11,	0,		13414), // 도끼
	STAFF(			"STAFF",		7,	40,	0,		13413), // 지팡이
	STING(			"STING",		8,	0,	2989,	0), 	// 스팅
	ARROW(			"ARROW",		9,	0,	66,		0), 	// 화살
	GAUNTLET(		"GAUNTLET",		10,	62,	0,		13398),	// 건틀렛
	CLAW(			"CLAW",			11,	58,	0,		13416),	// 크로우
	EDORYU(			"EDORYU",		12,	54,	0,		13417),	// 이도류
	SINGLE_BOW(		"SINGLE_BOW",	13,	20,	0,		13392),	// 한손활
	SINGLE_SPEAR(	"SINGLE_SPEAR",	14,	24,	19278,	13402),	// 한손창
	TOHAND_BLUNT(	"TOHAND_BLUNT",	15,	11,	0,		13414),	// 양손도끼
	TOHAND_STAFF(	"TOHAND_STAFF",	16,	40,	0,		13413),	// 양손지팡이
	KEYRINGK(		"KEYRINGK",		17,	58,	21081,	21083),	// 키링크
	CHAINSWORD(		"CHAINSWORD",	18,	24,	0,		13409),	// 체인소드
	;
	private String _key;
	private int _id;
	private int _action;
	private int _sprite;
	private int _critical;
	L1ItemWeaponType(String key, int id, int action, int sprite, int critical) {
		_key		= key;
		_id			= id;
		_action		= action;
		_sprite		= sprite;
		_critical	= critical;
	}
	public String getKey(){
		return _key;
	}
	public int getId(){
		return _id;
	}
	public int getAction() {
		return _action;
	}
	public int getSprite() {
		return _sprite;
	}
	public int getCritical() {
		return _critical;
	}
	
	private static final List<L1ItemWeaponType> TWOHAND_WEAPON = Arrays.asList(new L1ItemWeaponType[] {
			TOHAND_SWORD, BOW, SPEAR, CLAW, EDORYU, TOHAND_BLUNT, TOHAND_STAFF, CHAINSWORD
	});
	private static final List<L1ItemWeaponType> SHORT_WEAPON = Arrays.asList(new L1ItemWeaponType[] {
			SWORD, DAGGER, TOHAND_SWORD, SPEAR, BLUNT, CLAW, EDORYU, SINGLE_SPEAR, TOHAND_BLUNT
	});
	private static final List<L1ItemWeaponType> LONG_WEAPON = Arrays.asList(new L1ItemWeaponType[] {
			BOW, GAUNTLET, SINGLE_BOW
	});
	private static final List<L1ItemWeaponType> RANGE_2_WEAPON = Arrays.asList(new L1ItemWeaponType[] {
			SPEAR, SINGLE_SPEAR, CHAINSWORD
	});
	private static final List<L1ItemWeaponType> SWORD_WEAPON = Arrays.asList(new L1ItemWeaponType[] {
			SWORD, DAGGER, TOHAND_SWORD
	});
	private static final List<L1ItemWeaponType> BOW_WEAPON = Arrays.asList(new L1ItemWeaponType[] {
			BOW, SINGLE_BOW
	});
	private static final List<L1ItemWeaponType> BLUNT_WEAPON = Arrays.asList(new L1ItemWeaponType[] {
			BLUNT, TOHAND_BLUNT
	});
	private static final List<L1ItemWeaponType> STAFF_WEAPON = Arrays.asList(new L1ItemWeaponType[] {
			STAFF, TOHAND_STAFF
	});
	private static final List<L1ItemWeaponType> SPEAR_WEAPON = Arrays.asList(new L1ItemWeaponType[] {
			SPEAR, SINGLE_SPEAR
	});

	private static final FastMap<Integer, L1ItemWeaponType> DATA;
	private static final FastMap<String, L1ItemWeaponType> KEY_DATA;
	static {
		L1ItemWeaponType[] array = L1ItemWeaponType.values();
		DATA		= new FastMap<Integer, L1ItemWeaponType>(array.length);
		KEY_DATA	= new FastMap<String, L1ItemWeaponType>(array.length);
		for (L1ItemWeaponType weapon : array) {
			DATA.put(weapon._id, weapon);
			KEY_DATA.put(weapon._key, weapon);
		}
	}
	
	public static L1ItemWeaponType fromString(String key){
		return KEY_DATA.get(key);
	}
	
	public static L1ItemWeaponType fromInt(int id){
		return DATA.get(id);
	}
	
	public static boolean isTwohandWeapon(L1ItemWeaponType val){
		return TWOHAND_WEAPON.contains(val);
	}
	
	public static boolean isShortWeapon(L1ItemWeaponType val){
		return SHORT_WEAPON.contains(val);
	}
	
	public static boolean isLongWeapon(L1ItemWeaponType val){
		return LONG_WEAPON.contains(val);
	}
	
	public static boolean isRange2Weapon(L1ItemWeaponType val){
		return RANGE_2_WEAPON.contains(val);
	}
	
	public static boolean isSwordWeapon(L1ItemWeaponType val){
		return SWORD_WEAPON.contains(val);
	}
	
	public static boolean isBowWeapon(L1ItemWeaponType val){
		return BOW_WEAPON.contains(val);
	}
	
	public static boolean isBluntWeapon(L1ItemWeaponType val){
		return BLUNT_WEAPON.contains(val);
	}
	
	public static boolean isStaffWeapon(L1ItemWeaponType val){
		return STAFF_WEAPON.contains(val);
	}
	
	public static boolean isSpearWeapon(L1ItemWeaponType val){
		return SPEAR_WEAPON.contains(val);
	}
}

