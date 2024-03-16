package l1j.server.GameSystem.tjcoupon.bean;

import l1j.server.server.model.Instance.L1ItemInstance;

public class TJLogBean {
	private int logOwnerId;
	private L1ItemInstance logItem;
	private String logTime;
	
	public TJLogBean(int logOwnerId, L1ItemInstance logItem, String logTime) {
		this.logOwnerId	= logOwnerId;
		this.logItem	= logItem;
		this.logTime	= logTime;
	}
	
	public int getLogOwnerId() {
		return logOwnerId;
	}
	public L1ItemInstance getLogItem() {
		return logItem;
	}
	public String getLogTime() {
		return logTime;
	}
}

