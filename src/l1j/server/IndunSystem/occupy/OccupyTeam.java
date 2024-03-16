package l1j.server.IndunSystem.occupy;

import javolution.util.FastTable;

public class OccupyTeam {
	private int badgeId;
	private OccupyTeamType teamType;
	private int point;// 점수
	private FastTable<String> teamNameList;// 팀원 이름 리스트(가동중 유지를 위해 이름으로 관리)
	private FastTable<String> largeBadgeNameList;
	
	public OccupyTeam(OccupyTeamType teamType, int point, FastTable<String> teamNameList, FastTable<String> largeBadgeNameList) {
		this.teamType			= teamType;
		this.point				= point;
		this.teamNameList		= teamNameList;
		this.largeBadgeNameList	= largeBadgeNameList;
		this.badgeId			= OccupyUtil.getBadgeId(teamType);
	}
	
	public int getBadgeId(){
		return badgeId;
	}
	public OccupyTeamType getTeamType() {
		return teamType;
	}
	public void setTeamType(OccupyTeamType teamType) {
		this.teamType = teamType;
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
	public void addPoint(int point) {
		this.point += point;
	}
	public FastTable<String> getTeamNameList() {
		return teamNameList;
	}
	public void setTeamNameList(FastTable<String> teamNameList) {
		this.teamNameList = teamNameList;
	}
	public FastTable<String> getLargeBadgeNameList() {
		return largeBadgeNameList;
	}
	public void setLargeBadgeNameList(FastTable<String> largeBadgeNameList) {
		this.largeBadgeNameList = largeBadgeNameList;
	}
	public void dispose(){
		this.teamNameList.clear();
		this.largeBadgeNameList.clear();
		this.teamType			= null;
		this.teamNameList		= null;
		this.largeBadgeNameList	= null;
	}
}

