package l1j.server.server.construct;

import javolution.util.FastMap;
import l1j.server.server.model.L1CastleLocation;

public enum L1CastleType {
	KENT(		L1CastleLocation.KENT_CASTLE_ID,	"Kent Castle"),
	ORC(		L1CastleLocation.OT_CASTLE_ID,		"Orc Fortress"),
	WINDAWOOD(	L1CastleLocation.WW_CASTLE_ID,		"Windawood Castle"),
	GIRAN(		L1CastleLocation.GIRAN_CASTLE_ID,	"Giran Castle"),
	HEINE(		L1CastleLocation.HEINE_CASTLE_ID,	"Heine Castle"),
	DWARF(		L1CastleLocation.DOWA_CASTLE_ID,	"Dwarf Castle"),
	ADEN(		L1CastleLocation.ADEN_CASTLE_ID,	"Aden Castle"),
	DIAD(		L1CastleLocation.DIAD_CASTLE_ID,	"Diad Fortress"),
	;
	private int _id;
	private String _name;
	L1CastleType(int id, String name) {
		_id		= id;
		_name	= name;
	}
	public int getId(){
		return _id;
	}
	public String getName(){
		return _name;
	}
	
	private static final FastMap<Integer, L1CastleType> DATA;
	static {
		L1CastleType[] array = L1CastleType.values();
		DATA = new FastMap<Integer, L1CastleType>(array.length);
		for (L1CastleType castle : array) {
			DATA.put(castle._id, castle);
		}
	}
	
	public static L1CastleType fromInt(int id){
		return DATA.get(id);
	}
}

