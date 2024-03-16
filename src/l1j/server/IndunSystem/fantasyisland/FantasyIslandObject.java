package l1j.server.IndunSystem.fantasyisland;

import java.sql.ResultSet;
import java.sql.SQLException;

import l1j.server.server.datatables.NpcTable;
import l1j.server.server.templates.L1Npc;

public class FantasyIslandObject {
	private int _type;
	private int _npcId;
	private L1Npc _template;
	private int _locX;
	private int _locY;
	private int _heading;
	private int _count;
	
	public FantasyIslandObject(ResultSet rs) throws SQLException {
		_type		= rs.getInt("type");
		_npcId		= rs.getInt("npc_id");
		_template	= NpcTable.getInstance().getTemplate(_npcId);
		if (_template == null) {
			System.out.println(String.format("[FantasyIslandObject] NPC_TEMPLATE_NOT_FOUND NPCID(%d)", _npcId));
		}
		_locX		= rs.getInt("locx");
		_locY		= rs.getInt("locy");
		_heading	= rs.getInt("heading");
		_count		= rs.getInt("count");
	}
	
	public int getType() {
		return _type;
	}
	public int getNpcId() {
		return _npcId;
	}
	public L1Npc getTemplate() {
		return _template;
	}
	public int getLocX() {
		return _locX;
	}
	public int getLocY() {
		return _locY;
	}
	public int getHeading() {
		return _heading;
	}
	public int getCount() {
		return _count;
	}
}

