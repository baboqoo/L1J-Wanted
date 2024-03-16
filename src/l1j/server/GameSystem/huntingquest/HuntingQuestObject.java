package l1j.server.GameSystem.huntingquest;

import java.sql.ResultSet;
import java.sql.SQLException;

import l1j.server.server.utils.StringUtil;

public class HuntingQuestObject {
	private int map_number, location_desc, quest_id;
	private boolean is_use;
	
	public HuntingQuestObject(ResultSet rs) throws SQLException {
		this.map_number		= rs.getInt("map_number");
		this.location_desc	= rs.getInt("location_desc");
		this.quest_id		= rs.getInt("quest_id");
		this.is_use			= Boolean.parseBoolean(rs.getString("is_use"));
	}
	
	public int getMapNumber() {
		return map_number;
	}

	public int getLocationDesc() {
		return location_desc;
	}

	public int getQuestId() {
		return quest_id;
	}
	
	public boolean isUse() {
		return is_use;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("map_number : ").append(map_number).append(StringUtil.LineString);
		sb.append("location_desc : ").append(location_desc).append(StringUtil.LineString);
		sb.append("quest_id : ").append(quest_id).append(StringUtil.LineString);
		sb.append("is_use : ").append(is_use);
		return sb.toString();
	}
}

