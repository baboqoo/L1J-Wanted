package l1j.server.IndunSystem.clandungeon;

public enum ClanDungeonType {
	AREA(751),
	DAILY(840),
	WEEKLY(890),
	;
	private int _mapId;
	ClanDungeonType(int mapId) {
		_mapId = mapId;
	}
	public int getMapId(){
		return _mapId;
	}
}

