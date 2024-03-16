package l1j.server.GameSystem.arca;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 캐릭터의 탐 예약 정보
 * @author LinOffice
 */
public class L1ArcaRemain {
	private int id;
	private int charId;
	private int day;
	private int useItemId;
	
	public L1ArcaRemain(ResultSet rs) throws SQLException {
		this(rs.getInt("id"), rs.getInt("charId"), rs.getInt("day"), rs.getInt("useItemId"));
	}
	
	public L1ArcaRemain(int id, int charId, int day, int useItemId) {
		this.id			= id;
		this.charId		= charId;
		this.day		= day;
		this.useItemId	= useItemId;
	}
	
	public int getId() {
		return id;
	}
	public int getCharId() {
		return charId;
	}
	public int getDay() {
		return day;
	}
	public int getUseItemId() {
		return useItemId;
	}
}

