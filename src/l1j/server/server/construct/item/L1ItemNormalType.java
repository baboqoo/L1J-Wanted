package l1j.server.server.construct.item;

import javolution.util.FastMap;

public enum L1ItemNormalType {
	ARROW(			"ARROW",		0),
	WAND(			"WAND",			1),
	LIGHT(			"LIGHT",		2),
	GEM(			"GEM",			3),
	TOTEM(			"TOTEM",		4),
	FIRE_CRACKER(	"FIRE_CRACKER",	5),
	POTION(			"POTION",		6),
	FOOD(			"FOOD",			7),
	SCROLL(			"SCROLL",		8),
	QUEST_ITEM(		"QUEST_ITEM",	9),
	SPELL_BOOK(		"SPELL_BOOK",	10),
	PET_ITEM(		"PET_ITEM",		11),
	OTHER(			"OTHER",		12),
	MATERIAL(		"MATERIAL",		13),
	EVENT(			"EVENT",		14),
	STING(			"STING",		15),
	TREASURE_BOX(	"TREASURE_BOX",	16),
	;
	private String _key;
	private int _id;
	L1ItemNormalType(String key, int id) {
		_key	= key;
		_id		= id;
	}
	public String getKey(){
		return _key;
	}
	public int getId(){
		return _id;
	}
	
	private static final L1ItemNormalType[] ARRAY = L1ItemNormalType.values();
	private static final FastMap<Integer, L1ItemNormalType> DATA;
	private static final FastMap<String, L1ItemNormalType> KEY_DATA;
	static {
		DATA		= new FastMap<Integer, L1ItemNormalType>(ARRAY.length);
		KEY_DATA	= new FastMap<String, L1ItemNormalType>(ARRAY.length);
		for (L1ItemNormalType normal : ARRAY) {
			DATA.put(normal._id, normal);
			KEY_DATA.put(normal._key, normal);
		}
	}
	
	public static L1ItemNormalType fromInt(int id){
		return DATA.get(id);
	}
	
	public static L1ItemNormalType fromString(String key){
		return KEY_DATA.get(key);
	}
}

