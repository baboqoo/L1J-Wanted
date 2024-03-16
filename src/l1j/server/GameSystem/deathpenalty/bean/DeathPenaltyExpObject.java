package l1j.server.GameSystem.deathpenalty.bean;

import java.sql.Timestamp;

import l1j.server.server.utils.StringUtil;

/**
 * 경험치 패널티 클래스
 * @author LinOffice
 */
public class DeathPenaltyExpObject extends DeathPenaltyObject {
	private int index = -1;
	private int death_level, exp_value;
	private float exp_ratio;
	
	public DeathPenaltyExpObject(int char_id, Timestamp delete_time, int death_level, float exp_ratio, int exp_value, int recovery_cost) {
		super(char_id, delete_time, recovery_cost);
		this.death_level	= death_level;
		this.exp_ratio		= exp_ratio;
		this.exp_value		= exp_value;
	}
	
	public int get_index() {
		return index;
	}
	public void set_index(int val) {
		index = val;
	}
	public int get_death_level() {
		return death_level;
	}
	public float get_exp_ratio() {
		return exp_ratio;
	}
	public int get_exp_value() {
		return exp_value;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("char_id: ").append(char_id).append(StringUtil.LineString);
		sb.append("delete_time: ").append(delete_time.getTime()).append(StringUtil.LineString);
		sb.append("recovery_cost: ").append(recovery_cost).append(StringUtil.LineString);
		sb.append("index: ").append(index).append(StringUtil.LineString);
		sb.append("death_level: ").append(death_level).append(StringUtil.LineString);
		sb.append("exp_ratio: ").append(exp_ratio).append(StringUtil.LineString);
		sb.append("exp_value: ").append(exp_value);
		return sb.toString();
	}
}

