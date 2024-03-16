package l1j.server.server.model;

public class L1PcSpeedSync {
	long moveSyncInterval, attackSyncInterval;
	int moveSpeedOverCount, attackSpeedOverCount;
	
	public long getMoveSyncInterval() {
		return moveSyncInterval;
	}
	public void setMoveSyncInterval(long val) {
		moveSyncInterval = val;
	}
	
	public long getAttackSyncInterval() {
		return attackSyncInterval;
	}
	public void setAttackSyncInterval(long val) {
		attackSyncInterval = val;
	}
	
	public int increaseMoveSpeedOverCountAndGet() {
		return ++moveSpeedOverCount;
	}
	public int decreaseMoveSpeedOverCountAndGet() {
		return moveSpeedOverCount == 0 ? 0 : --moveSpeedOverCount;
	}
	public void resetMoveSpeedOverCount() {
		moveSpeedOverCount = 0;
	}
	
	public int increaseAttackSpeedOverCountAndGet() {
		return ++attackSpeedOverCount;
	}
	public int decreaseAttackSpeedOverCountAndGet() {
		return attackSpeedOverCount == 0 ? 0 : --attackSpeedOverCount;
	}
	public void resetAttackSpeedOverCount() {
		attackSpeedOverCount = 0;
	}
	
	public L1PcSpeedSync(){
		init();
	}
	
	void init() {
		moveSyncInterval = attackSyncInterval = System.currentTimeMillis();
		moveSpeedOverCount = attackSpeedOverCount = 0;
	}
}

