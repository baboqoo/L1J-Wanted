package l1j.server.server.templates;

public class L1UserRanking {
	private String name;
	private int classId;
	private int curRank;
	private int oldRank;
	private int subCurRank;
	private int subOldRank;

	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return this.name;
	}

	public void setClassId(int id) {
		this.classId = id;
	}
	public int getClassId() {
		return this.classId;
	}

	public void setCurRank(int rank) {
		this.curRank = rank;
	}
	public int getCurRank() {
		return this.curRank;
	}

	public void setOldRank(int rank) {
		this.oldRank = rank;
	}
	public int getOldRank() {
		return this.oldRank;
	}
	
	public void setSubCurRank(int rank) {
		this.subCurRank = rank;
	}
	public int getSubCurRank() {
		return this.subCurRank;
	}

	public void setSubOldRank(int rank) {
		this.subOldRank = rank;
	}
	public int getSubOldRank() {
		return this.subOldRank;
	}
}

