package l1j.server.IndunSystem.occupy;

import l1j.server.server.model.L1TownLocation;

public enum OccupyType {
	HEINE(		"Heine",		L1TownLocation.MAP_WOLRDWAR_HEINE_TOWER,		L1TownLocation.MAP_WOLRDWAR_HEINE_EVA),
	WINDAWOOD(	"Windawood",	L1TownLocation.MAP_WOLRDWAR_WINDAWOOD_TOWER,	L1TownLocation.MAP_WOLRDWAR_WINDAWOOD_AZUR),
	;
	private String _desc;
	private short _warMap;
	private short _bossMap;
	OccupyType(String desc, short warMap, short bossMap) {
		_desc		= desc;
		_warMap		= warMap;
		_bossMap	= bossMap;
	}
	public String getDesc(){
		return _desc;
	}
	public short getWarMap(){
		return _warMap;
	}
	public short getBossMap(){
		return _bossMap;
	}
}

