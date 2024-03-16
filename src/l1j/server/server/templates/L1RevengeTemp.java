package l1j.server.server.templates;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import l1j.server.server.serverpackets.revenge.S_RevengeInfo;

public class L1RevengeTemp {
	private int number;
	private int charId;
	private S_RevengeInfo.eAction actionType;
	private Timestamp registerTimestamp;
	private Timestamp unregisterDuration;
	private Timestamp actionTimestamp;
	private Timestamp actionDuration;
	private int actionRemainCount;
	private int actionCount;
	private int user_uid;
	private int gameClass;
	private String userName;
	private int pledgeId;
	private String pledgeName;
	
	public L1RevengeTemp(){}
	
	public L1RevengeTemp(ResultSet rs) throws SQLException{
		this(rs.getInt("number"), 
				rs.getInt("char_id"), 
				S_RevengeInfo.eAction.fromInt(rs.getInt("result")),
				rs.getTimestamp("starttime"), 
				rs.getTimestamp("endtime"), 
				rs.getTimestamp("chasestarttime"), 
				rs.getTimestamp("chaseendtime"),
				rs.getInt("usecount"), 
				rs.getInt("amount"), 
				rs.getInt("targetobjid"), 
				rs.getInt("targetclass"),
				rs.getString("targetname"), 
				rs.getInt("targetclanid"), 
				rs.getString("targetclanname"));
	}
	
	public L1RevengeTemp(int number, int charId, S_RevengeInfo.eAction actionType,
			Timestamp registerTimestamp, Timestamp unregisterDuration, Timestamp actionTimestamp, Timestamp actionDuration, 
			int actionRemainCount, int actionCount, int user_uid,
			int gameClass, String userName, int pledgeId,
			String pledgeName) {
		this.number = number;
		this.charId = charId;
		this.actionType = actionType;
		this.registerTimestamp = registerTimestamp;
		this.unregisterDuration = unregisterDuration;
		this.actionTimestamp = actionTimestamp;
		this.actionDuration = actionDuration;
		this.actionRemainCount = actionRemainCount;
		this.actionCount = actionCount;
		this.user_uid = user_uid;
		this.gameClass = gameClass;
		this.userName = userName;
		this.pledgeId = pledgeId;
		this.pledgeName = pledgeName;
	}
	
	public int getNumber() {
		return number;
	}
	public int getCharId() {
		return charId;
	}
	public S_RevengeInfo.eAction getActionType() {
		return actionType;
	}
	public void setActionType(S_RevengeInfo.eAction actionType) {
		this.actionType = actionType;
	}
	public Timestamp getRegisterTimestamp() {
		return registerTimestamp;
	}
	public Timestamp getUnregisterDuration() {
		return unregisterDuration;
	}
	public Timestamp getActionTimestamp() {
		return actionTimestamp;
	}
	public void setActionTimestamp(Timestamp actionTimestamp) {
		this.actionTimestamp = actionTimestamp;
	}
	public Timestamp getActionDuration() {
		return actionDuration;
	}
	public void setActionDuration(Timestamp actionDuration) {
		this.actionDuration = actionDuration;
	}
	public int getActionRemainCount() {
		return actionRemainCount;
	}
	public void setActionRemainCount(int actionRemainCount) {
		this.actionRemainCount = actionRemainCount;
	}
	public int getActionCount() {
		return actionCount;
	}
	public void setActionCount(int actionCount) {
		this.actionCount = actionCount;
	}
	public int getUserUid() {
		return user_uid;
	}
	public int getGameClass() {
		return gameClass;
	}
	public String getUserName() {
		return userName;
	}
	public int getPledgeId() {
		return pledgeId;
	}
	public void setPledgeId(int pledgeId) {
		this.pledgeId = pledgeId;
	}
	public String getPledgeName() {
		return pledgeName;
	}
	public void setPledgeName(String pledgeName) {
		this.pledgeName = pledgeName;
	}
}

