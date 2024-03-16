package l1j.server.server.model;

/**
 * 랭킹 시스템<br>
 * 랭킹에 대한 연산은 DB에 접근하지 않고 Map을 활용.
 * 
 * @author
 */
public class L1Rank {

	public L1Rank() { }

	private int id;
	public int getId() {
		return id;
	}
	public void setId(int a) {
		id = a;
	}

	private String name;
	public String getName() {
		return name;
	}
	public void setName(String s) {
		name = s;
	}

	private String accountName;
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String s) {
		accountName = s;
	}

	private int type;
	public int getType() {
		return type;
	}
	public void setType(int a) {
		type = a;
	}

	private int exp;
	public int getExp() {
		return exp;
	}
	public void setExp(int a) {
		exp = a;
	}

	private int totalRank;
	public int getTotalRank() {
		return totalRank;
	}
	public void setTotalRank(int a) {
		totalRank = a;
	}

	private int classRank;
	public int getClassRank() {
		return classRank;
	}
	public void setClassRank(int a) {
		classRank = a;
	}

	private int oldTotalRank;
	public int getOldTotalRank() {
		return oldTotalRank;
	}
	public void setOldTotalRank(int a) {
		oldTotalRank = a;
	}

	private int oldClassRank;
	public int getOldClassRank() {
		return oldClassRank;
	}
	public void setOldClassRank(int a) {
		oldClassRank = a;
	}

	/*private boolean 전체상승기회;
	public boolean 전체상승기회() {
		return 전체상승기회;
	}
	public void 전체상승기회(boolean f) {
		전체상승기회 = f;
	}*/

	private boolean overallOpportunity;
	public boolean overallOpportunity() {
		return overallOpportunity;
	}
	public void overallOpportunity(boolean f) {
		overallOpportunity = f;
	}

	/*private boolean 전체추월위험;
	public boolean 전체추월위험() {
		return 전체추월위험;
	}
	public void 전체추월위험(boolean f) {
		전체추월위험 = f;
	}*/

	private boolean overallOvertakingRisk;
	public boolean overallOvertakingRisk() {
		return overallOvertakingRisk;
	}
	public void overallOvertakingRisk(boolean f) {
		overallOvertakingRisk = f;
	}

	/*private boolean 클래스상승기회;
	public boolean 클래스상승기회() {
		return 클래스상승기회;
	}
	public void 클래스상승기회(boolean f) {
		클래스상승기회 = f;
	}*/

	private boolean classPromotionOpportunity;
	public boolean classPromotionOpportunity() {
		return classPromotionOpportunity;
	}
	public void classPromotionOpportunity(boolean f) {
		classPromotionOpportunity = f;
	}

	/*private boolean 클래스추월위험;
	public boolean 클래스추월위험() {
		return 클래스추월위험;
	}
	public void 클래스추월위험(boolean f) {
		클래스추월위험 = f;
	}*/

	private boolean classOvertakeRisk;
	public boolean classOvertakeRisk() {
		return classOvertakeRisk;
	}
	public void classOvertakeRisk(boolean f) {
		classOvertakeRisk = f;
	}

	private int rankLevel = -1;

	public int getRankLevel() { // 랭킹버프 레벨은 오직 전체랭킹에만 관계가 있다. 클래스랭킹은 버프와 관련없음.(본섭화)
		if (rankLevel != -1) {
			return rankLevel;
		}
		if (totalRank <= 10) {
			return 4;
		}
		if (totalRank <= 30) {
			return 3;
		}
		if (totalRank <= 60) {
			return 2;
		}
		if (totalRank <= 100) {
			return 1;
		}
		return 0;
	}
}
