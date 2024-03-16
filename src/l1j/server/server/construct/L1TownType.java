package l1j.server.server.construct;

import java.util.concurrent.ConcurrentHashMap;

import l1j.server.server.model.L1TownLocation;

public enum L1TownType {
	/*TALK_ISLAND(	L1TownLocation.TOWNID_TALKING_ISLAND,	"말하는 섬"),
	SILVER_KNIGHT(	L1TownLocation.TOWNID_SILVER_KNIGHT,	"은기사"),
	GLUDIO(			L1TownLocation.TOWNID_GLUDIO,			"글로디오"),
	ORCISH_FOREST(	L1TownLocation.TOWNID_ORCISH_FOREST,	"오크"),
	WINDAWOOD(		L1TownLocation.TOWNID_WINDAWOOD,		"윈다우드"),
	KENT(			L1TownLocation.TOWNID_KENT,				"켄트"),
	GIRAN(			L1TownLocation.TOWNID_GIRAN,			"기란"),
	HEINE(			L1TownLocation.TOWNID_HEINE,			"하이네"),
	WERLDAN(		L1TownLocation.TOWNID_WERLDAN,			"웰던"),
	OREN(			L1TownLocation.TOWNID_OREN,				"오렌"),
	ELVEN_FOREST(	L1TownLocation.TOWNID_ELVEN_FOREST,		"요정숲"),
	ADEN(			L1TownLocation.TOWNID_ADEN,				"아덴"),
	SILENT_CAVERN(	L1TownLocation.TOWNID_SILENT_CAVERN,	"침묵의 동굴"),
	BEHEMOTH(		L1TownLocation.TOWNID_BEHEMOTH,			"베히모스"),
	SILVERIA(		L1TownLocation.TOWNID_SILVERIA,			"실베리아"),
	OUM_DUNGEON(	L1TownLocation.TOWNID_OUM_DUNGEON,		"오움 던전"),
	RESISTANCE(		L1TownLocation.TOWNID_RESISTANCE,		"대공동"),
	PIRATE_ISLAND(	L1TownLocation.TOWNID_PIRATE_ISLAND,	"해적섬"),
	RECLUSE_VILLAGE(L1TownLocation.TOWNID_RECLUSE_VILLAGE,	"저항군"),
	HIDDEN_VALLEY(	L1TownLocation.TOWNID_HIDDEN_VALLEY,	"숨겨진 계곡"),
	CLAUDIA(		L1TownLocation.TOWNID_CLAUDIA,			"클라우디아"),
	REDSOLDER(		L1TownLocation.TOWNID_REDSOLDER,		"붉은 기사단 훈련소"),
	SKYGARDEN(		L1TownLocation.TOWNID_SKYGARDEN,		"수상한 하늘 정원"),
	RUUN(			L1TownLocation.TOWNID_LUUN,				"루운");*/

	TALK_ISLAND(	L1TownLocation.TOWNID_TALKING_ISLAND,	"Talking Island"),
	SILVER_KNIGHT(	L1TownLocation.TOWNID_SILVER_KNIGHT,	"Silver Knight Village"),
	GLUDIO(			L1TownLocation.TOWNID_GLUDIO,			"Gludio"),
	ORCISH_FOREST(	L1TownLocation.TOWNID_ORCISH_FOREST,	"Orc Forest"),
	WINDAWOOD(		L1TownLocation.TOWNID_WINDAWOOD,		"Windawood"),
	KENT(			L1TownLocation.TOWNID_KENT,				"Kent"),
	GIRAN(			L1TownLocation.TOWNID_GIRAN,			"Giran"),
	HEINE(			L1TownLocation.TOWNID_HEINE,			"Heine"),
	WERLDAN(		L1TownLocation.TOWNID_WERLDAN,			"Weldern"),
	OREN(			L1TownLocation.TOWNID_OREN,				"Oren"),
	ELVEN_FOREST(	L1TownLocation.TOWNID_ELVEN_FOREST,		"Elven Forest"),
	ADEN(			L1TownLocation.TOWNID_ADEN,				"Aden"),
	SILENT_CAVERN(	L1TownLocation.TOWNID_SILENT_CAVERN,	"Silent Cavern"),
	BEHEMOTH(		L1TownLocation.TOWNID_BEHEMOTH,			"Behemoth"),
	SILVERIA(		L1TownLocation.TOWNID_SILVERIA,			"Silveria"),
	OUM_DUNGEON(	L1TownLocation.TOWNID_OUM_DUNGEON,		"Oum Dungeon"),
	RESISTANCE(		L1TownLocation.TOWNID_RESISTANCE,		"Resistance"),
	PIRATE_ISLAND(	L1TownLocation.TOWNID_PIRATE_ISLAND,	"Pirate Island"),
	RECLUSE_VILLAGE(L1TownLocation.TOWNID_RECLUSE_VILLAGE,	"Recluse Village"),
	HIDDEN_VALLEY(	L1TownLocation.TOWNID_HIDDEN_VALLEY,	"Hidden Valley"),
	CLAUDIA(		L1TownLocation.TOWNID_CLAUDIA,			"Claudia"),
	REDSOLDER(		L1TownLocation.TOWNID_REDSOLDER,		"Redsolder"),
	SKYGARDEN(		L1TownLocation.TOWNID_SKYGARDEN,		"Skygarden"),
	RUUN(			L1TownLocation.TOWNID_LUUN,				"Ruun Castle Village");
	
	private int _id;
	private String _name;
	L1TownType(int id, String name) {
		_id = id;
		_name = name;
	}
	public int getId(){
		return _id;
	}
	public String getName(){
		return _name;
	}
	
	private static final ConcurrentHashMap<String, L1TownType> DATA;
	static {
		DATA = new ConcurrentHashMap<String, L1TownType>();
		for (L1TownType val : L1TownType.values()) {
			DATA.put(val.name(), val);
		}
	}
	
	// get the town corresponding with the enum passed as parameter. For example, TALK_ISLAND, CLAUDIA, etc. 
	public static L1TownType fromString(String str) {
		return DATA.get(str);
	}
}

