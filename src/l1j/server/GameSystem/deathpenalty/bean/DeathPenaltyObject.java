package l1j.server.GameSystem.deathpenalty.bean;

import java.sql.Timestamp;

/**
 * 패널티 공통 추상 클래스
 * @author LinOffice
 */
public abstract class DeathPenaltyObject {
	protected int char_id, recovery_cost;
	protected Timestamp delete_time;
	
	public DeathPenaltyObject(int char_id, Timestamp delete_time, int recovery_cost) {
		this.char_id		= char_id;
		this.delete_time	= delete_time;
		this.recovery_cost	= recovery_cost;
	}
	
	public int get_char_id() {
		return char_id;
	}
	public Timestamp get_delete_time() {
		return delete_time;
	}
	public int get_recovery_cost() {
		return recovery_cost;
	}
	
	@Override
	public abstract String toString();
}

