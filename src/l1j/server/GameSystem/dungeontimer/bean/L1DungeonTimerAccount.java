package l1j.server.GameSystem.dungeontimer.bean;

import java.sql.Timestamp;

/**
 * Account 던전 타이머
 * @author LinOffice
 */
public class L1DungeonTimerAccount extends L1DungeonTimerUser {
	private String account;
	public L1DungeonTimerAccount(String account, int timerId, int remainSecond, int chargeCount, Timestamp resetTime) {
		super(timerId, remainSecond, chargeCount, resetTime);
		this.account = account;
	}
	public String getAccount() {
		return account;
	}
}

