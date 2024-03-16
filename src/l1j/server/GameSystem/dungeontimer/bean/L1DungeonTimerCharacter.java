package l1j.server.GameSystem.dungeontimer.bean;

import java.sql.Timestamp;

/**
 * Character 던전 타이머
 * @author LinOffice
 */
public class L1DungeonTimerCharacter extends L1DungeonTimerUser {
	private int charId;
	public L1DungeonTimerCharacter(int charId, int timerId, int remainSecond, int chargeCount, Timestamp resetTime) {
		super(timerId, remainSecond, chargeCount, resetTime);
		this.charId = charId;
	}
	public int getCharId() {
		return charId;
	}
}

