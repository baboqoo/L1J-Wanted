package l1j.server.IndunSystem.indun;

public enum IndunType {
	ORIM(		202,	731),
	CROCODILE(	203,	732),
	FANTASY(	204,	733),
	SPACE(		205,	734),
	AURAKIA(	206,	736),
	;
	private int _mapKind;
	private int _mapId;
	IndunType(int mapKind, int mapId) {
		_mapKind	= mapKind;
		_mapId		= mapId;
	}
	public int getMapKind(){
		return _mapKind;
	}
	public int getMapId(){
		return _mapId;
	}
	
	private static final IndunType[] ARRAY = IndunType.values();
	public static IndunType[] getArray(){
		return ARRAY;
	}
	public static IndunType getIndunType(int mapKind){
		for (IndunType type : ARRAY) {
			if (type._mapKind == mapKind) {
				return type;
			}
		}
		return null;
	}
}

