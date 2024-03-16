package l1j.server.IndunSystem.ruun;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RuunSpawnObject {
	private int _id, _stage, _npcId, _locX, _locY, _mapId, _range, _count;
	
	protected RuunSpawnObject(ResultSet rs) throws SQLException{
		_id		= rs.getInt("id");
		_stage	= rs.getInt("stage");
		_npcId	= rs.getInt("npcId");
		_locX	= rs.getInt("locX");
		_locY	= rs.getInt("locY");
		_mapId	= rs.getInt("mapId");
		_range	= rs.getInt("range");
		_count	= rs.getInt("count");
	}
	
	protected int getId() {
		return _id;
	}
	protected int getStage() {
		return _stage;
	}
	protected int getNpcId() {
		return _npcId;
	}
	protected int getLocX() {
		return _locX;
	}
	protected int getLocY() {
		return _locY;
	}
	protected int getMapId() {
		return _mapId;
	}
	protected int getRange() {
		return _range;
	}
	protected int getCount() {
		return _count;
	}
}

