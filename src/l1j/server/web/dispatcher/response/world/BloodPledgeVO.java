package l1j.server.web.dispatcher.response.world;

import java.sql.Timestamp;

import l1j.server.server.utils.StringUtil;

public class BloodPledgeVO {
	private int pledgeId;
	private String pledgeName;
	private String leaderName;
	private String hasCastle;
	private String hasHouse;
	private int totalMember;
	private int exp;
	private Timestamp birthday;
	private String joinBtn = StringUtil.EmptyString;
	
	public BloodPledgeVO(int pledgeId, String pledgeName, String leaderName, String hasCastle, String hasHouse, int totalMember, int exp, Timestamp birthday) {
		this.pledgeId		= pledgeId;
		this.pledgeName		= pledgeName;
		this.leaderName		= leaderName;
		this.hasCastle		= hasCastle;
		this.hasHouse		= hasHouse;
		this.totalMember	= totalMember;
		this.exp			= exp;
		this.birthday		= birthday;
	}
	
	public int getPledgeId() {
		return pledgeId;
	}
	public String getPledgeName() {
		return pledgeName;
	}
	public String getLeaderName() {
		return leaderName;
	}
	public String getHasCastle() {
		return hasCastle;
	}
	public String getHasHouse() {
		return hasHouse;
	}
	public int getTotalMember() {
		return totalMember;
	}
	public void setTotalMember(int val) {
		totalMember = val;
	}
	public int getExp() {
		return exp;
	}
	public Timestamp getBirthday() {
		return birthday;
	}
	public String getJoinBtn() {
		return joinBtn;
	}
	public void setJoinBtn(String val) {
		joinBtn = val;
	}
}

