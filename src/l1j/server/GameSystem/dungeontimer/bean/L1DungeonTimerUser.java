package l1j.server.GameSystem.dungeontimer.bean;

import java.sql.Timestamp;

import l1j.server.GameSystem.dungeontimer.loader.L1DungeonTimerLoader;

public abstract class L1DungeonTimerUser {
	protected int timerId;
	protected int remainSecond;
	protected int chargeCount;
	protected Timestamp resetTime;
	protected L1DungeonTimerInfo info;
	
	public L1DungeonTimerUser(int timerId, int remainSecond, int chargeCount, Timestamp resetTime) {
		this.timerId		= timerId;
		this.remainSecond	= remainSecond;
		this.chargeCount	= chargeCount;
		this.resetTime		= resetTime;
		this.info			= L1DungeonTimerLoader.getDungeonTimerId(this.timerId);
	}
	
	public int getTimerId() {
		return timerId;
	}
	
	public void setTimerId(int timerId) {
		this.timerId = timerId;
	}

	public int getRemainSecond() {
		return remainSecond;
	}

	public void setRemainSecond(int remainSecond) {
		this.remainSecond = remainSecond;
	}
	
	public int increaseAndGetRemainSecond() {
		return ++this.remainSecond;
	}

	public int getChargeCount() {
		return chargeCount;
	}

	public void setChargeCount(int chargeCount) {
		this.chargeCount = chargeCount;
	}

	public Timestamp getResetTime() {
		return resetTime;
	}

	public void setResetTime(Timestamp resetTime) {
		this.resetTime = resetTime;
	}

	public L1DungeonTimerInfo getInfo() {
		return info;
	}

	public void setInfo(L1DungeonTimerInfo info) {
		this.info = info;
	}
	
	public void reset(){
		remainSecond = chargeCount = 0;
		if (resetTime != null) {
			resetTime.setTime(System.currentTimeMillis());
		} else {
			resetTime = new Timestamp(System.currentTimeMillis());
		}
	}
}

