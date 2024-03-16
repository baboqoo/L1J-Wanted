package l1j.server.server.templates;

import l1j.server.server.model.Instance.L1PetInstance;


public class L1CompanionStatBonus {
	private int _meleeDmg;
	private int _meleeHit;
	private int _meleeCri;
	private int _regenHP;
	private int _add_min_HP;
	private int _add_max_HP;
	private int _AC;
	private int _mr;
	private int _reduction;
	private int _spellDmg;
	private int _spellHit;
	private int _spellCri;
	
	public int get_meleeDmg() {
		return _meleeDmg;
	}
	public int get_meleeHit() {
		return _meleeHit;
	}
	public int get_meleeCri() {
		return _meleeCri;
	}
	public int get_regenHP() {
		return _regenHP;
	}
	public int get_add_min_HP() {
		return _add_min_HP;
	}
	public int get_add_max_HP() {
		return _add_max_HP;
	}
	public int get_AC() {
		return _AC;
	}
	public int get_mr() {
		return _mr;
	}
	public int get_reduction() {
		return _reduction;
	}
	public int get_spellDmg() {
		return _spellDmg;
	}
	public int get_spellHit() {
		return _spellHit;
	}
	public int get_spellCri() {
		return _spellCri;
	}
	
	public void set(L1PetInstance companion) {
		int level	= companion.getLevel();

		byte str	= companion.getAbility().getTotalStr();
		int add_str = str - 10;
		int newDmg = (str / 3) + (level >> 2) + (add_str);
		int newHit = (str / 3) + (level / 3) + (add_str >> 1);
		int newCri = (str / 5) + (add_str / 10);
        _meleeDmg += newDmg;
        _meleeHit += newHit;
        _meleeCri += newCri;

		byte con	= companion.getAbility().getTotalCon();
		int add_con = con - 10;
        int newRegenHP = (level >> 2) + (add_con >> 1);
        int newAc = 14 + add_con + (level / 10);
        int newMr = 21 + ((add_con >> 2) << 2);
        int newReduction = con / 10;
        int newAddMinHP = add_con >= 37 ? 7 : add_con >= 32 ? 6 : add_con >= 22 ? 5 : add_con >= 10 ? 4 : add_con >= 8 ? 3 : add_con >= 5 ? 2 : add_con >= 2 ? 1 : 0;
        int newAddMaxHP = add_con >= 40 ? 7 : add_con >= 37 ? 6 : add_con >= 27 ? 5 : add_con >= 22 ? 4 : add_con >= 17 ? 3 : add_con >= 10 ? 2 : add_con >= 8 ? 1 : 0;
        _regenHP += newRegenHP;
        _AC += -newAc;
        _mr += newMr;
        _reduction += newReduction;
        _add_min_HP += newAddMinHP;
        _add_max_HP += newAddMaxHP;

		byte intel	= companion.getAbility().getTotalInt();
        int add_int = intel - 10;
        int newSkillDmg = intel + (add_int << 1);
        int newSkillHit = (intel / 10) + (add_int >> 1);
        int newSkillCri = add_int / 3;
        _spellDmg += newSkillDmg;
        _spellHit += newSkillHit;
        _spellCri += newSkillCri;
	}
	
	public void reset() {
		_meleeDmg = _meleeHit = _meleeCri = 0;
		_regenHP = _add_min_HP = _add_max_HP = _AC = _mr = _reduction = 0;
		_spellDmg = _spellHit = _spellCri = 0;
	}
}

