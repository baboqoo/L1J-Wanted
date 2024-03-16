package l1j.server.GameSystem.craft.bean;

import java.sql.ResultSet;
import java.sql.SQLException;

import l1j.server.common.bin.craft.Craft;

public class L1CraftSuccessCountUser {
	private String accountName;
	private int charId;
	private int craftId;
	private Craft.eSuccessCountType success_count_type;
	private int currentCount;
	
	public L1CraftSuccessCountUser(ResultSet rs) throws SQLException {
		this(rs.getString("accountName"), rs.getInt("charId"), rs.getInt("craftId"), 
				Craft.eSuccessCountType.fromString(rs.getString("success_count_type")),
				rs.getInt("currentCount"));
	}
	
	public L1CraftSuccessCountUser(String accountName, int charId, int craftId, Craft.eSuccessCountType success_count_type, int currentCount) {
		this.accountName		= accountName;
		this.charId				= charId;
		this.craftId			= craftId;
		this.success_count_type	= success_count_type;
		this.currentCount		= currentCount;
	}

	public String getAccountName() {
		return accountName;
	}
	public int getCharId() {
		return charId;
	}
	public int getCraftId() {
		return craftId;
	}
	public Craft.eSuccessCountType getSuccessCountType() {
		return success_count_type;
	}
	public int getCurrentCount() {
		return currentCount;
	}
	public void addCurrentCount(int value){
		this.currentCount += value;
	}
}

