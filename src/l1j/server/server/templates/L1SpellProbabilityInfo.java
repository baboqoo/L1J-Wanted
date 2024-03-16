package l1j.server.server.templates;

import static l1j.server.server.model.skill.L1SkillId.CANCELLATION;
import static l1j.server.server.model.skill.L1SkillId.COUNTER_BARRIER;
import static l1j.server.server.model.skill.L1SkillId.SHAPE_CHANGE;

import java.sql.ResultSet;
import java.sql.SQLException;

import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1PcInstance;

public class L1SpellProbabilityInfo {
	private boolean _is_loggin;
	private int _skill_id;
	private String _description;
	private String _skill_type;
	private double _pierce_lv_weight;
	private double _resis_lv_weight;
	private double _int_weight;
	private double _mr_weight;
	private double _pierce_weight;
	private double _resis_weight;
	private int _default_probability;
	private int _min_probability;
	private int _max_probability;

    public L1SpellProbabilityInfo(ResultSet rs) throws SQLException{
		_skill_id = rs.getInt("skill_id");
		_description = rs.getString("description");
		_skill_type = rs.getString("skill_type");
		_pierce_lv_weight = Double.parseDouble(rs.getString("pierce_lv_weight"));
		_resis_lv_weight = Double.parseDouble(rs.getString("resis_lv_weight"));
		_int_weight = Double.parseDouble(rs.getString("int_weight"));
		_mr_weight = Double.parseDouble(rs.getString("mr_weight"));
		_pierce_weight = Double.parseDouble(rs.getString("pierce_weight"));
		_resis_weight = Double.parseDouble(rs.getString("resis_weight"));
		_default_probability = rs.getInt("default_probability");
		_min_probability = rs.getInt("min_probability");
		_max_probability = rs.getInt("max_probability");
		_is_loggin = rs.getBoolean("is_loggin");
	}
	
	public int get_skill_id() {
		return _skill_id;
	}
	
	public String get_description() {
		return _description;
	}
	
	public String get_skill_type() {
		return _skill_type;
	}
	
	public double get_pierce_lv_weight() {
		return _pierce_lv_weight;
	}
	
	public double get_resis_lv_weight() {
		return _resis_lv_weight;
	}
	
    public 	double get_int_weight() {
		return _int_weight;
	}
	
	public double get_mr_weight() {
		return _mr_weight;
	}
	
	public double get_pierce_weight() {
		return _pierce_weight;
	}
	
	public double get_resis_weight() {
		return _resis_weight;
	}
	
	public int get_default_probability() {
		return _default_probability;
	}
	
	public int get_min_probability() {
		return _min_probability;
	}
	
	public int get_max_probability() {
		return _max_probability;
	}
	
	public void print() {
	}
	
	public int calc_probability(L1PcInstance pc, L1Character target, int attacker_int, int target_mr) {
		int probability 	= 0;
		int attackLevel 	= pc.getLevel();
		int defenseLevel 	= target.getLevel();
		int PiercePoint		= 0;
		int ResisPoint		= 0;
	
		if (get_skill_type().equalsIgnoreCase("MAGICHIT")) {
			PiercePoint = (int) ((attacker_int * _int_weight) + (pc.getAbility().getMagicHitup() * _pierce_weight));
			ResisPoint	= (int) (target_mr * _mr_weight);
		} else if (get_skill_type().equalsIgnoreCase("ABILITY")) {
			PiercePoint = (int) ((pc.getResistance().getHitupSpirit()) * _pierce_weight);
			ResisPoint = (int) ((target.getResistance().getToleranceSkill()) * _resis_weight);
		} else if (get_skill_type().equalsIgnoreCase("SPIRIT")) {
			PiercePoint = (int) ((pc.getResistance().getHitupSpirit()) * _pierce_weight);
			ResisPoint = (int) ((target.getResistance().getToleranceSpirit()) * _resis_weight);
		}
					
		if (attackLevel >= defenseLevel)
			probability = (int) (((attackLevel - defenseLevel) * _pierce_lv_weight) + (PiercePoint - ResisPoint)) + _default_probability;
		else if (attackLevel < defenseLevel)
			probability = (int) (((attackLevel - defenseLevel) * _resis_lv_weight) + (PiercePoint - ResisPoint)) + _default_probability;
		
		probability = Math.max(probability, _min_probability);
		probability = Math.min(probability, _max_probability);
		
		switch (_skill_id) { 
		case COUNTER_BARRIER: // 카운터 배리어
			if (pc != null) {
				int lvl = pc.getLevel();
				if (lvl >= 85) {
					probability += (lvl - 84);
				}
			}
			break;
		case SHAPE_CHANGE:
			if (target == pc) {
				probability = 100;
			}
			break;
		case CANCELLATION:
			if (!pc.isWizard()) {
				probability *= 0.5;
			}
			break; 
		}
		
		if (_is_loggin) {
			if (pc.isGm()) {
				System.out.println(String.format("스킬 : %s(%d,%d~%d) 공격자:%s(%d), 타겟:%s(%d), 확률:%d", _description, _skill_id, _min_probability, _max_probability, pc.getName(), PiercePoint, target.getName(), ResisPoint, probability));
				System.out.println(String.format("-> weight info... pierce:%d,resis:%d",
						PiercePoint, ResisPoint));
			}
		}
		return probability;
	}
	    
}
