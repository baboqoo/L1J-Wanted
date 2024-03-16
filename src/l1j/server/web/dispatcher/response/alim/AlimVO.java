package l1j.server.web.dispatcher.response.alim;

import java.sql.Timestamp;

public class AlimVO {
	private Integer id;
	private String accountName;
	private String logContent;
	private int type;
	private Timestamp insertTime;
	private boolean status;
	
	public AlimVO(String accountName, String logContent, int type, Timestamp insertTime, boolean status) {
		this(0, accountName, logContent, type, insertTime, status);
	}
	public AlimVO(int id, String accountName, String logContent, int type, Timestamp insertTime, boolean status) {
		this.id				= id;
		this.accountName	= accountName;
		this.logContent		= logContent;
		this.type			= type;
		this.insertTime		= insertTime;
		this.status			= status;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getLogContent() {
		return logContent;
	}
	public void setLogContent(String logContent) {
		this.logContent = logContent;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Timestamp getInsertTime() {
		return insertTime;
	}
	public void setInsertTime(Timestamp insertTime) {
		this.insertTime = insertTime;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
}

