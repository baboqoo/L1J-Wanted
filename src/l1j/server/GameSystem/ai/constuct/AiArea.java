package l1j.server.GameSystem.ai.constuct;

/**
 * Ai 지역
 * @author LinOffice
 */
public enum AiArea {
	GIKAM_FIRST_FLOOR(53),
	GIKAM_SECOND_FLOOR(54),
	FISHING(5490),
	;
	private int _mapId;
	AiArea(int mapId) {
		_mapId = mapId;
	}
	public int getMapId(){
		return _mapId;
	}
	public static AiArea fromInt(int val){
		switch(val){
		case 1:
			return GIKAM_FIRST_FLOOR;
		case 2:
			return GIKAM_SECOND_FLOOR;
		case 5490:
			return FISHING;
		default:
			return null;
		}
	}
}

