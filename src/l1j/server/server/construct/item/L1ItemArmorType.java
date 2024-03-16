package l1j.server.server.construct.item;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public enum L1ItemArmorType {
	NONE(		"NONE",		0),
	HELMET(		"HELMET",	1), // 투구
	ARMOR(		"ARMOR",	2), // 갑옷
	T_SHIRT(	"T_SHIRT",	3), // 티셔츠
	CLOAK(		"CLOAK",	4), // 망토
	GLOVE(		"GLOVE",	5), // 장갑
	BOOTS(		"BOOTS",	6), // 부츠
	SHIELD(		"SHIELD",	7), // 방패
	AMULET(		"AMULET",	8), // 목걸이
	RING(		"RING",		9), // 반지
	BELT(		"BELT",		10),// 벨트
	RING_2(		"RING_2",	11),// 반지(사용안함)
	EARRING(	"EARRING",	12),// 귀걸이
	GARDER(		"GARDER",	13),// 가더
	RON(		"RON",		14),// 룬1
	PAIR(		"PAIR",		15),// 각반
	SENTENCE(	"SENTENCE",	16),// 문장
	SHOULDER(	"SHOULDER",	17),// 견갑
	BADGE(		"BADGE",	18),// 휘장
	PENDANT(	"PENDANT",	19),// 펜던트
	;
	private String _key;
	private int _id;
	L1ItemArmorType(String key, int id) {
		_key	= key;
		_id		= id;
	}
	public String getKey(){
		return _key;
	}
	public int getId(){
		return _id;
	}
	
	private static final L1ItemArmorType[] ARRAY = L1ItemArmorType.values();
	private static final HashMap<Integer, L1ItemArmorType> DATA;
	private static final HashMap<String, L1ItemArmorType> KEY_DATA;
	static {
		DATA		= new HashMap<Integer, L1ItemArmorType>();
		KEY_DATA	= new HashMap<String, L1ItemArmorType>();
		for (L1ItemArmorType armor : ARRAY) {
			DATA.put(armor._id, armor);
			KEY_DATA.put(armor._key, armor);
		}
	}
	
	public static L1ItemArmorType fromString(String key){
		return KEY_DATA.get(key);
	}
	
	public static L1ItemArmorType fromInt(int id){
		return DATA.get(id);
	}
	
	private static final List<Integer> ACCESSARYS = Arrays.asList(new Integer[] {
			AMULET._id, RING._id, BELT._id, RING_2._id, EARRING._id, RON._id, SENTENCE._id, BADGE._id, PENDANT._id
	});
	public static boolean isAccessary(int type){
		return ACCESSARYS.contains(type);
	}
	
	private static final List<Integer> RINGS = Arrays.asList(new Integer[] {
			RING._id, RING_2._id
	});
	public static boolean isRing(int type){
		return RINGS.contains(type);
	}
}

