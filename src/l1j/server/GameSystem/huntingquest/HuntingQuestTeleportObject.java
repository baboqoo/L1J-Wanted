package l1j.server.GameSystem.huntingquest;

import java.sql.ResultSet;
import java.sql.SQLException;

import l1j.server.server.utils.StringUtil;

public class HuntingQuestTeleportObject {
	private final String action_string;
	private final int telMapid, telX, telY, telItemId;
	
	public HuntingQuestTeleportObject(ResultSet rs) throws SQLException {
		this.action_string	= rs.getString("action_string");
		this.telMapid		= rs.getInt("tel_mapid");
		this.telX			= rs.getInt("tel_x");
		this.telY			= rs.getInt("tel_y");
		this.telItemId		= rs.getInt("tel_itemid");
	}

	public String getActionString() {
		return action_string;
	}

	public int getTelMapId() {
		return telMapid;
	}

	public int getTelX() {
		return telX;
	}

	public int getTelY() {
		return telY;
	}

	public int getTelItemId() {
		return telItemId;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("action_string : ").append(action_string).append(StringUtil.LineString);
		sb.append("telMapid : ").append(telMapid).append(StringUtil.LineString);
		sb.append("telX : ").append(telX).append(StringUtil.LineString);
		sb.append("telY : ").append(telY).append(StringUtil.LineString);
		sb.append("telItemId : ").append(telItemId);
		return sb.toString();
	}
}

