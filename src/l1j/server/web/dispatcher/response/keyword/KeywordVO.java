package l1j.server.web.dispatcher.response.keyword;

public class KeywordVO {
	private String keyword;
	private int curRank;
	private int oldRank;
	private int view;
	
	public KeywordVO(String keyword, int curRank, int oldRank, int view) {
		this.keyword	= keyword;
		this.curRank	= curRank;
		this.oldRank	= oldRank;
		this.view		= view;
	}
	
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public int getCurRank() {
		return curRank;
	}
	public void setCurRank(int curRank) {
		this.curRank = curRank;
	}
	public int getOldRank() {
		return oldRank;
	}
	public void setOldRank(int oldRank) {
		this.oldRank = oldRank;
	}
	public int getView() {
		return view;
	}
	public void setView(int view) {
		this.view = view;
	}
	public void addView() {
		this.view++;
	}
}

