package l1j.server.IndunSystem.occupy;

public enum OccupyTeamType {
	RED_KNIGHT(		401,	"Red Knights"),
	GOLD_KNIGHT(	402,	"Golden Knights"),
	BLACK_KNIGHT(	403,	"Black Knights"),
	;
	private int _teamId;
	private String _desc;
	OccupyTeamType(int teamId, String desc) {
		_teamId	= teamId;
		_desc	= desc;
	}
	public int getTeamId(){
		return _teamId;
	}
	public String getDesc(){
		return _desc;
	}
}

