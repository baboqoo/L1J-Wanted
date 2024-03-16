package l1j.server.IndunSystem.indun;

import java.sql.ResultSet;
import java.sql.SQLException;

import l1j.server.server.datatables.NpcTable;
import l1j.server.server.templates.L1Npc;

public class IndunSpawnObject {
	private int type;
	private String location;
	private int npcId;
	private L1Npc template;
	private int locX, locY, heaing;
	
	public IndunSpawnObject(ResultSet rs) throws SQLException {
		this.type		= rs.getInt("type");
		this.location	= rs.getString("location");
		this.npcId		= rs.getInt("npc_id");
		this.template	= NpcTable.getInstance().getTemplate(this.npcId);
		if (this.template == null) {
			System.out.println(String.format("[IndunSpawnObject] NPC_TEMPLATE_NOT_FOUND NPCID(%d)", this.npcId));
		}
		this.locX		= rs.getInt("locx");
		this.locY		= rs.getInt("locy");
		this.heaing		= rs.getInt("heading");
	}
	
	public int getType() {
		return type;
	}
	public String getLocation() {
		return location;
	}
	public int getNpcId() {
		return npcId;
	}
	public L1Npc getTemplate() {
		return template;
	}
	public int getLocX() {
		return locX;
	}
	public int getLocY() {
		return locY;
	}
	public int getHeaing() {
		return heaing;
	}
}

